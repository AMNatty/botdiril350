package cz.tefek.botdiril.command.currency;

import cz.tefek.botdiril.framework.command.CommandContext;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.framework.command.invoke.ParType;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.item.ShopEntries;
import cz.tefek.botdiril.util.BotdirilFmt;

@Command(value = "exchange", aliases = {
        "buyfortokens" }, category = CommandCategory.CURRENCY, description = "Exchange tokens for items or cards.", levelLock = 8)
public class CommandExchange
{
    @CmdInvoke
    public static void buy(CommandContext co, @CmdPar(value = "item", type = ParType.ITEM_OR_CARD) Item item)
    {
        if (!ShopEntries.canBeBoughtForTokens(item))
        {
            throw new CommandException("That item cannot be bought, sorry.");
        }

        if (ShopEntries.getTokenPrice(item) > co.ui.getKekTokens())
        {
            throw new CommandException("You can't afford that item, sorry.");
        }

        buy(co, item, 1);
    }

    @CmdInvoke
    public static void buy(CommandContext co, @CmdPar(value = "item") Item item, @CmdPar(value = "amount", type = ParType.AMOUNT_ITEM_BUY_TOKENS) long amount)
    {
        CommandAssert.numberMoreThanZeroL(amount, "You can't buy zero items.");

        var price = amount * ShopEntries.getTokenPrice(item);

        co.ui.addItem(item, amount);

        co.ui.addKekTokens(-price);

        co.respond(String.format("You bought **%s** %s for **%s** %s.", BotdirilFmt.format(amount), item.getIcon(), BotdirilFmt.format(price), Icons.TOKEN));
    }
}
