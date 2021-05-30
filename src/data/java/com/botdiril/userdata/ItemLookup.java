package com.botdiril.userdata;

import com.botdiril.framework.sql.DBConnection;
import com.botdiril.framework.sql.SqlFoundation;
import com.botdiril.util.BotdirilLog;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Don't get confused by the name, it's for legacy reasons. This class can map
 * any {@link IGameObject}.
 */
public class ItemLookup
{
    public static final String TABLE_ITEMLOOKUP = "itemlookup";

    private static final int AVAILABLE_IDS = 1 << 17;
    private static final BiMap<String, Integer> mappings = HashBiMap.create();
    private static final String[] iarr = new String[AVAILABLE_IDS];

    private static final Map<String, Integer> newMappings = new HashMap<>();

    private static final Logger logger = LogManager.getLogger(ItemLookup.class);

    public static void prepare(DBConnection db)
    {
        ItemLookup.logger.info("Initializing item lookup.");

        var tabExistsIL = SqlFoundation.checkTableExists(db, TABLE_ITEMLOOKUP);

        if (!tabExistsIL)
        {
            db.simpleExecute("CREATE TABLE " + TABLE_ITEMLOOKUP + " ( " +
                             "il_id INT UNIQUE NOT NULL, " +
                             "il_itemname VARCHAR(64) NOT NULL);");
        } else
        {
            db.exec("SELECT * FROM " + TABLE_ITEMLOOKUP, stat ->
            {
                var rs = stat.executeQuery();

                while (rs.next())
                {
                    var itemname = rs.getString("il_itemname");
                    var itemid = rs.getInt("il_id");

                    mappings.put(itemname, itemid);
                    iarr[itemid] = itemname;
                }

                rs.close();

                return null;
            });
        }
    }

    public static String getName(int id)
    {
        return iarr[id];
    }

    public static int make(String name)
    {
        if (mappings.containsKey(name))
        {
            return mappings.get(name);
        }

        for (int i = 0; i < iarr.length; i++)
        {
            if (iarr[i] == null)
            {
                iarr[i] = name;
                mappings.put(name, i);

                newMappings.put(name, i);

                ItemLookup.logger.info(String.format("<RAID %05d> Item registered: %s", i, name));

                return i;
            }
        }

        throw new IllegalStateException("ItemLookup: I think we are out of space for IDs.");
    }

    public static void save(DBConnection db)
    {
        for (var entry : newMappings.entrySet())
        {
            db.exec("INSERT INTO " + TABLE_ITEMLOOKUP + " (il_id, il_itemname) VALUES (?, ?)", stat ->
            {
                stat.setInt(1, entry.getValue());
                stat.setString(2, entry.getKey());

                return stat.executeUpdate();
            });
        }
    }

    public static int get(INamedObject thing)
    {
        return get(thing.getName());
    }

    public static int get(String name)
    {
        if (name == null)
            throw new IllegalStateException("ItemLookup: Name is null!");

        try
        {
            return mappings.get(name);
        }
        catch (NullPointerException e)
        {
            BotdirilLog.logger.warn("Looks like the item wasn't in the lookup: " + name, e);
            return make(name);
        }
    }
}
