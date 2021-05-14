package com.botdiril.command.currency;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.stat.EnumStat;
import com.botdiril.userdata.timers.EnumTimer;
import com.botdiril.userdata.timers.TimerUtil;
import com.botdiril.util.BotdirilFmt;

@Command(value = "payoutkeks", aliases = {
        "payout", "kekspayout", "kekspayout", "payoutkek" }, category = CommandCategory.CURRENCY, description = "Pay out your keks for some tokens.")
public class CommandPayoutKeks
{
    private static final long conversionRate = 125;

    @CmdInvoke
    public static void payout(CommandContext co, @CmdPar(value = "how many", type = ParType.AMOUNT_CLASSIC_KEKS) long keks)
    {
        CommandAssert.numberMoreThanZeroL(keks, "You can't pay out zero keks.");

        TimerUtil.require(co.inventory, EnumTimer.PAYOUT, "You need to wait **$** before paying out again.");

        var tokens = keks / conversionRate;
        co.inventory.addKeks(-keks);
        co.inventory.addKekTokens(tokens);
        var xp = Math.round(Math.pow(keks / 5.0 + 1.0, 0.55));
        co.inventory.addXP(co, xp);

        if (co.userProperties.getStat(EnumStat.BIGGEST_PAYOUT) < keks)
            co.userProperties.setStat(EnumStat.BIGGEST_PAYOUT, keks);

        co.respondf("Paid out %s for %s at a conversion rate of **%d:1**. **[+%s]**",
            BotdirilFmt.amountOfMD(keks, Icons.KEK),
            BotdirilFmt.amountOf(tokens, Icons.TOKEN),
            conversionRate,
            BotdirilFmt.amountOf(xp, Icons.XP));
    }
}
