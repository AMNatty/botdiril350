package com.botdiril.command.currency;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.gamelogic.daily.DailyRewards;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.userdata.tempstat.EnumCurse;
import com.botdiril.userdata.timers.EnumTimer;
import com.botdiril.userdata.timers.TimerUtil;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.stat.EnumStat;
import com.botdiril.util.BotdirilFmt;

@Command(value = "daily", category = CommandCategory.CURRENCY, description = "Get yourself some free daily stuff.")
public class CommandDaily
{
    @CmdInvoke
    public static void daily(CommandContext co)
    {
        TimerUtil.require(co.ui, EnumTimer.DAILY, "You need to wait **$** to use **daily** again.");

        if (Curser.isCursed(co, EnumCurse.CANT_TAKE_DAILY))
        {
            throw new CommandException("***You prepare to check out your daily loot but some magical spell is preventing you from doing so.***");
        }

        var result = DailyRewards.generateRewards(co.ui.getLevel());

        var xp = result.getXP();
        var coins = result.getCoins();
        var keks = result.getKeks();
        var megaKeks = result.getMegaKeks();
        var keys = result.getKeys();

        co.ui.addXP(co, xp);
        co.ui.addCoins(coins);
        co.ui.addKeks(keks);
        co.ui.addMegaKeks(megaKeks);
        co.ui.addKeys(keys);

        var str = String.format("**Here are your daily items:**\n%s xp\n%s %s\n%s %s\n%s %s\n%s %s", BotdirilFmt.format(xp), BotdirilFmt.format(coins), Icons.COIN, BotdirilFmt.format(keks), Icons.KEK, BotdirilFmt.format(megaKeks), Icons.MEGAKEK, BotdirilFmt.format(keys), Icons.KEY);
        co.respond(str);

        co.po.incrementStat(EnumStat.TIMES_DAILY);
    }
}
