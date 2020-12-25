package cz.tefek.botdiril.userdata.items.scrolls;

import java.util.function.ObjLongConsumer;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.item.IOpenable;
import cz.tefek.botdiril.userdata.item.Item;

public class ItemScroll extends Item implements IOpenable
{
    private final ObjLongConsumer<CallObj> openHandler;

    public ItemScroll(String name, String icon, String localizedName, ObjLongConsumer<CallObj> openHandler, String description)
    {
        super(name, icon, localizedName, description);

        this.openHandler = openHandler;
    }

    @Override
    public void open(CallObj co, long amount)
    {
        openHandler.accept(co, amount);
    }
}
