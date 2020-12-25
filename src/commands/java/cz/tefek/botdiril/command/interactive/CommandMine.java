package cz.tefek.botdiril.command.interactive;


import java.util.stream.Collectors;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.gamelogic.mine.MineAPI;
import cz.tefek.botdiril.gamelogic.mine.MineInput;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.item.ItemDrops;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.items.pickaxe.ItemPickaxe;
import cz.tefek.botdiril.userdata.preferences.EnumUserPreference;
import cz.tefek.botdiril.userdata.preferences.UserPreferences;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.userdata.tempstat.Curser;
import cz.tefek.botdiril.userdata.tempstat.EnumBlessing;
import cz.tefek.botdiril.userdata.tempstat.EnumCurse;
import cz.tefek.botdiril.userdata.timers.EnumTimer;
import cz.tefek.botdiril.userdata.timers.TimerUtil;
import cz.tefek.botdiril.util.BotdirilFmt;

@Command(value = "mine", category = CommandCategory.INTERACTIVE, description = "Mine to get some sweet stuff.")
public class CommandMine
{
    @CmdInvoke
    public static void mine(CallObj co, @CmdPar("pickaxe") Item item)
    {
        TimerUtil.require(co.ui, EnumTimer.MINE, "You still need to wait **$** to **mine**.");

        CommandAssert.assertTrue(item instanceof ItemPickaxe, "That's not a valid pickaxe.");

        CommandAssert.numberMoreThanZeroL(co.ui.howManyOf(item), "You don't have that pickaxe.");

        var pick = (ItemPickaxe) item;

        var resultStr = new StringBuilder();

        var repairKitCount = co.ui.howManyOf(Items.repairKit);

        var userLevel = co.ui.getLevel();

        var loot = new ItemDrops();

        var mineInput = new MineInput(pick, repairKitCount, userLevel);
        mineInput.setBlessedMiningSurge(Curser.isBlessed(co, EnumBlessing.MINE_SURGE));
        mineInput.setBlessedUnbreakablePickaxe(Curser.isBlessed(co, EnumBlessing.UNBREAKABLE_PICKAXE));
        mineInput.setCursedDoubleBreak(Curser.isCursed(co, EnumCurse.DOUBLE_PICKAXE_BREAK_CHANCE));
        mineInput.setPreferenceRepairKitEnabled(!UserPreferences.isBitEnabled(co.po, EnumUserPreference.REPAIR_KIT_DISABLED));

        var mineResult = MineAPI.mine(loot, mineInput);

        var lostItems = mineResult.getLostItems();
        var modifiers = mineResult.getMultipliers();

        if (mineResult.isInstantlyRefreshed())
        {
            resultStr.append("*You mine with such precision that you feel like mining again instantly.*\n");
            co.ui.resetTimer(EnumTimer.MINE);
        }

        resultStr.append(String.format("You are mining with a **%s**", pick.inlineDescription()));

        if (mineResult.isMiningWithoutRepairKit() && pick.getChanceToBreak() > 0)
        {
            resultStr.append(" and *without a repair kit*");
        }

        resultStr.append(".\n");

        if (lostItems.hasItemDropped(Items.repairKit))
        {
            co.po.incrementStat(EnumStat.REPAIR_KITS_USED);
            resultStr.append("**Your ");
            resultStr.append(pick.inlineDescription());
            resultStr.append(" broke while mining, but you managed to fix it using a ");
            resultStr.append(Items.repairKit.inlineDescription());
            resultStr.append("**\n");
        }
        else if (lostItems.hasItemDropped(pick))
        {
            co.po.incrementStat(EnumStat.PICKAXES_BROKEN);
            resultStr.append("**You broke the ");
            resultStr.append(pick.inlineDescription());

            var trash = (long) Math.ceil(Math.log10(pick.getPickaxeValue()));
            co.ui.addItem(Items.trash, trash);
            resultStr.append(String.format(" while mining, but you managed to salvage the resources for %d %s", trash, Items.trash.getIcon()));

            var prevPick = pick.getPreviousPickaxe();

            if (prevPick != null)
            {
                co.ui.addItem(prevPick);
                resultStr.append(String.format("and a %s", prevPick.inlineDescription()));
            }

            resultStr.append(".**\n");
        }

        var xp = mineResult.getXP();
        co.ui.addXP(co, xp);

        var lootStr = loot.stream().map(ip -> "**" + BotdirilFmt.format(ip.getAmount()) + "** **" + ip.getItem().inlineDescription() + "**").collect(Collectors.joining(", "));

        resultStr.append("You found ");

        if (Curser.isCursed(co, EnumCurse.CANT_SEE_MINED_STUFF))
        {
            resultStr.append("*something*");
        }
        else
        {
            resultStr.append(lootStr);
        }

        resultStr.append(" and **");
        resultStr.append(BotdirilFmt.format(xp));
        resultStr.append(" XP**.\n");

        loot.stream().forEach(ip -> co.ui.addItem(ip.getItem(), ip.getAmount()));
        lostItems.stream().forEach(ip -> co.ui.addItem(ip.getItem(), -ip.getAmount()));

        co.po.incrementStat(EnumStat.TIMES_MINED);

        resultStr.append("[Modifiers: ");

        modifiers.forEach((key, value) -> resultStr.append(String.format("%s - `%.2f`, ", key.getLocalizedName(), value)));

        resultStr.append(String.format("total multiplier - **%.2fx** ]\n", modifiers.values().stream().reduce(1.0, (acc, other) -> acc * other)));

        co.respond(resultStr.toString());
    }
}
