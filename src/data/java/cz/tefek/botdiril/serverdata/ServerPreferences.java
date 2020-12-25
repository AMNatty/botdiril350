package cz.tefek.botdiril.serverdata;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

import cz.tefek.botdiril.MajorFailureException;
import cz.tefek.botdiril.framework.sql.DBConnection;
import cz.tefek.botdiril.framework.sql.SqlFoundation;
import cz.tefek.botdiril.internal.BotdirilConfig;
import cz.tefek.botdiril.util.BotdirilLog;

public class ServerPreferences
{
    static final String PREF_TABLE = "serverconfig";

    private static List<ServerConfig> cfgs;

    public static void load(DBConnection db)
    {
        cfgs = new ArrayList<>();

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

                cfgs.add(config);
            }

            return true;
        });

        if (!success)
        {
            throw new MajorFailureException("Error while loading server preferences, aborting.");
        }
    }

    public static synchronized ServerConfig getConfigByGuild(long id)
    {
        for (ServerConfig serverConfig : cfgs)
        {
            if (serverConfig.getServerID() == id)
            {
                return serverConfig;
            }
        }

        return null;
    }

    public static synchronized void addGuild(DBConnection db, Guild g)
    {
        var prem = g.getTextChannelsByName("botdiril", true);

        TextChannel pc;

        if (!prem.isEmpty())
        {
            pc = prem.get(0);
        }
        else
        {
            pc = g.createTextChannel("botdiril").complete();
        }

        var sc = new ServerConfig(g.getIdLong());
        sc.setLoggingChannel(db, pc.getIdLong());

        cfgs.add(sc);

        db.simpleUpdate("INSERT INTO " + PREF_TABLE + "(sc_guild, sc_logchannel) VALUES (?, ?)", sc.getServerID(), sc.getLoggingChannel());

        pc.sendMessage("Hello! This is Botdiril350! Please set a prefix via `" + BotdirilConfig.UNIVERSAL_PREFIX + "prefix <prefix>`. You can change it later. To set a logging channel please use `" + BotdirilConfig.UNIVERSAL_PREFIX + "channel <log channel>`.").submit();
    }
}
