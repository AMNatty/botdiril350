package com.botdiril;

import com.botdiril.serverdata.ChannelPreferences;
import com.botdiril.serverdata.RolePreferences;
import com.botdiril.serverdata.ServerPreferences;

import java.util.Locale;

import com.botdiril.framework.command.CommandIntitializer;
import com.botdiril.framework.sql.SqlConnectionManager;
import com.botdiril.framework.sql.SqlFoundation;
import com.botdiril.internal.BotdirilConfig;
import com.botdiril.userdata.InventoryTables;
import com.botdiril.userdata.ItemLookup;
import com.botdiril.userdata.achievement.Achievements;
import com.botdiril.userdata.card.Cards;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.items.Items;
import com.botdiril.userdata.metrics.UserMetrics;
import com.botdiril.userdata.properties.PropertyObject;
import com.botdiril.userdata.timers.EnumTimer;
import com.botdiril.userdata.xp.XPRewards;
import com.botdiril.util.BotdirilLog;

public class BotMain
{
    public static BotdirilConfig config;
    public static Botdiril botdiril;

    public static SqlConnectionManager SQL_MANAGER;

    public static void main(String[] args)
    {
        BotdirilLog.init();

        BotdirilLog.logger.info("=====================================");
        BotdirilLog.logger.info("####     BOTDIRIL MESON 350      ####");
        BotdirilLog.logger.info("=====================================");

        Locale.setDefault(Locale.US);


        try
        {
            config = BotdirilConfig.load();

            SqlFoundation.build();

            try (var db = BotMain.SQL_MANAGER.getConnection())
            {
                ItemLookup.prepare(db);

                InventoryTables.initTables(db);

                UserMetrics.initTable(db);

                ServerPreferences.load(db);

                RolePreferences.load(db);

                PropertyObject.init(db);

                Icons.load();

                Items.load();

                Cards.load();

                Class.forName(EnumTimer.class.getName());

                Class.forName(Achievements.class.getName());

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
