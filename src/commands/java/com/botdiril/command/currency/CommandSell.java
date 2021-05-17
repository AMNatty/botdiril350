package com.botdiril.command.currency;

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
import com.botdiril.userdata.items.Items;
import com.botdiril.userdata.preferences.EnumUserPreference;
import com.botdiril.userdata.preferences.UserPreferences;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.userdata.tempstat.EnumBlessing;
import com.botdiril.userdata.tempstat.EnumCurse;
import com.botdiril.util.BotdirilFmt;
import com.botdiril.util.BotdirilRnd;

@Command("sell")
public class CommandSell
{
    public static final double CHANCE_TO_EXPLODE = 0.005;

    public static final long GOLDEDOIL_SELL_BOOST = 10;
    public static final long EXPLOSION_MODIFIER = 850;

    public static void sellRoutine(CommandContext co, String articleName, long amountOfArticle, long totalMoney)
    {
        long resellModifier = 1000;

        long goldenOils = UserPreferences.isBitEnabled(co.userProperties, EnumUserPreference.GOLDEN_OIL_DISABLED) ? 0
                : co.inventory.howManyOf(Items.goldenOil);

        boolean exploded = false;

        if (BotdirilRnd.rollChance(goldenOils * CHANCE_TO_EXPLODE))
        {
            exploded = true;
            resellModifier = EXPLOSION_MODIFIER;
            co.inventory.setItem(Items.goldenOil, 0);
        }
        else
        {
            resellModifier += GOLDEDOIL_SELL_BOOST * goldenOils;
        }

        var value = totalMoney;

        if (Curser.isCursed(co, EnumCurse.HALVED_SELL_VALUE))
        {
            value /= 2;
        }

        if (Curser.isBlessed(co, EnumBlessing.BETTER_SELL_PRICES))
        {
            value = value * 17 / 10;
        }

        long actualValue = value * resellModifier / 1000;

        co.inventory.addCoins(actualValue);

        if (exploded)
        {
            co.respondf("""
            *:boom: Your %s barrels exploded, making you lose **%s** %s from this trade.*
            You sold %s for %s.
            """,
                BotdirilFmt.amountOfMD(goldenOils, Items.goldenOil),
                BotdirilFmt.amountOfMD(value - actualValue, Icons.COIN),
                BotdirilFmt.amountOfMD(amountOfArticle, articleName),
                BotdirilFmt.amountOfMD(actualValue, Icons.COIN));
        }
        else
        {
            if (actualValue != value)
            {
                co.respondf("You sold %s for %s plus extra %s from your %s barrels.",
                    BotdirilFmt.amountOfMD(amountOfArticle, articleName),
                    BotdirilFmt.amountOfMD(value, Icons.COIN),
                    BotdirilFmt.amountOfMD(actualValue - value, Icons.COIN),
                    BotdirilFmt.amountOfMD(goldenOils, Items.goldenOil));
            }
            else
            {
                co.respondf("You sold %s for %s.", BotdirilFmt.amountOfMD(amountOfArticle, articleName), BotdirilFmt.amountOfMD(value, Icons.COIN));
            }
        }
    }

    @CmdInvoke
    public static void sell(CommandContext co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item)
    {
        sell(co, item, 1);
    }

    @CmdInvoke
    public static void sell(CommandContext co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item, @CmdPar(value = "amount", type = ParType.AMOUNT_ITEM_OR_CARD) long amount)
    {
        CommandAssert.numberMoreThanZeroL(amount, "You can't sell zero items / cards.");

        if (item instanceof Item iitem)
        {
            if (!ShopEntries.canBeSold(iitem))
            {
                throw new CommandException("That item / card cannot be sold.");
            }

            CommandAssert.numberNotAboveL(amount, co.inventory.howManyOf(iitem), "You don't have that many items of that type.");
            co.inventory.addItem(iitem, -amount);

            sellRoutine(co, item.inlineDescription(), amount, amount * ShopEntries.getSellValue(item));
        }
        else if (item instanceof Card card)
        {
            CommandAssert.numberNotAboveL(amount, co.inventory.howManyOf(card) - 1, "You don't have that many cards of that type. Keep in mind you need to keep at least one card of each type once you receive it.");
            co.inventory.addCard(card, -amount);
            var cardLevel = co.inventory.getCardLevel(card);

            sellRoutine(co, item.inlineDescription(), amount, amount * Card.getPrice(card, cardLevel));
        }
    }
}
