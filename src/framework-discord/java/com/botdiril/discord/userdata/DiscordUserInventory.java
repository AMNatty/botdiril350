package com.botdiril.discord.userdata;

import com.botdiril.framework.sql.DBConnection;
import com.botdiril.userdata.InventoryTables;
import com.botdiril.userdata.UserInventory;

import java.sql.Statement;

public class DiscordUserInventory extends UserInventory
{
    protected final long userID;

    private static int getOrCreateUser(DBConnection db, long userID)
    {
        var user = db.getValue("SELECT us_id FROM " + InventoryTables.TABLE_USER + " WHERE us_userid=?", "us_id", Integer.class, userID);

        if (user.isEmpty())
        {
            return db.exec("INSERT INTO " + InventoryTables.TABLE_USER + "(us_userid) VALUES (?)", Statement.RETURN_GENERATED_KEYS, stat -> {
                stat.setLong(1, userID);
                stat.executeUpdate();
                var keys = stat.getGeneratedKeys();
                keys.next();
                return keys.getInt(1);
            });
        }
        else
        {
            return user.get();
        }
    }

    public DiscordUserInventory(DBConnection connection, long userid)
    {
        super(connection, getOrCreateUser(connection, userid));

        this.userID = userid;
    }

    public long getUserID()
    {
        return this.userID;
    }
}
