package com.botdiril.command.inventory;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.item.ShopEntries;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Comparator;
import java.util.stream.Collectors;

import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.item.CraftingEntries;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.Recipe;
import com.botdiril.util.BotdirilFmt;

@Command(value = "recipes", aliases = { "recipelist",
        "rl" }, category = CommandCategory.ITEMS, description = "Shows all craftable items including their recipes.")
public class CommandRecipes
{
    private static final int ITEMS_PER_PAGE = 12;
    private static final Comparator<Recipe> recipeSorter = (i1, i2) ->
    {
        if (i1.getResult() instanceof Card)
        {
            return Integer.MAX_VALUE;
        }

        if (i2.getResult() instanceof Card)
        {
            return Integer.MIN_VALUE;
        }

        if (!ShopEntries.canBeBought((Item) i2.getResult()) && !ShopEntries.canBeBought((Item) i1.getResult()))
        {
            return Integer.MIN_VALUE + 1;
        }

        if (!ShopEntries.canBeBought((Item) i2.getResult()))
        {
            return Integer.MIN_VALUE + 1;
        }

        if (!ShopEntries.canBeBought((Item) i1.getResult()))
        {
            return Integer.MAX_VALUE - 1;
        }

        return Long.compare(ShopEntries.getCoinPrice(i2.getResult()), ShopEntries.getCoinPrice(i1.getResult()));
    };

    @CmdInvoke
    public static void show(CommandContext co)
    {
        var recipes = CraftingEntries.getRecipes();

        var eb = new EmbedBuilder();

        eb.setTitle("Total " + BotdirilFmt.format(recipes.size()) + " recipes.");
        eb.setDescription("Showing " + ITEMS_PER_PAGE + " recipes per page.");
        eb.setColor(0x008080);

        var isc = recipes.stream();

        var pages = 1 + (recipes.size() - 1) / ITEMS_PER_PAGE;

        eb.appendDescription("\nPage 1/" + pages);

        // Sort by value
        isc.sorted(recipeSorter).limit(ITEMS_PER_PAGE).forEach(recipe ->
        {
            var components = recipe.getComponents();
            var recipeParts = components.stream().map(comp -> String.format("**%s %s**", BotdirilFmt.format(comp.getAmount()), comp.getItem().inlineDescription())).collect(Collectors.joining(" + "));
            eb.addField(recipe.getResult().inlineDescription(), recipeParts, false);
        });

        eb.setFooter("Use `" + co.usedPrefix + "recipes <page>` to go to another page.", null);

        co.respond(eb);
    }

    @CmdInvoke
    public static void show(CommandContext co, @CmdPar("page") int page)
    {
        CommandAssert.numberNotBelowL(page, 1, "Invalid page.");

        var recipes = CraftingEntries.getRecipes();

        var eb = new EmbedBuilder();

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
            var components = recipe.getComponents();
            var recipeParts = components.stream().map(comp -> String.format("**%s %s**", BotdirilFmt.format(comp.getAmount()), comp.getItem().inlineDescription())).collect(Collectors.joining(" + "));
            eb.addField(recipe.getResult().inlineDescription(), recipeParts, false);
        });

        eb.setFooter("Use `" + co.usedPrefix + "recipes <page>` to go to another page.", null);

        co.respond(eb);
    }
}
