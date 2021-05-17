package com.botdiril.command.currency;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.gamelogic.daily.DailyRewards;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.stat.EnumStat;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.userdata.tempstat.EnumCurse;
import com.botdiril.userdata.timers.EnumTimer;
import com.botdiril.userdata.timers.TimerUtil;
import com.botdiril.util.BotdirilFmt;

@Command("daily")
public class CommandDaily
{
    @CmdInvoke
    public static void daily(CommandContext co)
    {
        TimerUtil.require(co.inventory, EnumTimer.DAILY, "You need to wait **$** to use **daily** again.");

        if (Curser.isCursed(co, EnumCurse.CANT_TAKE_DAILY))
        {
            throw new CommandException("***You prepare to check out your daily loot but some magical spell is preventing you from doing so.***");
        }

        var result = DailyRewards.generateRewards(co.inventory.getLevel());

        var xp = result.xp();
        var coins = result.coins();
        var keks = result.keks();
        var megaKeks = result.megaKeks();
        var keys = result.keys();

        co.inventory.addXP(co, xp);
        co.inventory.addCoins(coins);
        co.inventory.addKeks(keks);
        co.inventory.addMegaKeks(megaKeks);
        co.inventory.addKeys(keys);

        var str = """
        **Here are your daily items:**
          %s
          %s
          %s
          %s
          %s
        """.formatted(
            BotdirilFmt.amountOfMD(xp, Icons.XP),
            BotdirilFmt.amountOfMD(coins, Icons.COIN),
            BotdirilFmt.amountOfMD(keks, Icons.KEK),
            BotdirilFmt.amountOfMD(megaKeks, Icons.MEGAKEK),
            BotdirilFmt.amountOfMD(keys, Icons.KEY)
        );

        co.respond(str);

        co.userProperties.incrementStat(EnumStat.TIMES_DAILY);
    }
}
