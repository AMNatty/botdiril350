package cz.tefek.botdiril.command.interactive;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.userdata.EnumCurrency;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.userdata.timers.Timers;
import cz.tefek.botdiril.util.BotdirilFmt;
import cz.tefek.botdiril.util.BotdirilRnd;

@Command(value = "farm", category = CommandCategory.INTERACTIVE, description = "Farm to get some gold, experience and items.")
public class CommandFarm
{
    @CmdInvoke
    public static void farm(CallObj co)
    {
        CommandAssert.assertTimer(co.ui, Timers.farm, "You need to wait **$** to **farm** again.");

        var levelScalingCoins = Math.pow(co.ui.getLevel(), 1.6);
        var levelScalingKeks = Math.pow(co.ui.getLevel(), 1.8);
        var levelScalingXP = Math.pow(co.ui.getLevel(), 1.2);

        var coins = BotdirilRnd.RDG.nextLong(Math.round(100 + levelScalingCoins * 20L), Math.round(300 + levelScalingCoins * 35L));

        var keks = BotdirilRnd.RDG.nextLong(Math.round(300 + levelScalingKeks * 200L), Math.round(500 + levelScalingKeks * 600L));

        var xp = BotdirilRnd.RDG.nextLong(Math.round(100 + levelScalingXP * 20L), Math.round(300 + levelScalingXP * 35L));

        var trash = 1;

        co.ui.addKeks(keks);
        co.ui.addCoins(coins);
        co.ui.addItem(Items.trash, trash);
        co.ui.addXP(co, xp);

        var op = String.format("You farmed and got **%s %s**, **%s %s** and **%s %s**. **[%s XP]**",
            BotdirilFmt.format(coins), EnumCurrency.COINS.getIcon(),
            BotdirilFmt.format(keks), EnumCurrency.KEKS.getIcon(),
            BotdirilFmt.format(trash), Icons.ITEM_TRASH,
            BotdirilFmt.format(xp));

        co.po.incrementStat(EnumStat.TIMES_FARMED);

        co.respond(op);
    }
}
