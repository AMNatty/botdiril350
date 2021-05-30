package com.botdiril.userdata.items.crate;

import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.items.Items;
import com.botdiril.userdata.pools.PoolDrawer;

public class ItemKekCrate extends ItemCrateGeneric
{
    public ItemKekCrate(String name, String icon, String localizedName, int contents, PoolDrawer<Item> lootPool, long minKeks, long maxKeks, String description)
    {
        super(name, icon, localizedName, contents, lootPool, (co, id) -> id.addItem(Items.keks, co.rdg.nextLong(minKeks, maxKeks)), description);
    }
}
