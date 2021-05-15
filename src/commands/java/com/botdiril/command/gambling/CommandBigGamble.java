package com.botdiril.command.gambling;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.stat.EnumStat;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.userdata.tempstat.EnumBlessing;
import com.botdiril.userdata.tempstat.EnumCurse;
import com.botdiril.util.BotdirilFmt;
import com.botdiril.util.BotdirilRnd;

import java.util.function.BiFunction;

@Command(value = "biggamble", aliases = { "gamblemega", "gamblemegakeks",
    "mega", "megagamble" }, category = CommandCategory.GAMBLING, description = "Gamble in megakek style. There is a dark secret though.", levelLock = 3)
public class CommandBigGamble
{
    private static final BiFunction<Long, Integer, Double> getChanceToLoseEverything = (amount, level) -> Math.sqrt(Math.max(amount - 1, 0)) / (Math.pow(level, 0.75) + 25.0);

    private static final double LOW_CHANCE_PROTECTION = 0.15;

    @CmdInvoke
    public static void gamble(CommandContext co, @CmdPar(value = "amount", type = ParType.AMOUNT_MEGA_KEKS) long amount)
    {
        CommandAssert.numberMoreThanZeroL(amount, "You can't gamble negative or zero " + Icons.MEGAKEK + ".");

        var chanceToLoseEverything = getChanceToLoseEverything.apply(amount, co.inventory.getLevel());

        if (Curser.isBlessed(co, EnumBlessing.MEGAKEK_LOSS_IMMUNITY))
        {
            chanceToLoseEverything *= 0.666;
        }

        if (Curser.isCursed(co, EnumCurse.DOUBLE_CHANCE_TO_LOSE_MEGA))
        {
            chanceToLoseEverything *= 2;
        }

        if (BotdirilRnd.rollChance(chanceToLoseEverything) && chanceToLoseEverything > LOW_CHANCE_PROTECTION)
        {
            co.respondf("**You lost every single %s.**", Icons.MEGAKEK);
            co.userProperties.incrementStat(EnumStat.TIMES_LOST_ALL_MEGAKEKS);
            co.inventory.setMegaKeks(0);
            return;
        }

        var mul = BotdirilRnd.RDG.nextGaussian(1.35, 0.5);

        var has = co.inventory.getMegaKeks();
        var gets = Math.max(Math.round(mul * amount), -has);

        co.inventory.setMegaKeks(has + gets);

        has = has + gets;

        if (gets < 0)
        {
            co.respondf("""
            You **lost** %s.
            *Your chance to lose everything decreased to **%.3f%%***.
            """, BotdirilFmt.amountOfMD(-gets, Icons.MEGAKEK), getChanceToLoseEverything.apply(has, co.inventory.getLevel()) * 100);
        }
        else if (gets > 0)
        {
            var chance = getChanceToLoseEverything.apply(has, co.inventory.getLevel()) * 100;

            if (chance > 30)
            {
                co.respondf("""
                 You **won** %s.
                 *Your chance to lose everything increased to **%.3f%%***.
                 """, BotdirilFmt.amountOfMD(gets, Icons.MEGAKEK), chance);

                if (co instanceof ChatCommandContext cch)
                    co.respondf("Consider paying out with `%spayoutmegakeks`.", cch.usedPrefix);
                else
                    co.respond("Consider paying out.");
            }
            else if (chance > LOW_CHANCE_PROTECTION)
            {
                co.respondf("""
                You **won** %s.
                *Your chance to lose everything increased to **%.3f%%***.
                """, BotdirilFmt.amountOfMD(gets, Icons.MEGAKEK), chance);
            }
        }
        else
        {
            co.respond("You neither win or lose... Somehow.");
        }
    }
}