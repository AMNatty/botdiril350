package com.botdiril.command.gambling;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.timers.EnumTimer;
import com.botdiril.userdata.timers.TimerUtil;
import com.botdiril.userdata.xp.XPRewards;
import com.botdiril.util.BotdirilRnd;

@Command(value = "diceroll", category = CommandCategory.GAMBLING, description = "Rolls a six-sided die. You can specify a number to gamble keks.")
public class CommandDiceRoll
{
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

        if (TimerUtil.tryConsume(co.ui, EnumTimer.GAMBLE_XP))
        {
            var lvl = co.ui.getLevel();
            co.ui.addXP(co, Math.round(XPRewards.getXPAtLevel(lvl) * XPRewards.getLevel(lvl).getGambleFalloff() * BotdirilRnd.RDG.nextUniform(0.00001, 0.0001)));
        }

        var rolled = BotdirilRnd.RDG.nextInt(1, 6);

        if (rolled == number)
        {
            var reward = keks * 5;
            co.ui.addKeks(reward);
            co.respond(String.format(":game_die: You rolled a **%d**! Here are your **%d %s**.", rolled, reward, Icons.KEK));
        }
        else
        {
            co.ui.addKeks(-keks);
            co.respond(String.format(":game_die: You rolled a **%d**! You **lost** your **%d %s.**", rolled, keks, Icons.KEK));
        }
    }
}
