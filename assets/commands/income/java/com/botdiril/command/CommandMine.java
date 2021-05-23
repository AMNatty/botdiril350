package com.botdiril.command;


import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.gamelogic.mine.MineAPI;
import com.botdiril.gamelogic.mine.MineInput;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ItemAssert;
import com.botdiril.userdata.item.ItemDrops;
import com.botdiril.userdata.item.ItemPair;
import com.botdiril.userdata.items.Items;
import com.botdiril.userdata.items.pickaxe.ItemPickaxe;
import com.botdiril.userdata.preferences.EnumUserPreference;
import com.botdiril.userdata.preferences.UserPreferences;
import com.botdiril.userdata.stat.EnumStat;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.userdata.tempstat.EnumBlessing;
import com.botdiril.userdata.tempstat.EnumCurse;
import com.botdiril.userdata.timers.EnumTimer;
import com.botdiril.userdata.timers.TimerUtil;
import com.botdiril.util.BotdirilFmt;

import java.util.stream.Collectors;

@Command("mine")
public class CommandMine
{
    @CmdInvoke
    public static void mine(CommandContext co, @CmdPar("pickaxe") Item item)
    {
        mine(co, item, null);
    }

    @CmdInvoke
    public static void mine(CommandContext co, @CmdPar("pickaxe") Item item, @CmdPar("booster item") Item booster)
    {
        TimerUtil.require(co.inventory, EnumTimer.MINE, "You still need to wait **$** to **mine**.");

        if (!(item instanceof ItemPickaxe pick))
            throw new CommandException("That's not a valid pickaxe.");

        ItemAssert.requireItem(co.inventory, pick);

        if (booster != null)
        {
            CommandAssert.assertTrue(MineAPI.canBoost(booster), "*You **cannot** use **%s** to boost mining.*".formatted(booster));
            ItemAssert.consumeItem(co.inventory, booster);
        }

        var resultStr = new StringBuilder();

        var repairKitCount = co.inventory.howManyOf(Items.repairKit);

        var userLevel = co.inventory.getLevel();

        var loot = new ItemDrops();

        var mineInput = new MineInput(pick, booster, repairKitCount, userLevel);
        mineInput.setBlessedMiningSurge(Curser.isBlessed(co, EnumBlessing.MINE_SURGE));
        mineInput.setBlessedUnbreakablePickaxe(Curser.isBlessed(co, EnumBlessing.UNBREAKABLE_PICKAXE));
        mineInput.setCursedDoubleBreak(Curser.isCursed(co, EnumCurse.DOUBLE_PICKAXE_BREAK_CHANCE));
        mineInput.setPreferenceRepairKitEnabled(!UserPreferences.isBitEnabled(co.userProperties, EnumUserPreference.REPAIR_KIT_DISABLED));

        var mineResult = MineAPI.mine(loot, mineInput);

        var lostItems = mineResult.getLostItems();
        var modifiers = mineResult.getMultipliers();

        if (mineResult.isInstantlyRefreshed())
        {
            resultStr.append("*You mine with such precision that you feel like mining again instantly.*\n");
            co.inventory.resetTimer(EnumTimer.MINE);
        }

        resultStr.append(String.format("You are mining with a **%s**", pick.inlineDescription()));

        if (mineResult.isMiningWithoutRepairKit() && pick.getChanceToBreak() > 0)
        {
            resultStr.append(" and *without a repair kit*");
        }

        resultStr.append(".\n");

        if (lostItems.hasItemDropped(Items.repairKit))
        {
            co.userProperties.incrementStat(EnumStat.REPAIR_KITS_USED);
            resultStr.append("**Your ");
            resultStr.append(pick.inlineDescription());
            resultStr.append(" broke while mining, but you managed to fix it using a ");
            resultStr.append(Items.repairKit.inlineDescription());
            resultStr.append("**\n");
        }
        else if (lostItems.hasItemDropped(pick))
        {
            co.userProperties.incrementStat(EnumStat.PICKAXES_BROKEN);
            resultStr.append("**You broke the ");
            resultStr.append(pick.inlineDescription());

            var trash = (long) Math.ceil(Math.log10(pick.getPickaxeValue()));
            co.inventory.addItem(Items.ash, trash);
            resultStr.append(String.format(" while mining, but you managed to salvage the resources for %s", BotdirilFmt.amountOf(trash, Items.ash)));

            var prevPick = pick.getPreviousPickaxe();

            if (prevPick != null)
            {
                co.inventory.addItem(prevPick);
                resultStr.append(String.format("and a %s", prevPick.inlineDescription()));
            }

            resultStr.append(".**\n");
        }

        var xp = mineResult.getXP();
        co.inventory.addXP(co, xp);

        var lootStr = loot.stream().map(ItemPair::toString).collect(Collectors.joining(", "));

        resultStr.append("You found ");

        if (Curser.isCursed(co, EnumCurse.CANT_SEE_MINED_STUFF))
        {
            resultStr.append("*something*");
        }
        else
        {
            resultStr.append("**");
            resultStr.append(lootStr);
            resultStr.append("**");
        }

        resultStr.append(" and ");
        resultStr.append(BotdirilFmt.amountOfMD(xp, Icons.XP));
        resultStr.append(".\n");

        loot.stream().forEach(ip -> co.inventory.addItem(ip.getItem(), ip.getAmount()));
        lostItems.stream().forEach(ip -> co.inventory.addItem(ip.getItem(), -ip.getAmount()));

        co.userProperties.incrementStat(EnumStat.TIMES_MINED);

        resultStr.append("[Modifiers: ");

        modifiers.forEach((key, value) -> resultStr.append(String.format("%s - `%.2f`, ", key.getLocalizedName(), value)));

        resultStr.append(String.format("total multiplier - **%.2fx** ]\n", modifiers.values().stream().reduce(1.0, (acc, other) -> acc * other)));

        co.respond(resultStr.toString());
    }
}
