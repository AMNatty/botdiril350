package com.botdiril.userdata.items.crate;

import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ItemDrops;
import com.botdiril.userdata.pools.PoolDrawer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ItemCrateGeneric extends ItemCrateSimple
{
    public ItemCrateGeneric(String name, String icon, String localizedName, int contents, PoolDrawer<Item> lootPool, String description)
    {
        super(name, icon, localizedName, (co, id) -> id.addFromPool(lootPool, contents), description);
    }

    public ItemCrateGeneric(String name, String icon, String localizedName, int contents, PoolDrawer<Item> lootPool, Consumer<CommandContext> openHandler, String description)
    {
        super(name, icon, localizedName, (co, id) -> {
            openHandler.accept(co);
            id.addFromPool(lootPool, contents);
        }, description);
    }

    public ItemCrateGeneric(String name, String icon, String localizedName, int contents, PoolDrawer<Item> lootPool, BiConsumer<CommandContext, ItemDrops> openHandler, String description)
    {
        super(name, icon, localizedName, (co, id) -> {
            openHandler.accept(co, id);
            id.addFromPool(lootPool, contents);
        }, description);
    }
}
