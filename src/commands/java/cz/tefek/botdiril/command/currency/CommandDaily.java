package cz.tefek.botdiril.command.currency;

import cz.tefek.botdiril.framework.command.CommandContext;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.gamelogic.daily.DailyRewards;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.userdata.tempstat.Curser;
import cz.tefek.botdiril.userdata.tempstat.EnumCurse;
import cz.tefek.botdiril.userdata.timers.EnumTimer;
import cz.tefek.botdiril.userdata.timers.TimerUtil;
import cz.tefek.botdiril.util.BotdirilFmt;

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
