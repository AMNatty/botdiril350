package com.botdiril.userdata.items.cardpack;

import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.card.CardDrops;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.item.IOpenable;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ShopEntries;
import com.botdiril.userdata.pools.PoolDrawer;
import com.botdiril.userdata.stat.EnumStat;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.userdata.tempstat.EnumCurse;
import com.botdiril.util.BotdirilFmt;

public abstract class ItemCardPack extends Item implements IOpenable
{
    private static final int DISPLAY_LIMIT = 15;

    public ItemCardPack(String name, String icon, String localizedName, String description)
    {
        super(name, icon, localizedName, description);
    }

    public abstract PoolDrawer<Card> getPool(CommandContext co);

    public abstract int getNumberOfCards(CommandContext co);

    protected void onOpen(CommandContext co, long amount)
    {

    }

    @Override
    public void open(CommandContext co, long amount)
    {
        var fm = String.format("You open **%d %s** and get the following cards:", amount, this.getInlineDescription());
        var sb = new StringBuilder();

        sb.append(fm);

        var cp = new CardDrops();

        var contents = this.getNumberOfCards(co);
        var pool = this.getPool(co);

        for (int i = 0; i < contents * amount; i++)
        {
            if (Curser.isCursed(co, EnumCurse.CURSE_OF_YASUO))
            {
                cp.addItem(Card.getCardByName("yasuo"));
                continue;
            }

            cp.addItem(pool.draw(), 1);
        }

        var i = 0;

        for (var cardPair : cp)
        {
            var card = cardPair.getCard();
            var amt = cardPair.getAmount();

            co.inventory.addCard(card, amt);

            if (i <= DISPLAY_LIMIT)
                sb.append(String.format("\n%dx %s", amt, card.getInlineDescription()));

            i++;
        }

        this.onOpen(co, amount);

        var dc = cp.distintCount();

        if (dc > DISPLAY_LIMIT)
        {
            sb.append(String.format("\nand %d more different cards...", dc - DISPLAY_LIMIT));
        }

        var dustVal = cp.stream().mapToLong(cardPair -> ShopEntries.getDustForDisenchanting(cardPair.getCard()) * cardPair.getAmount()).sum();

        sb.append(String.format("\nTotal %s cards. Approximate value: %s%s", BotdirilFmt.format(cp.totalCount()), BotdirilFmt.format(dustVal), Icons.DUST));

        co.userProperties.addStat(EnumStat.CARD_PACKS_OPENED, amount);

        co.respond(sb.toString());
    }

    @Override
    public String getFootnote(CommandContext co)
    {
        if (co instanceof ChatCommandContext ccc)
            return "Open using `%sopen %s`. Guaranteed to contain at least %d cards.".formatted(ccc.usedPrefix, this.getName(), this.getNumberOfCards(co));
        else
            return "Guaranteed to contain at least %d cards.".formatted(this.getNumberOfCards(co));
    }
}
