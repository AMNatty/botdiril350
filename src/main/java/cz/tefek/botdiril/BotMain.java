package cz.tefek.botdiril;

import java.util.Locale;

import cz.tefek.botdiril.framework.command.CommandIntitializer;
import cz.tefek.botdiril.framework.sql.SqlConnectionManager;
import cz.tefek.botdiril.framework.sql.SqlFoundation;
import cz.tefek.botdiril.internal.BotdirilConfig;
import cz.tefek.botdiril.serverdata.ChannelPreferences;
import cz.tefek.botdiril.serverdata.RolePreferences;
import cz.tefek.botdiril.serverdata.ServerPreferences;
import cz.tefek.botdiril.userdata.ItemLookup;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.achievement.Achievements;
import cz.tefek.botdiril.userdata.card.Cards;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.metrics.UserMetrics;
import cz.tefek.botdiril.userdata.properties.PropertyObject;
import cz.tefek.botdiril.userdata.timers.Timers;
import cz.tefek.botdiril.userdata.xp.XPRewards;
import cz.tefek.botdiril.util.BotdirilLog;

public class BotMain
{
    public static BotdirilConfig config;
    public static Botdiril botdiril;

    public static SqlConnectionManager SQL_MANAGER;

    public static void main(String[] args)
    {
        BotdirilLog.init();

        BotdirilLog.logger.info("=====================================");
        BotdirilLog.logger.info("####       BOTDIRIL 350          ####");
        BotdirilLog.logger.info("=====================================");

        Locale.setDefault(Locale.US);


        try
        {
            config = BotdirilConfig.load();

            SqlFoundation.build();

            try (var db = BotMain.SQL_MANAGER.getConnection())
            {
                ItemLookup.prepare(db);

                UserInventory.initTables(db);

                UserMetrics.initTable(db);

                ServerPreferences.load(db);

                RolePreferences.load(db);

                PropertyObject.init(db);

                Items.load();

                Cards.load();

                Timers.load();

                Achievements.load();

                XPRewards.populate();

                CommandIntitializer.load();

                ItemLookup.save(db);

                ChannelPreferences.load(db);

                db.commit();
            }

            botdiril = new Botdiril();
        }
        catch (MajorFailureException e)
        {
            BotdirilLog.logger.fatal("An unrecoverable failure has occurred while initializing the bot.", e);
        }
        catch (Exception e)
        {
            BotdirilLog.logger.fatal("A general unrecoverable failure has occurred while initializing the bot.", e);
        }
    }
}
