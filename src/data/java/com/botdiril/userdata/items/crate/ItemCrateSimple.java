package com.botdiril.userdata.items.crate;

import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.userdata.item.ItemDrops;

import java.util.function.BiConsumer;

public class ItemCrateSimple extends ItemCrate
{
    private final BiConsumer<CommandContext, ItemDrops> openHandler;

    public ItemCrateSimple(String name, String icon, String localizedName, BiConsumer<CommandContext, ItemDrops> addDropsHandler, String description)
    {
        super(name, icon, localizedName, description);

        this.openHandler = addDropsHandler;
    }

    @Override
    protected void addDrops(CommandContext co, ItemDrops id)
    {
        this.openHandler.accept(co, id);
    }
}
