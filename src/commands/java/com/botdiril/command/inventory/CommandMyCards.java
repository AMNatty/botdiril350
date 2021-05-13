package com.botdiril.command.inventory;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.util.CommandAssert;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.util.Comparator;
import java.util.Locale;

import com.botdiril.userdata.UserInventory;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.card.CardPair;
import com.botdiril.userdata.card.UserCards;

@Command(value = "collection", aliases = { "mycards",
        "cardcollection", "cards" }, category = CommandCategory.ITEMS, description = "Displays your card collection.")
public class CommandMyCards
{
    private static final long CARDS_PER_PAGE = 18;

    private static final Comparator<CardPair> cardComparator = (i1, i2) -> Long.compare(Card.getPrice(i2.getCard(), i2.getLevel()), Card.getPrice(i1.getCard(), i1.getLevel()));

    @CmdInvoke
    public static void show(CommandContext co)
    {
        show(co, co.caller);
    }

    @CmdInvoke
    public static void show(CommandContext co, @CmdPar("user") User user)
    {
        show(co, user, 1);
    }

    @CmdInvoke
    public static void show(CommandContext co, @CmdPar("user") User user, @CmdPar("page") long page)
    {
        CommandAssert.numberNotBelowL(page, 1, "Invalid page.");

        var ui = new UserInventory(co.db, user.getIdLong());
        var cps = UserCards.getCards(co.db, ui.getFID());

        if (cps.isEmpty())
        {
            co.respond("The card collection is empty.");
            return;
        }

        var eb = new EmbedBuilder();

        eb.setTitle("This user has " + cps.size() + " different cards.");
        eb.setDescription(user.getAsMention() + "'s card collection.");
        eb.setColor(0x008080);
        eb.setThumbnail(user.getEffectiveAvatarUrl());
        var isc = cps.stream();

        var pageCount = 1 + (cps.size() - 1) / CARDS_PER_PAGE;

        if (page > pageCount)
        {
            page = pageCount;
        }

        eb.appendDescription(String.format("\nPage %d/%d", page, pageCount));

        isc.sorted(cardComparator)
            .skip((page - 1) * CARDS_PER_PAGE).limit(CARDS_PER_PAGE)
            .forEach(ip ->  eb.addField(ip.getCard().inlineDescription(), String.format("Count: **%d**\nID: **%s**", ip.getAmount(), ip.getCard().getName()), true));

        eb.setFooter(String.format(Locale.ROOT, "Use `%s%s %d <page>` to go to another page.", co.usedPrefix, co.usedAlias, user.getIdLong()), null);

        co.respond(eb);
    }
}
