package com.botdiril.userdata;

import com.botdiril.framework.sql.DBConnection;
import com.botdiril.framework.sql.SqlFoundation;

public class InventoryTables
{
    public static final String TABLE_USER = "users";
    public static final String TABLE_INVENTORY = "inventory";
    public static final String TABLE_CARDS = "cards";
    public static final String TABLE_ACHIEVEMENTS = "achievements";
    public static final String TABLE_GIFTCODES = "giftcodes";
    public static final String TABLE_TIMERS = "timers";

    public static void initTables(DBConnection db)
    {
        var tabExists = SqlFoundation.checkTableExists(db, TABLE_USER);

        if (!tabExists)
        {
            db.simpleExecute("CREATE TABLE " + TABLE_USER + "( " +
                             "us_id INT PRIMARY KEY AUTO_INCREMENT, " +
                             "us_userid BIGINT NOT NULL UNIQUE, " +
                             "us_coins BIGINT NOT NULL DEFAULT 0, " +
                             "us_keks BIGINT NOT NULL DEFAULT 0, " +
                             "us_tokens BIGINT NOT NULL DEFAULT 0, " +
                             "us_keys BIGINT NOT NULL DEFAULT 0, " +
                             "us_mega BIGINT NOT NULL DEFAULT 0, " +
                             "us_dust BIGINT NOT NULL DEFAULT 0, " +
                             "us_level INT NOT NULL DEFAULT '1', " +
                             "us_xp BIGINT NOT NULL DEFAULT 0);");
        }

        var tabExistsInv = SqlFoundation.checkTableExists(db, TABLE_INVENTORY);

        if (!tabExistsInv)
        {
            db.simpleExecute("CREATE TABLE " + TABLE_INVENTORY + "(" +
                             "fk_us_id INT NOT NULL, " +
                             "fk_il_id INT NOT NULL, " +
                             "it_amount BIGINT NOT NULL DEFAULT 0, " +
                             "FOREIGN KEY (fk_us_id) REFERENCES " + TABLE_USER + "(us_id), " +
                             "FOREIGN KEY (fk_il_id) REFERENCES " + ItemLookup.TABLE_ITEMLOOKUP + "(il_id) );");
        }

        var tabExistsCard = SqlFoundation.checkTableExists(db, TABLE_CARDS);

        if (!tabExistsCard)
        {
            db.simpleExecute("CREATE TABLE " + TABLE_CARDS + "(" +
                             "fk_us_id INT NOT NULL, " +
                             "fk_il_id INT NOT NULL, " +
                             "cr_amount BIGINT NOT NULL DEFAULT 0, " +
                             "cr_level INT NOT NULL DEFAULT 0, " +
                             "cr_xp BIGINT NOT NULL DEFAULT 0, " +
                             "FOREIGN KEY (fk_us_id) REFERENCES " + TABLE_USER + "(us_id), " +
                             "FOREIGN KEY (fk_il_id) REFERENCES " + ItemLookup.TABLE_ITEMLOOKUP + "(il_id));");
        }

        var tabExistsAchv = SqlFoundation.checkTableExists(db, TABLE_ACHIEVEMENTS);

        if (!tabExistsAchv)
        {
            db.simpleExecute("CREATE TABLE " + TABLE_ACHIEVEMENTS + "(" +
                             "fk_us_id INT NOT NULL, " +
                             "fk_il_id INT NOT NULL, " +
                             "FOREIGN KEY (fk_us_id) REFERENCES " + TABLE_USER + "(us_id), " +
                             "FOREIGN KEY (fk_il_id) REFERENCES " + ItemLookup.TABLE_ITEMLOOKUP + "(il_id));");
        }

        var tabExistsCodes = SqlFoundation.checkTableExists(db, TABLE_GIFTCODES);

        if (!tabExistsCodes)
        {
            db.simpleExecute("CREATE TABLE " + TABLE_GIFTCODES + "(" +
                             "fk_us_id INT NOT NULL, " +
                             "cd_code VARCHAR(32) NOT NULL, " +
                             "FOREIGN KEY (fk_us_id) REFERENCES " + TABLE_USER + "(us_id));");
        }

        var tabExistsTime = SqlFoundation.checkTableExists(db, TABLE_TIMERS);

        if (!tabExistsTime)
        {
            db.simpleExecute("CREATE TABLE " + TABLE_TIMERS + "(" +
                             "fk_us_id INT NOT NULL, " +
                             "fk_il_id INT NOT NULL, " +
                             "tm_time BIGINT NOT NULL, " +
                             "FOREIGN KEY (fk_us_id) REFERENCES " + TABLE_USER + "(us_id), " +
                             "FOREIGN KEY (fk_il_id) REFERENCES " + ItemLookup.TABLE_ITEMLOOKUP + "(il_id));");
        }
    }
}
