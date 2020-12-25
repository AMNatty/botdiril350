package cz.tefek.botdiril.userdata.items.crate;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.item.ItemDrops;
import cz.tefek.botdiril.userdata.pools.PoolDrawer;

public class ItemCrateGeneric extends ItemCrateSimple
{
    public ItemCrateGeneric(String name, String icon, String localizedName, int contents, PoolDrawer<Item> lootPool, String description)
    {
        super(name, icon, localizedName, (co, id) -> id.addFromPool(lootPool, contents), description);
    }

    public ItemCrateGeneric(String name, String icon, String localizedName, int contents, PoolDrawer<Item> lootPool, Consumer<CallObj> openHandler, String description)
    {
        super(name, icon, localizedName, (co, id) -> {
            openHandler.accept(co);
            id.addFromPool(lootPool, contents);
        }, description);
    }

    public ItemCrateGeneric(String name, String icon, String localizedName, int contents, PoolDrawer<Item> lootPool, BiConsumer<CallObj, ItemDrops> openHandler, String description)
    {
        super(name, icon, localizedName, (co, id) -> {
            openHandler.accept(co, id);
            id.addFromPool(lootPool, contents);
        }, description);
    }
}
