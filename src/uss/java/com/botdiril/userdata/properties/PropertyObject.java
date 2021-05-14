package com.botdiril.userdata.properties;

import com.botdiril.MajorFailureException;
import com.botdiril.command.general.CommandAlias;
import com.botdiril.framework.sql.DBConnection;
import com.botdiril.framework.sql.SqlFoundation;
import com.botdiril.userdata.InventoryTables;
import com.botdiril.userdata.stat.EnumStat;
import com.botdiril.userdata.tempstat.EnumBlessing;
import com.botdiril.userdata.tempstat.EnumCurse;
import com.botdiril.uss2.properties.USS2PropertyObject;
import com.botdiril.uss2.properties.USS2PropertySchema;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.ByteBuffer;

public final class PropertyObject
{
    public static final String TABLE_USS2 = "uss2";
    public static final int USS_SIZE = 4096;

    public static final byte PO_VERSION_0 = 0;
    public static final byte PO_VERSION_1 = 1;

    private static USS2PropertySchema.USS2Byte ALIAS_USED;
    private static USS2PropertySchema.USS2String[] ALIAS_IN;
    private static USS2PropertySchema.USS2String[] ALIAS_OUT;
    private static USS2PropertySchema.USS2Long PREFERENCES_BITFIELD;

    private static USS2PropertySchema.USS2Long[] BLESSINGS;
    private static USS2PropertySchema.USS2Long[] CURSES;
    private static USS2PropertySchema.USS2Long[] STATS;

    private static USS2PropertySchema.USS2Long JACKPOT;
    private static USS2PropertySchema.USS2Long JACKPOT_STORED;

    private static USS2PropertySchema schema;

    public static void init(DBConnection db)
    {
        schema = new USS2PropertySchema(USS_SIZE)
        {{
            ALIAS_USED = this.declareByte(PO_VERSION_0);

            ALIAS_IN = new USS2String[Byte.SIZE];
            for (int i = 0; i < ALIAS_IN.length; i++)
                ALIAS_IN[i] = this.declareString(PO_VERSION_0, (byte) (Character.BYTES * CommandAlias.ALIAS_IN_MAX_LENGTH));

            ALIAS_OUT = new USS2String[Byte.SIZE];
            for (int i = 0; i < ALIAS_OUT.length; i++)
                ALIAS_OUT[i] = this.declareString(PO_VERSION_0, (byte) (Character.BYTES * CommandAlias.ALIAS_OUT_MAX_LENGTH));

            PREFERENCES_BITFIELD = this.declareLong(PO_VERSION_0);

            BLESSINGS = new USS2Long[EnumBlessing.MAX_BLESSINGS];
            for (int i = 0; i < BLESSINGS.length; i++)
                BLESSINGS[i] = this.declareLong(PO_VERSION_0);

            CURSES = new USS2Long[EnumCurse.MAX_CURSES];
            for (int i = 0; i < CURSES.length; i++)
                CURSES[i] = this.declareLong(PO_VERSION_0);

            STATS = new USS2Long[EnumStat.MAX_STATS];
            for (int i = 0; i < STATS.length; i++)
                STATS[i] = this.declareLong(PO_VERSION_0);

            JACKPOT = this.declareLong(PO_VERSION_1);
            JACKPOT_STORED = this.declareLong(PO_VERSION_1);
        }};

        schema.printInfo();

        try
        {
            if (!SqlFoundation.checkTableExists(db, TABLE_USS2))
            {
                db.simpleExecute("CREATE TABLE " + TABLE_USS2 + " (" +
                                 "uss_us_id INT NOT NULL PRIMARY KEY, " +
                                 "uss_data BLOB(" + USS_SIZE + "), " +
                                 "FOREIGN KEY (uss_us_id) REFERENCES " + InventoryTables.TABLE_USER + "(us_id)" +
                                 ")");
            }
        }
        catch (Exception e)
        {
            throw new MajorFailureException("Failed to init the property object table!", e);
        }
    }

    private final int fid;
    private final DBConnection db;
    private boolean newObj;

    public PropertyObject(DBConnection db, int fid)
    {
        this.fid = fid;
        this.db = db;
    }

