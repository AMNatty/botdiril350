package cz.tefek.botdiril.command.gambling;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.ParType;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.gamelogic.gamble.GambleAPI;
import cz.tefek.botdiril.gamelogic.gamble.GambleInput;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.userdata.tempstat.Curser;
import cz.tefek.botdiril.userdata.tempstat.EnumCurse;
import cz.tefek.botdiril.userdata.timers.Timers;
import cz.tefek.botdiril.userdata.xp.XPRewards;
import cz.tefek.botdiril.util.BotdirilFmt;

@Command(value = "gamble", category = CommandCategory.GAMBLING, levelLock = 5, description = "The good old gamble.")
public class CommandGamble
{
    @CmdInvoke
    public static void gamble(CallObj co, @CmdPar(value = "amount of keks", type = ParType.AMOUNT_CLASSIC_KEKS) long keks)
    {
        CommandAssert.numberMoreThanZeroL(keks, "You can't gamble zero keks...");

        var gambleInput = new GambleInput(keks,
            co.ui.useTimer(Timers.gambleXP) == -1,
            Curser.isCursed(co, EnumCurse.CANT_WIN_JACKPOT),
            co.po.getJackpot(),
            co.po.getJackpotStored());

        var result = GambleAPI.roll(gambleInput);

        var outcome = result.getOutcome();
        var kekDifference = result.getKekDifference();

        var resultText = String.format(outcome.getText(), BotdirilFmt.format(Math.abs(kekDifference)));

        co.ui.addKeks(kekDifference);

        if (result.shouldUpdateJackpotPool())
            co.po.setJackpot(result.getNewJackpotPool(), result.getNewJackpotStored());

        var xp = result.getXPGained();
        var gambleXP = Math.round(xp * XPRewards.getLevel(co.ui.getLevel()).getGambleFalloff());
        co.ui.addXP(co, gambleXP);

        if (result.hasMissedJackpot())
        {
            var resultStr = """
            %s **[+ %sXP]**
            %s *It would be a real shame if you missed the jackpot because of your curse.* <a:pepeLaughing:791861756800270346>
            """.formatted(resultText, BotdirilFmt.format(gambleXP),
                Icons.SCROLL_RARE);

            co.respond(resultStr);

            return;
        }

        co.respond(String.format("%s **[+ %sXP]**", resultText,  BotdirilFmt.format(gambleXP)));
    }
}
