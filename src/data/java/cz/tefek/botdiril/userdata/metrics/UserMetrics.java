package cz.tefek.botdiril.userdata.metrics;

import cz.tefek.botdiril.framework.sql.DBConnection;
import cz.tefek.botdiril.framework.sql.SqlFoundation;
import cz.tefek.botdiril.userdata.UserInventory;

public class UserMetrics
{
    public static final String TABLE_USER_METRICS = "metrics";

    public static void initTable(DBConnection db)
    {
        var tabExists = SqlFoundation.checkTableExists(db, TABLE_USER_METRICS);

        if (!tabExists)
        {
            db.simpleExecute("""
             CREATE TABLE %s
             (
                 fk_us_id INT NOT NULL,
                 um_commandid INT NOT NULL AUTO_INCREMENT,
                 um_coins BIGINT NOT NULL,
                 um_keks BIGINT NOT NULL,
                 um_tokens BIGINT NOT NULL,
                 um_keys BIGINT NOT NULL,
                 um_mega BIGINT NOT NULL,
                 um_dust BIGINT NOT NULL,
                 um_level INT NOT NULL,
                 um_xp BIGINT NOT NULL,
                 
                 PRIMARY KEY (fk_us_id, um_commandid),
                 FOREIGN KEY (fk_us_id) REFERENCES %s(us_id)
             ) ENGINE = MyISAM;
             """.formatted(TABLE_USER_METRICS, UserInventory.TABLE_USER));
        }
    }

    public static void updateMetrics(DBConnection db, UserInventory ui)
    {
        var uiObj = ui.getUserDataObj();

        db.simpleUpdate("""
            INSERT INTO %s
            (fk_us_id, um_coins, um_keks, um_tokens, um_keys, um_mega, um_dust, um_level, um_xp)
            VALUES
            (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.formatted(TABLE_USER_METRICS), ui.getFID(), uiObj.getCoins(), uiObj.getKeks(), uiObj.getTokens(), uiObj.getKeys(), uiObj.getMegaKeks(), uiObj.getDust(), uiObj.getLevel(), uiObj.getXP());
    }
}
