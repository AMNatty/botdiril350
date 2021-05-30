package com.botdiril.command;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.gamelogic.coinflip.EnumCoinSides;
import com.botdiril.gamelogic.gamble.GambleAPI;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.timers.EnumTimer;
import com.botdiril.userdata.timers.TimerUtil;
import com.botdiril.util.BotdirilFmt;
import com.botdiril.util.BotdirilRnd;

@Command("coinflip")
public class CommandCoinFlip
{
    @CmdInvoke
    public static void roll(CommandContext co)
    {
        co.respond(co.random.nextBoolean() ? "**Heads.**" : "**Tails.**");
    }

    @CmdInvoke
    public static void roll(CommandContext co, @CmdPar(value = "keks", type = ParType.AMOUNT_CLASSIC_KEKS) long keks)
    {
        CommandAssert.numberMoreThanZeroL(keks, "You can't gamble zero keks...");

        long xp;

        if (TimerUtil.tryConsume(co.inventory, EnumTimer.GAMBLE_XP))
            xp = GambleAPI.XP_CONVERSION_BOOSTED.applyAsLong(keks);
        else
            xp = GambleAPI.XP_CONVERSION.applyAsLong(keks);

        co.inventory.addXP(co, xp);

        var rolled = co.random.nextBoolean() ? EnumCoinSides.HEADS : EnumCoinSides.TAILS;

        // Slightly offset the odds against the player to adjust for the jackpot
        var offsetChance = 0.95;
        var offset = BotdirilRnd.rollChance(co.rdg, offsetChance);

        if (rolled == EnumCoinSides.HEADS && offset)
        {
            co.inventory.addKeks(keks);
            co.respondf("**:large_orange_diamond: Heads!** Here are your %s. **[+%s]**", BotdirilFmt.amountOfMD(keks, Icons.KEK), BotdirilFmt.amountOf(xp, Icons.XP));
        }
        else
        {
            co.userProperties.setJackpot(co.userProperties.getJackpot() + GambleAPI.JACKPOT_POOL_CONVERSION.applyAsLong(keks),
                co.userProperties.getJackpotStored() + GambleAPI.JACKPOT_STORE_CONVERSION.applyAsLong(keks));
            co.inventory.addKeks(-keks);
            co.respondf("**:large_blue_diamond: Tails.** You **lost** your %s. **[+%s]**", BotdirilFmt.amountOfMD(keks, Icons.KEK), BotdirilFmt.amountOf(xp, Icons.XP));
        }
    }
}
