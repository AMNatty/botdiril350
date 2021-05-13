package com.botdiril.userdata.items.cardpack;

import com.botdiril.userdata.pools.PoolDrawer;

import java.util.function.ObjLongConsumer;

import com.botdiril.framework.command.CommandContext;
import com.botdiril.userdata.card.Card;

public class ItemCardPackSimple extends ItemCardPack
{
    protected final PoolDrawer<Card> pool;
    protected final int contents;
    protected final ObjLongConsumer<CommandContext> openHandler;

    public ItemCardPackSimple(String name, String icon, String localizedName, PoolDrawer<Card> pool, int contents, String description)
    {
        super(name, icon, localizedName, description);

        this.pool = pool;
        this.contents = contents;
        this.openHandler = (callObj, amount) -> {};
    }

    public ItemCardPackSimple(String name, String icon, String localizedName, PoolDrawer<Card> pool, int contents, ObjLongConsumer<CommandContext> openHandler, String description)
    {
        super(name, icon, localizedName, description);

        this.pool = pool;
        this.contents = contents;
        this.openHandler = openHandler;
    }

    @Override
    protected void onOpen(CommandContext co, long amount)
    {
        this.openHandler.accept(co, amount);
    }

    public PoolDrawer<Card> getPool(CommandContext co)
    {
        return this.pool;
    }

    public int getNumberOfCards(CommandContext co)
    {
        return this.contents;
    }
}
