package cz.tefek.botdiril.userdata.items.crate;

import java.util.function.BiConsumer;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.item.ItemDrops;

public class ItemCrateSimple extends ItemCrate
{
    private final BiConsumer<CallObj, ItemDrops> openHandler;

    public ItemCrateSimple(String name, String icon, String localizedName, BiConsumer<CallObj, ItemDrops> addDropsHandler, String description)
    {
        super(name, icon, localizedName, description);

        this.openHandler = addDropsHandler;
    }

    @Override
    protected void addDrops(CallObj co, ItemDrops id)
    {
        this.openHandler.accept(co, id);
    }
}
