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
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.userdata.tempstat.Curser;
import cz.tefek.botdiril.userdata.tempstat.EnumBlessing;
import cz.tefek.botdiril.userdata.tempstat.EnumCurse;
import cz.tefek.botdiril.userdata.timers.EnumTimer;
import cz.tefek.botdiril.userdata.timers.TimerUtil;
import cz.tefek.botdiril.util.BotdirilFmt;

@Command(value = "oilmine", aliases = { "omine" },
    category = CommandCategory.INTERACTIVE,
    description = "Mine with oil, as if your level was ten times higher plus 1000. Your pickaxe cannot break.",
    levelLock = 15)
public class CommandOilMine
{
    @CmdInvoke
    public static void mine(CallObj co, @CmdPar("pickaxe") Item item)
    {
        TimerUtil.require(co.ui, EnumTimer.MINE, "You still need to wait **$** to **mine**.");

        CommandAssert.assertTrue(item instanceof ItemPickaxe, "That's not a valid pickaxe.");

        CommandAssert.numberMoreThanZeroL(co.ui.howManyOf(item), "You don't have that pickaxe.");

        CommandAssert.numberMoreThanZeroL(co.ui.howManyOf(Items.oil), String.format("*You need at least one %s barrel to mine with oil.*", Items.oil.inlineDescription()));

        co.ui.addItem(Items.oil, -1);

        var pick = (ItemPickaxe) item;

        var resultStr = new StringBuilder();

        var repairKitCount = co.ui.howManyOf(Items.repairKit);

        var userLevel = co.ui.getLevel();

        var loot = new ItemDrops();

        var mineInput = new MineInput(pick, repairKitCount, userLevel * 10L + 1000L);
        mineInput.setBlessedMiningSurge(Curser.isBlessed(co, EnumBlessing.MINE_SURGE));
        mineInput.setBlessedUnbreakablePickaxe(true);
        mineInput.setCursedDoubleBreak(false);
        mineInput.setPreferenceRepairKitEnabled(false);

        var mineResult = MineAPI.mine(loot, mineInput);

        var modifiers = mineResult.getMultipliers();

        if (mineResult.isInstantlyRefreshed())
        {
            resultStr.append("*You mine with such precision that you feel like mining again instantly.*\n");
            co.ui.resetTimer(EnumTimer.MINE);
        }

        resultStr.append(String.format("You are mining with a **%s**.\n", pick.inlineDescription()));

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

        co.po.incrementStat(EnumStat.TIMES_MINED);

        resultStr.append("[Modifiers: ");

        modifiers.forEach((key, value) -> resultStr.append(String.format("%s - `%.2f`, ", key.getLocalizedName(), value)));

        resultStr.append(String.format("total multiplier - **%.2fx** ]\n", modifiers.values().stream().reduce(1.0, (acc, other) -> acc * other)));

        co.respond(resultStr.toString());
    }
}
