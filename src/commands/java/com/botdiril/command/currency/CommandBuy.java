package com.botdiril.command.currency;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ShopEntries;
import com.botdiril.util.BotdirilFmt;

@Command("buy")
public class CommandBuy
{
    @CmdInvoke
    public static void buy(CommandContext co, @CmdPar(value = "item") Item item)
    {
        if (!ShopEntries.canBeBought(item))
            throw new CommandException("That item cannot be bought, sorry.");

        if (ShopEntries.getCoinPrice(item) > co.inventory.getCoins())
            throw new CommandException("You can't afford that item, sorry.");

        buy(co, item, 1);
    }

    @CmdInvoke
    public static void buy(CommandContext co, @CmdPar(value = "item") Item item, @CmdPar(value = "amount", type = ParType.AMOUNT_ITEM_BUY_COINS) long amount)
    {
        CommandAssert.numberMoreThanZeroL(amount, "You can't buy zero items / cards.");

        var price = amount * ShopEntries.getCoinPrice(item);

        co.inventory.addItem(item, amount);

        co.inventory.addCoins(-price);

        co.respondf("You bought %s for %s.", BotdirilFmt.amountOfMD(amount, item), BotdirilFmt.amountOfMD(price, Icons.COIN));
    }
}
