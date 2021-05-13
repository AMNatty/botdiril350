package com.botdiril.command.inventory;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.util.CommandAssert;
import net.dv8tion.jda.api.EmbedBuilder;
import org.apache.commons.text.similarity.FuzzyScore;

import java.util.Comparator;
import java.util.Locale;

import com.botdiril.userdata.item.Item;

@Command(value = "itemlist", aliases = { "il" }, category = CommandCategory.ITEMS, description = "Shows a browsable list of items")
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
        var items = Item.items();

        var itemsPerPage = 21L;

        var pages = items.size() / itemsPerPage + 1;

        CommandAssert.numberInBoundsInclusiveL(page, 1, pages, String.format("Select a page in the range 1..%d", pages));

        var eb = new EmbedBuilder();

        eb.setTitle("Item list");
        eb.setColor(0x008080);
        eb.setDescription(String.format("**Page %d/%d**\nUse `%s%s <page>` to browse.", page, pages, co.usedPrefix, co.usedAlias));

        items.stream().skip(itemsPerPage * (page - 1)).limit(itemsPerPage).forEach(it ->
            eb.addField(it.inlineDescription(), "**ID: **" + it.getName(), true));

        eb.setFooter("Use `" + co.usedPrefix + "iteminfo <item id>` to show more information about an item.", null);

        co.respond(eb);
    }

    @CmdInvoke
    public static void showPage(CommandContext co, @CmdPar("search query") String search, @CmdPar("page") int page)
    {
        CommandAssert.stringNotTooLong(search, 50, "The search string can't be this long.");

        var items = Item.items();

        var itemsPerPage = 21L;

        var pages = items.size() / itemsPerPage + 1;

        CommandAssert.numberInBoundsInclusiveL(page, 1, pages, String.format("Select a page in the range 1..%d", pages));

        var eb = new EmbedBuilder();

        eb.setTitle("Item list");
        eb.setColor(0x008080);
        eb.setDescription(String.format("**Page %d/%d**\nSearch results for `%s`.\nUse `%s%s <search> <page>` to browse.", page, pages, search, co.usedPrefix, co.usedAlias));

        var fc = new FuzzyScore(Locale.getDefault());

        Comparator<? super Item> itemcp = Comparator.comparing((Item it) -> fc.fuzzyScore(it.getName(), search)).reversed();

        items.stream().sorted(itemcp).skip(itemsPerPage * (page - 1)).limit(itemsPerPage).forEach(it ->
            eb.addField(it.inlineDescription(), "**ID: **" + it.getName(), true));

        eb.setFooter("Use `" + co.usedPrefix + "iteminfo <item id>` to show more information about an item.", null);

        co.respond(eb);
    }
}
