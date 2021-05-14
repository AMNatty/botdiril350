package com.botdiril.command.gambling;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.gamelogic.gamble.GambleAPI;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.timers.EnumTimer;
import com.botdiril.userdata.timers.TimerUtil;
import com.botdiril.util.BotdirilFmt;
import com.botdiril.util.BotdirilRnd;

@Command(value = "diceroll", category = CommandCategory.GAMBLING, description = "Rolls a six-sided die. You can specify a number to gamble keks.")
public class CommandDiceRoll
{
    private static final int SIDES = 6;

    @CmdInvoke
    public static void roll(CommandContext co)
    {
        co.respond(String.format(":game_die: You rolled a **%d**!", BotdirilRnd.RDG.nextInt(1, 6)));
    }

    @CmdInvoke
    public static void roll(CommandContext co, @CmdPar(value = "keks", type = ParType.AMOUNT_CLASSIC_KEKS) long keks, @CmdPar("bet on side") int number)
    {
        CommandAssert.numberMoreThanZeroL(keks, "You can't gamble zero keks...");
        CommandAssert.numberInBoundsInclusiveL(number, 1, 6, "You can't bet on a side that does not exit... Use a number in the range 1..6");

        long xp;

        if (TimerUtil.tryConsume(co.inventory, EnumTimer.GAMBLE_XP))
            xp = GambleAPI.XP_CONVERSION_BOOSTED.applyAsLong(keks);
        else
            xp = GambleAPI.XP_CONVERSION.applyAsLong(keks);

        co.inventory.addXP(co, xp);

        var rolled = BotdirilRnd.rollDie(SIDES);

        // Slightly offset the odds against the player to adjust for the jackpot
        var offsetChance = 0.95;
        var offset = BotdirilRnd.rollChance(offsetChance);

        if (!offset)
            while (rolled == number)
                rolled = BotdirilRnd.rollDie(SIDES);

        if (rolled == number)
        {
            var reward = keks * 5;
            co.inventory.addKeks(reward);
            co.respondf(":game_die: You rolled a **%d**! Here are your %s. **[+%s]**", rolled, BotdirilFmt.amountOfMD(reward, Icons.KEK), BotdirilFmt.amountOf(xp, Icons.XP));
        }
        else
        {
            co.userProperties.setJackpot(co.userProperties.getJackpot() + GambleAPI.JACKPOT_POOL_CONVERSION.applyAsLong(keks),
                co.userProperties.getJackpotStored() + GambleAPI.JACKPOT_STORE_CONVERSION.applyAsLong(keks));
            co.inventory.addKeks(-keks);
            co.respondf(":game_die: You rolled a **%d**! You **lost** your %s. **[+%s]**", rolled, BotdirilFmt.amountOfMD(keks, Icons.KEK), BotdirilFmt.amountOf(xp, Icons.XP));
        }
    }
}
