package cz.tefek.botdiril.serverdata;

import org.intellij.lang.annotations.MagicConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import cz.tefek.botdiril.framework.sql.DBConnection;
import cz.tefek.botdiril.framework.sql.SqlFoundation;

public class ChannelPreferences
{
    static final String TABLE_CHANNELPROPERTIES = "channelproperties";

    public static final int BIT_DISABLED = 1;

    @Target({ ElementType.PARAMETER, ElementType.LOCAL_VARIABLE })
    @MagicConstant(intValues = { BIT_DISABLED })
    private @interface ChannelBit
    {

    }

    public static void load(DBConnection db)
    {
        var tabExists = SqlFoundation.checkTableExists(db, TABLE_CHANNELPROPERTIES);

        if (!tabExists)
        {
            db.simpleExecute("CREATE TABLE " + TABLE_CHANNELPROPERTIES + " (cp_channel BIGINT PRIMARY KEY, cp_data INT NOT NULL);");
        }
    }

    public static void clearBit(DBConnection db, long channel, @ChannelBit int bit)
    {
        var data = db.getValueOr("SELECT cp_data FROM " + TABLE_CHANNELPROPERTIES + " WHERE cp_channel=?", "cp_data", Integer.class, 0, channel);

        data = data & ~(1 << bit - 1);

        db.simpleUpdate("INSERT INTO " + TABLE_CHANNELPROPERTIES + "(cp_channel, cp_data) VALUES (?, ?) ON DUPLICATE KEY UPDATE cp_data=?", channel, data, data);
    }

    public static boolean checkBit(DBConnection db, long channel, @ChannelBit int bit)
    {
        var val = db.getValueOr("SELECT cp_data FROM " + TABLE_CHANNELPROPERTIES + " WHERE cp_channel=?", "cp_data", Integer.class, 0, channel);

        return (val & 1 << bit - 1) > 0;
    }

    public static void setBit(DBConnection db, long channel, @ChannelBit int bit)
    {
        var data = db.getValueOr("SELECT cp_data FROM " + TABLE_CHANNELPROPERTIES + " WHERE cp_channel=?", "cp_data", Integer.class, 0, channel);

        data = data | 1 << bit - 1;

        db.simpleUpdate("INSERT INTO " + TABLE_CHANNELPROPERTIES + "(cp_channel, cp_data) VALUES (?, ?) ON DUPLICATE KEY UPDATE cp_data=?", channel, data, data);
    }
}
