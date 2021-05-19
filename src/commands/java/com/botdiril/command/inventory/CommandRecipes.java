package com.botdiril.command.inventory;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.item.*;
import com.botdiril.util.BotdirilFmt;

import java.util.Comparator;
import java.util.stream.Collectors;

@Command("recipes")
public class CommandRecipes
{
    private static final int ITEMS_PER_PAGE = 12;
    private static final Comparator<Recipe> recipeSorter = (i1, i2) ->
    {
        if (i1.result() instanceof Card)
        {
            return Integer.MAX_VALUE;
        }

        if (i2.result() instanceof Card)
        {
            return Integer.MIN_VALUE;
        }

        if (!ShopEntries.canBeBought((Item) i2.result()) && !ShopEntries.canBeBought((Item) i1.result()))
        {
            return Integer.MIN_VALUE + 1;
        }

        if (!ShopEntries.canBeBought((Item) i2.result()))
        {
            return Integer.MIN_VALUE + 1;
        }

        if (!ShopEntries.canBeBought((Item) i1.result()))
        {
            return Integer.MAX_VALUE - 1;
        }

        return Long.compare(ShopEntries.getCoinPrice(i2.result()), ShopEntries.getCoinPrice(i1.result()));
    };

    @CmdInvoke
    public static void show(CommandContext co)
    {
        show(co, 1);
    }

    @CmdInvoke
    public static void show(CommandContext co, @CmdPar("page") int page)
    {
        CommandAssert.numberNotBelowL(page, 1, "Invalid page.");

        var recipes = CraftingEntries.getRecipes();

        var eb = new ResponseEmbed();

        eb.setTitle("Total " + BotdirilFmt.format(recipes.size()) + " recipes.");
        eb.setDescription("Showing " + ITEMS_PER_PAGE + " recipes per page.");
        eb.setColor(0x008080);

        var isc = recipes.stream();

        var pageCount = 1 + (recipes.size() - 1) / ITEMS_PER_PAGE;

        if (page > pageCount)
        {
            page = pageCount;
        }

        eb.appendDescription(String.format("\nPage %d/%d", page, pageCount));

        isc.sorted(recipeSorter).skip((long) (page - 1) * ITEMS_PER_PAGE).limit(ITEMS_PER_PAGE).forEach(recipe ->
        {
            var components = recipe.components();
            var recipeParts = components.stream().map(ItemPair::toString).collect(Collectors.joining(" + "));
            eb.addField(recipe.result().inlineDescription(), "**%s**".formatted(recipeParts), false);
        });

        if (co instanceof ChatCommandContext ccc)
            eb.setFooter("Use `%s%s %s <page>` to go to another page.".formatted(ccc.usedPrefix, ccc.usedAlias, ccc.player.getMention()), null);

        co.respond(eb);
    }
}
