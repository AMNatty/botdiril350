package cz.tefek.botdiril.framework.sql;

import java.sql.DriverManager;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.MajorFailureException;
import cz.tefek.botdiril.util.BotdirilLog;

public class SqlFoundation
{
    public static final String SCHEMA = "botdiril35";

    public static void build()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (var c = DriverManager.getConnection("jdbc:mysql://" + BotMain.config.getSqlHost()
                                                     + "/?useUnicode=true"
                                                     + "&autoReconnect=true"
                                                     + "&useJDBCCompliantTimezoneShift=true"
                                                     + "&useLegacyDatetimeCode=false"
                                                     + "&serverTimezone=UTC",
                BotMain.config.getSqlUser(), BotMain.config.getSqlPass()))
            {
                try (var stat = c.prepareStatement("SHOW DATABASES LIKE ?"))
                {
                    stat.setString(1, SCHEMA);

                    if (!stat.executeQuery().next())
                    {
                        BotdirilLog.logger.info("Database needs to be reconstructed.");

                        try (var createStat = c.prepareStatement("CREATE DATABASE " + SCHEMA))
                        {
                            createStat.execute();
                        }
                    }

                    BotMain.SQL_MANAGER = new SqlConnectionManager();
                }
            }
        }
        catch (Exception e)
        {
            throw new MajorFailureException("An error has occured while preparing the database structure.", e);
        }
    }

    public static boolean checkTableExists(DBConnection db, String name)
    {
        return db.exec("SHOW TABLES LIKE ?", stat -> stat.executeQuery().next(), name);
    }
}
