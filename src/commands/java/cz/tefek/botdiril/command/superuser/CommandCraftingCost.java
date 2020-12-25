package cz.tefek.botdiril.command.superuser;

import java.util.stream.Collectors;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.ParType;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.userdata.IIdentifiable;
import cz.tefek.botdiril.userdata.item.CraftingEntries;
import cz.tefek.botdiril.userdata.item.ItemDrops;
import cz.tefek.botdiril.userdata.item.Recipe;
import cz.tefek.botdiril.util.BotdirilFmt;

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
    public static void craft(CallObj co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item)
    {
        craft(co, item, 1);
    }

    @CmdInvoke
    public static void craft(CallObj co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item, @CmdPar("count") long amount)
    {
        CommandAssert.numberInBoundsExclusiveL(amount, 0, Integer.MAX_VALUE, "Please select a number between 1 and Int32 max.");

        var recipe = CraftingEntries.search(item);

        CommandAssert.assertTrue(recipe != null, "That item cannot be crafted.");

        var totalDrops = new ItemDrops();

        addComponents(totalDrops, recipe, amount);

        var needed = totalDrops.stream().map(ip -> String.format("**%s %s**", BotdirilFmt.format(ip.getAmount()), ip.getItem().inlineDescription())).collect(Collectors.joining(", "));
        co.respond("It would cost %s to craft **%d %s**.".formatted(needed, amount, item.inlineDescription()));
    }
}
