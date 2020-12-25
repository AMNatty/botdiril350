package cz.tefek.botdiril.command.gambling;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.ParType;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.gamelogic.coinflip.EnumCoinSides;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.userdata.timers.EnumTimer;
import cz.tefek.botdiril.userdata.timers.TimerUtil;
import cz.tefek.botdiril.userdata.xp.XPRewards;
import cz.tefek.botdiril.util.BotdirilRnd;

@Command(value = "coinflip", category = CommandCategory.GAMBLING, description = "Coin flip. You can specify a number to gamble keks.")
public class CommandCoinFlip
{
    @CmdInvoke
    public static void roll(CallObj co)
    {
        co.respond(BotdirilRnd.RANDOM.nextBoolean() ? "**Heads.**" : "**Tails.**");
    }

    @CmdInvoke
    public static void roll(CallObj co, @CmdPar(value = "keks", type = ParType.AMOUNT_CLASSIC_KEKS) long keks, @CmdPar("bet on side") EnumCoinSides side)
    {
        CommandAssert.numberMoreThanZeroL(keks, "You can't gamble zero keks...");

        if (TimerUtil.tryConsume(co.ui, EnumTimer.GAMBLE_XP))
        {
            var lvl = co.ui.getLevel();
            co.ui.addXP(co, Math.round(XPRewards.getXPAtLevel(lvl) * XPRewards.getLevel(lvl).getGambleFalloff() * BotdirilRnd.RDG.nextUniform(0.00001, 0.0001)));
        }

        var rolled = BotdirilRnd.RANDOM.nextBoolean() ? EnumCoinSides.HEADS : EnumCoinSides.TAILS;

        if (rolled == side)
        {
            co.ui.addKeks(keks);
            co.respond(String.format("**%s!** Here are your **%d %s**.", rolled == EnumCoinSides.HEADS
                    ? ":large_orange_diamond: Heads"
                    : ":large_blue_diamond: Tails", keks, Icons.KEK));
        }
        else
        {
            co.ui.addKeks(-keks);
            co.respond(String.format("**%s!** You **lost** your **%d %s**.", rolled == EnumCoinSides.HEADS
                    ? ":large_orange_diamond: Heads"
                    : ":large_blue_diamond: Tails", keks, Icons.KEK));
        }
    }
}
