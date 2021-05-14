package com.botdiril.userdata.items.scrolls;

import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.userdata.item.IOpenable;
import com.botdiril.userdata.item.Item;

import java.util.function.ObjLongConsumer;

public class ItemScroll extends Item implements IOpenable
{
    private final ObjLongConsumer<CommandContext> openHandler;

    public ItemScroll(String name, String icon, String localizedName, ObjLongConsumer<CommandContext> openHandler, String description)
    {
        super(name, icon, localizedName, description);

        this.openHandler = openHandler;
    }

    @Override
    public void open(CommandContext co, long amount)
    {
        openHandler.accept(co, amount);
    }
}
