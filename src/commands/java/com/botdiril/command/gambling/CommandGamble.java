package com.botdiril.command.gambling;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.gamelogic.gamble.GambleAPI;
import com.botdiril.gamelogic.gamble.GambleInput;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.userdata.tempstat.EnumCurse;
import com.botdiril.userdata.timers.EnumTimer;
import com.botdiril.userdata.timers.TimerUtil;
import com.botdiril.userdata.xp.XPRewards;
import com.botdiril.util.BotdirilFmt;

@Command(value = "gamble", category = CommandCategory.GAMBLING, levelLock = 5, description = "The good old gamble.")
public class CommandGamble
{
    @CmdInvoke
    public static void gamble(CommandContext co, @CmdPar(value = "amount of keks", type = ParType.AMOUNT_CLASSIC_KEKS) long keks)
    {
        CommandAssert.numberMoreThanZeroL(keks, "You can't gamble zero keks...");

        var gambleInput = new GambleInput(keks,
            TimerUtil.tryConsume(co.ui, EnumTimer.GAMBLE_XP),
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
