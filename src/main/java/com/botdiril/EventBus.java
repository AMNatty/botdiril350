package com.botdiril;

import com.botdiril.serverdata.ServerPreferences;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.botdiril.command.general.CommandAlias;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.parser.CommandParser;
import com.botdiril.framework.sql.DBConnection;
import com.botdiril.framework.util.PrefixUtil;
import com.botdiril.userdata.UserInventory;
import com.botdiril.userdata.metrics.UserMetrics;
import com.botdiril.userdata.properties.PropertyObject;
import com.botdiril.util.BotdirilLog;

public class EventBus extends ListenerAdapter
{
    @Override
    public void onGuildJoin(GuildJoinEvent event)
    {
        var g = event.getGuild();

        BotdirilLog.logger.info("Joining guild " + g);

        var sc = ServerPreferences.getConfigByGuild(g.getIdLong());
        if (sc == null)
        {
            var scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.schedule(() -> {
                DBConnection db = BotMain.SQL_MANAGER.getConnection();
                try (db)
                {
                    ServerPreferences.addGuild(db, g);

                    db.commit();
                }
                catch (Exception e)
                {
                    BotdirilLog.logger.fatal("An exception has occured while setting up server preferences.", e);
                    db.rollback();
                }
            }, 5, TimeUnit.SECONDS);
        }
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        var user = event.getAuthor();
        var message = event.getMessage();
        var jda = event.getJDA();
        var botUser = jda.getSelfUser();
        var guild = event.getGuild();
        var textChannel = event.getChannel();

        if (!user.isBot())
        {
            var co = new CommandContext();
            co.caller = user;
            co.callerMember = event.getMember();
            co.guild = guild;
            co.message = message;
            co.sc = ServerPreferences.getConfigByGuild(co.guild.getIdLong());
            co.contents = message.getContentRaw();
            co.textChannel = textChannel;
            co.jda = jda;

            try
            {
                var db = BotMain.SQL_MANAGER.getConnection();
                try (db)
                {
                    co.db = db;

                    if (co.sc == null)
                    {
                        ServerPreferences.addGuild(co.db, co.guild);
                        co.sc = ServerPreferences.getConfigByGuild(co.guild.getIdLong());
                    }

                    co.bot = botUser;

                    co.ui = new UserInventory(co.db, co.caller.getIdLong());

                    co.po = new PropertyObject(co.db, co.ui.getFID());

                    CommandAlias.allAliases(co.po).forEach((src, repl) -> co.contents = co.contents.replace(src, repl));

                    if (!PrefixUtil.findPrefix(guild, co))
                    {
                        co.db.commit();
                        return;
                    }

                    if (CommandParser.parse(co))
                    {
                        UserMetrics.updateMetrics(co.db, co.ui);
                        co.db.commit();
                    }
                    else
                    {
                        co.db.rollback();
                    }
                }
                catch (Exception e)
                {
                    // co.respond("**An error has occured while processing the command.**\nPlease report this to the bot owner.");
                    BotdirilLog.logger.fatal("An exception has occured while invoking a command.", e);
                }
            }
            catch (Exception e)
            {
                // co.respond("**An error has occured while processing the command.**\nPlease report this to the bot owner.");
                BotdirilLog.logger.fatal("An exception has occured while invoking a command.", e);
            }
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event)
    {
        try
        {
            var db = BotMain.SQL_MANAGER.getConnection();

            try (db)
            {
                event.getJDA().getGuilds().forEach(g ->
                {
                    var sc = ServerPreferences.getConfigByGuild(g.getIdLong());
                    if (sc == null)
                    {
                        ServerPreferences.addGuild(db, g);
                    }
                });

                db.commit();
            }
            catch (Exception e)
            {
                db.rollback();
                BotdirilLog.logger.fatal("An exception has occured while setting up server preferences.", e);
            }
        }
        catch (Exception e)
        {
            BotdirilLog.logger.fatal("An exception has occured while setting up server preferences.", e);
        }
    }
}
