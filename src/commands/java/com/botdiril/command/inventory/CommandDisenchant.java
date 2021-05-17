package com.botdiril.command.inventory;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.IIdentifiable;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ShopEntries;
import com.botdiril.util.BotdirilFmt;

@Command("disenchant")
public class CommandDisenchant
{
    @CmdInvoke
    public static void dust(CommandContext co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item)
    {
        dust(co, item, 1);
    }

    @CmdInvoke
    public static void dust(CommandContext co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item, @CmdPar(value = "amount", type = ParType.AMOUNT_ITEM_OR_CARD) long amount)
    {
        CommandAssert.numberMoreThanZeroL(amount, "You can't disenchant zero items / cards.");

        if (!ShopEntries.canBeDisenchanted(item))
            throw new CommandException("That item cannot be sold.");

        if (item instanceof Item iitem)
        {
            CommandAssert.numberNotAboveL(amount, co.inventory.howManyOf(iitem), "You don't have that many items of that type.");
            co.inventory.addItem(iitem, -amount);
        }
        else if (item instanceof Card card)
        {
            CommandAssert.numberNotAboveL(amount, co.inventory.howManyOf(card) - 1, "You don't have that many cards of that type. Keep in mind you need to keep at least one card of each type once you receive it.");
            co.inventory.addCard(card, -amount);
        }

        var value = amount * ShopEntries.getDustForDisenchanting(item);
        co.inventory.addDust(value);

        co.respondf("You disenchanted %s for %s.", BotdirilFmt.amountOfMD(amount, item), BotdirilFmt.amountOfMD(value, Icons.DUST));
    }
}
