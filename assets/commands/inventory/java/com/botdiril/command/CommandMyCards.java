package com.botdiril.command;

import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.card.CardPair;
import com.botdiril.userdata.card.UserCards;

import java.util.Comparator;

@Command("collection")
public class CommandMyCards
{
    private static final long CARDS_PER_PAGE = 18;

    private static final Comparator<CardPair> cardComparator = (i1, i2) -> Long.compare(Card.getPrice(i2.getCard(), i2.getLevel()), Card.getPrice(i1.getCard(), i1.getLevel()));

    @CmdInvoke
    public static void show(CommandContext co)
    {
        show(co, co.player);
    }

    @CmdInvoke
    public static void show(CommandContext co, @CmdPar("player") EntityPlayer player)
    {
        show(co, player, 1);
    }

    @CmdInvoke
    public static void show(CommandContext co, @CmdPar("player") EntityPlayer player, @CmdPar("page") long page)
    {
        CommandAssert.numberNotBelowL(page, 1, "Invalid page.");

        var cps = UserCards.getCards(co.db, player);

        if (cps.isEmpty())
        {
            co.respond("The card collection is empty.");
            return;
        }

        var eb = new ResponseEmbed();

        eb.setTitle("This player has %d different cards.".formatted(cps.size()));
        eb.setDescription(player.getMention() + "'s card collection.");
        eb.setColor(0x008080);
        eb.setThumbnail(player.getAvatarURL());
        var isc = cps.stream();

        var pageCount = 1 + (cps.size() - 1) / CARDS_PER_PAGE;

        if (page > pageCount)
        {
            page = pageCount;
        }

        eb.appendDescription(String.format("\nPage %d/%d", page, pageCount));

        isc.sorted(cardComparator)
            .skip((page - 1) * CARDS_PER_PAGE).limit(CARDS_PER_PAGE)
            .forEach(ip ->  eb.addField(ip.getCard().getInlineDescription(), String.format("Count: **%d**\nID: **%s**", ip.getAmount(), ip.getCard().getName()), true));

        if (co instanceof ChatCommandContext ccc)
            eb.setFooter("Use `%s%s %s <page>` to go to another page.".formatted(ccc.usedPrefix, ccc.usedAlias, ccc.player.getMention()), null);

        co.respond(eb);
    }
}
