package com.botdiril.command.superuser;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.IIdentifiable;
import com.botdiril.userdata.item.CraftingEntries;
import com.botdiril.userdata.item.ItemDrops;
import com.botdiril.userdata.item.ItemPair;
import com.botdiril.userdata.item.Recipe;

import java.util.stream.Collectors;

@Command(value = "craftingcost", aliases = {
    "craftcost", "craftingtotal", "crafttotal"
}, category = CommandCategory.ITEMS, description = "Shows total components needed to craft an item.")
public class CommandCraftingCost
{
    private static void addComponents(ItemDrops id, Recipe recipe, long count)
    {
        for (var itemPair : recipe.getComponents())
        {
            var item = itemPair.getItem();
            var subRecipe = CraftingEntries.search(item);

            if (subRecipe != null)
                addComponents(id, subRecipe, itemPair.getAmount() * count);
            else
                id.addItem(item, itemPair.getAmount() * count);
        }
    }

    @CmdInvoke
    public static void craft(CommandContext co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item)
    {
        craft(co, item, 1);
    }

    @CmdInvoke
    public static void craft(CommandContext co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item, @CmdPar("count") long amount)
    {
        CommandAssert.numberInBoundsExclusiveL(amount, 0, Integer.MAX_VALUE, "Please select a number between 1 and Int32 max.");

        var recipe = CraftingEntries.search(item);

        CommandAssert.assertTrue(recipe != null, "That item cannot be crafted.");

        var totalDrops = new ItemDrops();

        addComponents(totalDrops, recipe, amount);

        var needed = totalDrops.stream().map(ItemPair::toString).collect(Collectors.joining(", "));
        co.respondf("It would cost **%s** to craft **%d %s**.", needed, amount, item.inlineDescription());
    }
}
