package com.botdiril.command.currency;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.timers.EnumTimer;
import com.botdiril.userdata.timers.TimerUtil;
import com.botdiril.util.BotdirilFmt;

import java.util.function.Function;

@Command("payoutmegakeks")
public class CommandPayoutMegaKeks
{
    private static final Function<Long, Long> conversion = megaKeks -> megaKeks * 80 + megaKeks * megaKeks * 4;
    private static final Function<Long, Long> xpConversion = megaKeks -> megaKeks * 5;

    @CmdInvoke
    public static void payout(CommandContext co)
    {
        var has = co.inventory.getMegaKeks();
        CommandAssert.numberMoreThanZeroL(has, String.format("You can't pay out zero %s.", Icons.MEGAKEK));

        if (co instanceof ChatCommandContext cch)
        {
            var gets = conversion.apply(has);
            var xp = xpConversion.apply(has);

            co.respondf("""
            You would receive %s and **[+%s]** for your %s.
            Type `%s%s confirm` to confirm this transaction.
            """,
                BotdirilFmt.amountOfMD(gets, Icons.COIN),
                BotdirilFmt.amountOf(xp, Icons.XP),
                BotdirilFmt.amountOf(has, Icons.MEGAKEK),
                cch.usedPrefix, cch.usedAlias
            );

            return;
        }

        payoutConfirmed(co);
    }

    @CmdInvoke
    public static void payout(ChatCommandContext co, @CmdPar("confirm") String confirmation)
    {
        var has = co.inventory.getMegaKeks();
        CommandAssert.numberMoreThanZeroL(has, String.format("You can't pay out zero %s.", Icons.MEGAKEK));

        CommandAssert.assertEquals("confirm", confirmation, "Type `%s%s confirm` to confirm this transaction.".formatted(co.usedPrefix, co.usedAlias));

        payoutConfirmed(co);
    }

    private static void payoutConfirmed(CommandContext co)
    {
        var has = co.inventory.getMegaKeks();

        TimerUtil.require(co.inventory, EnumTimer.PAYOUT, "You need to wait **$** before paying out again.");

        var gets = conversion.apply(has);

        co.inventory.setMegaKeks(0);
        co.inventory.addCoins(gets);

        var xp = xpConversion.apply(has);
        co.inventory.addXP(co, xp);

        co.respondf("Paid out %s for %s. **[+%s]**",
            BotdirilFmt.amountOfMD(has, Icons.MEGAKEK),
            BotdirilFmt.amountOfMD(gets, Icons.COIN),
            BotdirilFmt.amountOf(xp, Icons.XP));
    }
}
