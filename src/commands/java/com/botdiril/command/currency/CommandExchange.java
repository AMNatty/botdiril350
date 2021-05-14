package com.botdiril.command.currency;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
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

@Command(value = "exchange", aliases = {
        "buyfortokens" }, category = CommandCategory.CURRENCY, description = "Exchange tokens for items or cards.")
public class CommandExchange
{
    @CmdInvoke
    public static void buy(CommandContext co, @CmdPar(value = "item", type = ParType.ITEM_OR_CARD) Item item)
    {
        if (!ShopEntries.canBeBoughtForTokens(item))
            throw new CommandException("That item cannot be bought, sorry.");

        if (ShopEntries.getTokenPrice(item) > co.inventory.getKekTokens())
            throw new CommandException("You can't afford that item, sorry.");

        buy(co, item, 1);
    }

    @CmdInvoke
    public static void buy(CommandContext co, @CmdPar(value = "item") Item item, @CmdPar(value = "amount", type = ParType.AMOUNT_ITEM_BUY_TOKENS) long amount)
    {
        CommandAssert.numberMoreThanZeroL(amount, "You can't buy zero items.");

        var price = amount * ShopEntries.getTokenPrice(item);

        co.inventory.addItem(item, amount);

        co.inventory.addKekTokens(-price);

        co.respondf("""
        You bought %s for %s.
        """,
            BotdirilFmt.amountOfMD(amount, item.getIcon()),
            BotdirilFmt.amountOfMD(price, Icons.TOKEN));
    }
}
