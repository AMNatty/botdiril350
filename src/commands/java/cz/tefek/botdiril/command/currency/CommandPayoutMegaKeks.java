package cz.tefek.botdiril.command.currency;

import java.util.function.Function;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.userdata.timers.EnumTimer;
import cz.tefek.botdiril.userdata.timers.TimerUtil;
import cz.tefek.botdiril.util.BotdirilFmt;

@Command(value = "payoutmegakeks", aliases = { "payoutmega",
        "bigpayout" }, category = CommandCategory.CURRENCY, description = "Pay out your " + Icons.MEGAKEK + " for some " + Icons.COIN, levelLock = 1)
public class CommandPayoutMegaKeks
{
    private static final Function<Long, Long> conversion = megaKeks -> megaKeks * 80 + megaKeks * megaKeks * 4;
    private static final Function<Long, Long> xpConversion = megaKeks -> megaKeks * 5;

    @CmdInvoke
    public static void payout(CallObj co)
    {
        var has = co.ui.getMegaKeks();
        CommandAssert.numberMoreThanZeroL(has, String.format("You can't pay out zero %s.", Icons.MEGAKEK));

        var gets = conversion.apply(has);
        var xp = xpConversion.apply(has);

        co.respond(String.format("You would receive **%s** %s and **[+%s XP]** for your **%s** %s.\n" +
                                 "Type `%s%s confirm` to confirm this transaction.",
             BotdirilFmt.format(gets), Icons.COIN, BotdirilFmt.format(xp), BotdirilFmt.format(has), Icons.MEGAKEK,
            co.usedPrefix, co.usedAlias));
    }

    @CmdInvoke
    public static void payout(CallObj co, @CmdPar("confirm") String confirmation)
    {
        var has = co.ui.getMegaKeks();
        CommandAssert.numberMoreThanZeroL(has, String.format("You can't pay out zero %s.", Icons.MEGAKEK));

        CommandAssert.assertEquals("confirm", confirmation, "Type `%s%s confirm` to confirm this transaction.".formatted(co.usedPrefix, co.usedAlias));

        TimerUtil.require(co.ui, EnumTimer.PAYOUT, "You need to wait **$** before paying out again.");

        var gets = conversion.apply(has);

        co.ui.setMegaKeks(0);
        co.ui.addCoins(gets);

        var xp = xpConversion.apply(has);
        co.ui.addXP(co, xp);

        co.respond(String.format("Paid out **%s** %s for **%s** %s. **[+%s XP]**",
            BotdirilFmt.format(has), Icons.MEGAKEK, BotdirilFmt.format(gets), Icons.COIN, BotdirilFmt.format(xp)));
    }
}
