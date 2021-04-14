package cz.tefek.botdiril.command.gambling;

import java.util.function.BiFunction;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.ParType;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.userdata.tempstat.Curser;
import cz.tefek.botdiril.userdata.tempstat.EnumBlessing;
import cz.tefek.botdiril.userdata.tempstat.EnumCurse;
import cz.tefek.botdiril.util.BotdirilFmt;
import cz.tefek.botdiril.util.BotdirilRnd;

@Command(value = "biggamble", aliases = { "gamblemega", "gamblemegakeks",
    "mega", "megagamble" }, category = CommandCategory.GAMBLING, description = "Gamble in " + Icons.MEGAKEK + " style. There is a dark secret though.", levelLock = 12)
public class CommandBigGamble
{
    private static final BiFunction<Long, Integer, Double> getChanceToLoseEverything = (amount, level) -> Math.sqrt(Math.max(amount - 1, 0)) / (Math.pow(level, 0.75) + 25.0);

    private static final double LOW_CHANCE_PROTECTION = 0.15;

    @CmdInvoke
    public static void gamble(CallObj co, @CmdPar(value = "amount", type = ParType.AMOUNT_MEGA_KEKS) long amount)
    {
        CommandAssert.numberMoreThanZeroL(amount, "You can't gamble negative or zero " + Icons.MEGAKEK + ".");

        var chanceToLoseEverything = getChanceToLoseEverything.apply(amount, co.ui.getLevel());

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
            co.respond(String.format("**You lost every single %s.**", Icons.MEGAKEK));
            co.po.incrementStat(EnumStat.TIMES_LOST_ALL_MEGAKEKS);
            co.ui.setMegaKeks(0);
            return;
        }

        var mul = BotdirilRnd.RDG.nextGaussian(1.35, 0.5);

        var has = co.ui.getMegaKeks();
        var gets = Math.max(Math.round(mul * amount), -has);

        co.ui.setMegaKeks(has + gets);

        has = has + gets;

        if (gets < 0)
        {
            co.respond(String.format("You **lost** **%s** %s. " +
                                     "*Your chance to lose everything decreased to **%.3f%%***.",
                BotdirilFmt.format(-gets), Icons.MEGAKEK,
                getChanceToLoseEverything.apply(has, co.ui.getLevel()) * 100));
        }
        else if (gets > 0)
        {
            var chance = getChanceToLoseEverything.apply(has, co.ui.getLevel()) * 100;

            if (chance > 30)
            {
                co.respond(String.format("You **won** **%s** %s.\n" +
                                         "*Your chance to lose everything increased to **%.3f%%***. " +
                                         "Consider paying out with `%spayoutmegakeks`.",
                    BotdirilFmt.format(gets), Icons.MEGAKEK,
                    chance,
                    co.usedPrefix));
            }
            else if (chance > LOW_CHANCE_PROTECTION)
            {
                co.respond(String.format("You **won** **%s** %s. " +
                                         "*Your chance to lose everything increased to **%.3f%%***.",
                    BotdirilFmt.format(gets), Icons.MEGAKEK,
                    chance));
            }
        }
        else
        {
            co.respond("You neither win or lose... Somehow.");
        }
    }
}
