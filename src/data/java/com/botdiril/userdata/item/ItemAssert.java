package com.botdiril.userdata.item;

import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.userdata.UserInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ItemAssert
{
    public static void requireItem(UserInventory ui, Item item)
    {
        var has = ui.howManyOf(item);

        if (has < 1)
            throw new CommandException("*You don't have any **%s**.*".formatted(item));
    }

    public static void consumeItem(UserInventory ui, Item item)
    {
        var has = ui.howManyOf(item);

        if (has < 1)
            throw new CommandException("*You don't have any **%s**.*".formatted(item));

        ui.addItem(item, -1);
    }

    public static void consumeItems(UserInventory ui, String actionName, ItemPair itemPair)
    {
        var item = itemPair.getItem();
        var amount = itemPair.getAmount();
        var has = ui.howManyOf(itemPair.getItem());

        if (has < amount)
        {
            throw new CommandException("*You need at least **%s %s** to %s.*\n".formatted(amount, item.getInlineDescription(), actionName) +
                                       "You are missing **%s %s**.".formatted(amount - has, item.getInlineDescription()));
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
            .map(itemPair -> "**%s %s**".formatted(itemPair.getAmount(), itemPair.getItem().getInlineDescription()))
            .collect(Collectors.joining(", "));

        var joinedMissing = missingResources.stream()
            .map(itemPair -> "**%s %s**".formatted(itemPair.getAmount(), itemPair.getItem().getInlineDescription()))
            .collect(Collectors.joining(", "));

        throw new CommandException("*You need at least %s to %s.*\n".formatted(joinedRequired, actionName) +
                                   "You are missing %s.".formatted(joinedMissing));
    }
}
