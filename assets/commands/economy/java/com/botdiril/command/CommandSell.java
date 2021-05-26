package com.botdiril.command;

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

import java.util.ArrayList;
import java.util.stream.Collectors;

@Command("sell")
public class CommandSell
{
    public static final double CHANCE_TO_EXPLODE = 0.005;

    public static final double CURSE_LOSS_MODIFIER = 0.5;
    public static final double BLESS_BONUS_MODIFIER = 0.7;

    public static final double GOLDEDOIL_SELL_BOOST = 0.01;
    public static final double PRISMATICOIL_SELL_BOOST = 0.0075;
    public static final double EXPLOSION_MODIFIER = 0.15;

    public static void sellRoutine(CommandContext co, String articleName, long amountOfArticle, long totalMoney)
    {
        record SellBonus(long bonus, String description)
        {

        }

        var bonuses = new ArrayList<SellBonus>();

        long goldenOils = co.inventory.howManyOf(Items.goldenOil);
        boolean goldenOilDisabled = UserPreferences.isBitEnabled(co.userProperties, EnumUserPreference.GOLDEN_OIL_DISABLED);

        if (!goldenOilDisabled && goldenOils > 0)
        {
            if (BotdirilRnd.rollChance(goldenOils * CHANCE_TO_EXPLODE))
            {
                var lost = Math.round(totalMoney * EXPLOSION_MODIFIER);
                co.respondf("""
                *:boom: Your %s barrels exploded, making you lose **%s** from this trade.*
                """, BotdirilFmt.amountOfMD(goldenOils, Items.goldenOil),
                        BotdirilFmt.amountOfMD(lost, Icons.COIN));

                bonuses.add(new SellBonus(-lost, ""));
                co.inventory.setItem(Items.goldenOil, 0);
            }
            else
            {
                long bonus = Math.round(totalMoney * GOLDEDOIL_SELL_BOOST * goldenOils);
                bonuses.add(new SellBonus(bonus, " plus %s from your %s barrels".formatted(
                    BotdirilFmt.amountOfMD(bonus, Icons.COIN),
                    BotdirilFmt.amountOfMD(goldenOils, Items.goldenOil)))
                );
            }
        }

        long prismaticOils = co.inventory.howManyOf(Items.prismaticOil);

        if (prismaticOils > 0)
        {
            long bonus = Math.round(totalMoney * PRISMATICOIL_SELL_BOOST * prismaticOils);
            bonuses.add(new SellBonus(bonus, " plus %s from your %s barrels".formatted(
                BotdirilFmt.amountOfMD(bonus, Icons.COIN),
                BotdirilFmt.amountOfMD(prismaticOils, Items.prismaticOil)))
            );
        }

        if (Curser.isCursed(co, EnumCurse.HALVED_SELL_VALUE))
        {
            long lost = Math.round(totalMoney * CURSE_LOSS_MODIFIER);

            bonuses.add(new SellBonus(-lost, " minus %s due to %s".formatted(
                BotdirilFmt.amountOfMD(lost, Icons.COIN),
                BotdirilFmt.amountOfMD("the", EnumCurse.HALVED_SELL_VALUE)))
            );
        }

        if (Curser.isBlessed(co, EnumBlessing.BETTER_SELL_PRICES))
        {
            long bonus = Math.round(totalMoney * BLESS_BONUS_MODIFIER);

            bonuses.add(new SellBonus(bonus, " plus %s due to %s".formatted(
                BotdirilFmt.amountOfMD(bonus, Icons.COIN),
                BotdirilFmt.amountOfMD("the", EnumBlessing.BETTER_SELL_PRICES)))
            );
        }



        var bonusSum = bonuses.stream().mapToLong(SellBonus::bonus).sum();
        var newTotal = totalMoney + bonusSum;
        co.inventory.addCoins(newTotal);

        var base = "You sold %s for %s".formatted(
            BotdirilFmt.amountOfMD(amountOfArticle, articleName),
            BotdirilFmt.amountOfMD(totalMoney, Icons.COIN)
        );

        if (bonuses.isEmpty())
        {
            co.respondf("%s.", base);
            return;
        }

        var bonusesStr = bonuses.stream().map(SellBonus::description).collect(Collectors.joining());

        co.respondf("%s%s for a grand total of %s.", base, bonusesStr, BotdirilFmt.amountOfMD(newTotal, Icons.COIN));
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