    private USS2PropertyObject load()
    {
        var data = db.getValue("SELECT uss_data FROM " + TABLE_USS2 + " WHERE uss_us_id=?", "uss_data", byte[].class, this.fid);
        this.newObj = data.isEmpty();
        return this.newObj ? USS2PropertyObject.create(schema) : USS2PropertyObject.from(ByteBuffer.wrap(data.get()), schema);
    }

    public int getFID()
    {
        return this.fid;
    }

    public byte getUsedAliases()
    {
        var uss = this.load();
        return ALIAS_USED.read(uss);
    }

    public void setUsedAliases(byte aliasList)
    {
        var uss = this.load();
        ALIAS_USED.write(uss, aliasList);
        this.save(uss);
    }

    public Pair<String, String> getAlias(int aliasNum)
    {
        var uss = this.load();
        return Pair.of(ALIAS_IN[aliasNum].read(uss), ALIAS_OUT[aliasNum].read(uss));
    }

    public void setAlias(int aliasNum, String in, String out)
    {
        var uss = this.load();
        ALIAS_IN[aliasNum].write(uss, in);
        ALIAS_OUT[aliasNum].write(uss, out);
        this.save(uss);
    }

    public long getPreferencesBitfield()
    {
        var uss = this.load();
        return PREFERENCES_BITFIELD.read(uss);
    }

    public void setPreferencesBitfield(long preferences)
    {
        var uss = this.load();
        PREFERENCES_BITFIELD.write(uss, preferences);
        this.save(uss);
    }

    public long getBlessing(EnumBlessing blessing)
    {
        var uss = this.load();
        return BLESSINGS[blessing.getID()].read(uss);
    }

    public long getCurse(EnumCurse curse)
    {
        var uss = this.load();
        return CURSES[curse.getID()].read(uss);
    }

    public void setBlessing(EnumBlessing blessing, long time)
    {
        var uss = this.load();
        BLESSINGS[blessing.getID()].write(uss, time);
        this.save(uss);
    }

    public void setCurse(EnumCurse curse, long time)
    {
        var uss = this.load();
        CURSES[curse.getID()].write(uss, time);
        this.save(uss);
    }

    public void extendBlessing(EnumBlessing blessing, long time)
    {
        var uss = this.load();
        var id = blessing.getID();
        var bless = BLESSINGS[id].read(uss);
        BLESSINGS[id].write(uss, bless + time);
        this.save(uss);
    }

    public void extendCurse(EnumCurse curse, long time)
    {
        var uss = this.load();
        var id = curse.getID();
        var bless = CURSES[id].read(uss);
        CURSES[id].write(uss, bless + time);
        this.save(uss);
    }

    public long getStat(EnumStat stat)
    {
        var uss = this.load();
        return STATS[stat.getID()].read(uss);
    }

    public void setStat(EnumStat stat, long value)
    {
        var uss = this.load();
        STATS[stat.getID()].write(uss, value);
        this.save(uss);
    }

    public void addStat(EnumStat stat, long valueToAdd)
    {
        var uss = this.load();
        var id = stat.getID();
        var statValue = STATS[id].read(uss);
        STATS[id].write(uss, statValue + valueToAdd);
        this.save(uss);
    }

    public void incrementStat(EnumStat stat)
    {
        this.addStat(stat, 1);
    }

    public void setJackpot(long pool, long stored)
    {
        var uss = this.load();
        JACKPOT.write(uss, pool);
        JACKPOT_STORED.write(uss, stored);
        this.save(uss);
    }

    public long getJackpot()
    {
        var uss = this.load();
        return JACKPOT.read(uss);
    }

    public long getJackpotStored()
    {
        var uss = this.load();
        return JACKPOT_STORED.read(uss);
    }

    private void save(USS2PropertyObject uss)
    {
        if (this.newObj)
        {
            db.simpleUpdate("INSERT INTO " + TABLE_USS2 + "(uss_us_id, uss_data) VALUES (?, ?)", fid, uss.getDataByteArray());
        }
        else if (uss.isDirty())
        {
            db.simpleUpdate("UPDATE " + TABLE_USS2 + " SET uss_data=? WHERE uss_us_id=?", uss.getDataByteArray(), fid);
        }
    }
}
