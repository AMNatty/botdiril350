package cz.tefek.botdiril.userdata.items.scrolls;

import java.util.function.ObjLongConsumer;

import cz.tefek.botdiril.framework.command.CommandContext;
import cz.tefek.botdiril.userdata.item.IOpenable;
import cz.tefek.botdiril.userdata.item.Item;

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
