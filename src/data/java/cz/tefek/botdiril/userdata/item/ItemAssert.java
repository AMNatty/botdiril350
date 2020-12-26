package cz.tefek.botdiril.userdata.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.userdata.UserInventory;

public class ItemAssert
{
    public static void consumeItems(UserInventory ui, String actionName, ItemPair itemPair)
    {
        var item = itemPair.getItem();
        var amount = itemPair.getAmount();
        var has = ui.howManyOf(itemPair.getItem());

        if (has < amount)
        {
            throw new CommandException("*You need at least **%s %s** to %s.*\n".formatted(amount, item.inlineDescription(), actionName) +
                                       "You are missing **%s %s**.".formatted(amount - has, item.inlineDescription()));
        }

        ui.addItem(item, -amount);
    }

    public static void consumeItems(UserInventory ui, String actionName, ItemPair... itemPairs)
    {
        var missingResources = new ArrayList<ItemPair>();

        for (var itemPair : itemPairs)
        {
            var item = itemPair.getItem();
            var amount = itemPair.getAmount();
            var has = ui.howManyOf(itemPair.getItem());

            if (has < amount)
            {
                missingResources.add(ItemPair.of(item, amount - has));
                continue;
            }

            ui.addItem(item, -amount);
        }

        if (missingResources.isEmpty())
            return;

        var joinedRequired = Arrays.stream(itemPairs)
            .map(itemPair -> "**%s %s**".formatted(itemPair.getAmount(), itemPair.getItem().inlineDescription()))
            .collect(Collectors.joining(", "));

        var joinedMissing = missingResources.stream()
            .map(itemPair -> "**%s %s**".formatted(itemPair.getAmount(), itemPair.getItem().inlineDescription()))
            .collect(Collectors.joining(", "));

        throw new CommandException("*You need at least %s to %s.*\n".formatted(joinedRequired, actionName) +
                                   "You are missing %s.".formatted(joinedMissing));
    }
}
