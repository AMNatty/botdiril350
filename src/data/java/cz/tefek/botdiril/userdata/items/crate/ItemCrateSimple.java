package cz.tefek.botdiril.userdata.items.crate;

import java.util.function.BiConsumer;

import cz.tefek.botdiril.framework.command.CommandContext;
import cz.tefek.botdiril.userdata.item.ItemDrops;

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
