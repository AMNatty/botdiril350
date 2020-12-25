package cz.tefek.botdiril.userdata.items.crate;

import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.pools.PoolDrawer;
import cz.tefek.botdiril.util.BotdirilRnd;

public class ItemKekCrate extends ItemCrateGeneric
{
    public ItemKekCrate(String name, String icon, String localizedName, int contents, PoolDrawer<Item> lootPool, long minKeks, long maxKeks, String description)
    {
        super(name, icon, localizedName, contents, lootPool, (co, id) -> id.addItem(Items.keks, BotdirilRnd.RDG.nextLong(minKeks, maxKeks)), description);
    }
}
