package com.botdiril.serverdata;

import com.botdiril.Botdiril;
import com.botdiril.MajorFailureException;
import com.botdiril.framework.sql.DBConnection;
import com.botdiril.framework.sql.SqlFoundation;
import com.botdiril.internal.BotdirilConfig;
import com.botdiril.util.BotdirilLog;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ServerPreferences
{
    static final String PREF_TABLE = "serverconfig";

    private static Map<Long, ServerConfig> cfgs;

    public static void load(DBConnection db)
    {
        cfgs = new HashMap<>();

        var tabExists = SqlFoundation.checkTableExists(db, PREF_TABLE);

        if (!tabExists)
        {
            db.simpleExecute("CREATE TABLE " + PREF_TABLE + "( " +
                             "sc_guild BIGINT PRIMARY KEY , " +
                             "sc_logchannel BIGINT, " +
                             "sc_prefix VARCHAR(16) );");

            return;
        }

        var success = db.exec("SELECT * FROM " + PREF_TABLE, stat ->
        {
            var rs = stat.executeQuery();

            while (rs.next())
            {
                var gid = rs.getLong("sc_guild");

                BotdirilLog.logger.info("Loading guild " + gid);

                var config = new ServerConfig(gid);

                config.loadLoggingChannel(rs.getLong("sc_logchannel"));
                config.loadPrefix(rs.getString("sc_prefix"));

                cfgs.put(gid, config);
            }

            return true;
        });

        if (!success)
        {
            throw new MajorFailureException("Error while loading server preferences, aborting.");
        }
    }

    public static synchronized @Nullable ServerConfig getConfigByGuild(long id)
    {
        return cfgs.get(id);
    }

    public static synchronized void addGuild(DBConnection db, Guild g)
    {
        var prem = g.getTextChannelsByName("botdiril", true);

        Optional<TextChannel> suitableChannel = Optional.empty();

        if (!prem.isEmpty())
        {
            suitableChannel = prem.stream().filter(TextChannel::canTalk).findFirst();
        }

        var pc = suitableChannel.orElseGet(() -> g.createTextChannel("botdiril").complete());

        var id = g.getIdLong();
        var sc = new ServerConfig(id);
        sc.setLoggingChannel(db, pc.getIdLong());

        cfgs.put(id, sc);

        db.simpleUpdate("INSERT INTO " + PREF_TABLE + "(sc_guild, sc_logchannel) VALUES (?, ?)", sc.getServerID(), sc.getLoggingChannel());

        pc.sendMessage("""
        Hello! This is %s! Please set a prefix via `%sprefix <prefix>`.
        You can change it later.
        To set a logging channel please use `%schannel <log channel>`.
        """.formatted(Botdiril.BRANDING, BotdirilConfig.UNIVERSAL_PREFIX, BotdirilConfig.UNIVERSAL_PREFIX)).submit();
    }
}
