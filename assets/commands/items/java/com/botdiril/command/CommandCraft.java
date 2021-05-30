package com.botdiril.command;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.IGameObject;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.item.CraftingEntries;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ItemPair;
import com.botdiril.userdata.items.scrolls.Scrolls;
import com.botdiril.userdata.stat.EnumStat;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.userdata.tempstat.EnumBlessing;
import com.botdiril.userdata.tempstat.EnumCurse;
import com.botdiril.util.BotdirilFmt;
import com.botdiril.util.BotdirilRnd;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Command("craft")
public class CommandCraft
{
    @CmdInvoke
    public static void craft(CommandContext co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IGameObject item)
    {
        craft(co, item, 1);
    }

    @CmdInvoke
    public static void craft(CommandContext co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IGameObject item, @CmdPar(value = "amount") long amount)
    {
        CommandAssert.numberMoreThanZeroL(amount, "You can't craft zero items / cards.");

        CommandAssert.numberNotAboveL(amount, Integer.MAX_VALUE, "That's way too many.");

        var recipe = CraftingEntries.search(item);

        CommandAssert.assertTrue(recipe != null, "That item cannot be crafted.");

        var components = recipe.components();

        var missing = new ArrayList<ItemPair>();

        for (var itemPair : components)
        {
            var it = itemPair.getItem();
            var itAmt = itemPair.getAmount() * amount;
            var has = co.inventory.howManyOf(it);

            if (has < itAmt)
            {
                missing.add(ItemPair.of(it, itAmt - has));
            }
        }

        if (!missing.isEmpty())
        {
            var missingStr = missing.stream().map(ItemPair::toString).collect(Collectors.joining(", "));
            throw new CommandException(String.format("You are missing **%s** for this crafting.", missingStr));
        }

        var craftingSurgeActivated = false;

        if (Curser.isBlessed(co, EnumBlessing.CRAFTING_SURGE) && BotdirilRnd.rollChance(co.rdg, 0.2))
        {
            craftingSurgeActivated = true;
        }
        else
        {
            components.forEach(c -> co.inventory.addItem(c.getItem(), -c.getAmount() * amount));
        }

        var ingr = components.stream().map(ip -> BotdirilFmt.amountOfMD(ip.getAmount() * amount, ip.getItem())).collect(Collectors.joining(", "));

        if (Curser.isCursed(co, EnumCurse.CRAFTING_MAY_FAIL) && co.rdg.nextUniform(0, 1) < 0.2)
        {
            if (craftingSurgeActivated)
            {
                co.respondf("*You failed horribly while crafting and lost nothing, because the **%s%s** also activated.*", Scrolls.scrollOfBlessing.getIcon(), EnumBlessing.CRAFTING_SURGE.getLocalizedName());
            }
            else
            {
                co.respondf("*You failed horribly while crafting and lost %s.*", ingr);
            }

            return;
        }

        var product = amount * recipe.amount();

        if (item instanceof Item iitem)
        {
            co.inventory.addItem(iitem, product);
        }
        else if (item instanceof Card card)
        {
            co.inventory.addCard(card, product);
        }

        co.userProperties.addStat(EnumStat.ITEMS_CRAFTED, product);

        if (craftingSurgeActivated)
        {
            co.respondf("You crafted %s out of **%s thin air**.", BotdirilFmt.amountOfMD(product, item.getInlineDescription()), Scrolls.scrollOfBlessing.getIcon());
        }
        else
        {
            co.respondf("You crafted %s from %s.", BotdirilFmt.amountOfMD(product, item.getInlineDescription()), ingr);
        }
    }
}
