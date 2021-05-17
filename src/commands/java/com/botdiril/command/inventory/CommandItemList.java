package com.botdiril.command.inventory;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.item.Item;
import org.apache.commons.text.similarity.FuzzyScore;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Locale;

@Command("itemlist")
public class CommandItemList
{

    @CmdInvoke
    public static void showFirstPage(CommandContext co)
    {
        showPage(co, 1);
    }

    @CmdInvoke
    public static void showPage(CommandContext co, @CmdPar("page") int page)
    {
        showSorted(co, null, Comparator.comparing(Item::getLocalizedName), page);
    }

    @CmdInvoke
    public static void showPage(CommandContext co, @CmdPar("search query") String search, @CmdPar("page") int page)
    {
        CommandAssert.stringNotTooLong(search, 50, "The search string can't be this long.");

        var fc = new FuzzyScore(Locale.getDefault());

        Comparator<? super Item> itemcp = Comparator.comparing((Item it) -> fc.fuzzyScore(it.getName(), search)).reversed();

        showSorted(co, search, itemcp, page);
    }

    private static void showSorted(CommandContext co, @Nullable String search, Comparator<? super Item> itemSorting, int page)
    {
        var items = Item.items();

        var itemsPerPage = 21L;

        var pages = items.size() / itemsPerPage + 1;

        CommandAssert.numberInBoundsInclusiveL(page, 1, pages, String.format("Select a page in the range 1..%d", pages));

        var eb = new ResponseEmbed();

        eb.setTitle("Item list");
        eb.setColor(0x008080);

        eb.setDescription("""
        **Page %d/%d**
        """.formatted(page, pages));

        if (search != null)
            eb.appendDescription("Search results for `%s`.".formatted(search));

        if (co instanceof ChatCommandContext ccc)
        {
            eb.appendDescription("Use `%s%s <search> <page>` to browse.".formatted(ccc.usedPrefix, ccc.usedAlias));

            eb.setFooter("Use `%siteminfo <card id>` to show more information about a card.".formatted(ccc.usedPrefix), null);
        }

        items.stream().sorted(itemSorting).skip(itemsPerPage * (page - 1)).limit(itemsPerPage).forEach(it ->
            eb.addField(it.inlineDescription(), "**ID: **" + it.getName(), true));


        co.respond(eb);
    }
}
