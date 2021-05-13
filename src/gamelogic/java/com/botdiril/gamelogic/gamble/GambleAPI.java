package com.botdiril.gamelogic.gamble;

import com.botdiril.gamelogic.weighted.IWeightedRandom;

import java.util.function.LongUnaryOperator;

public class GambleAPI
{
    public static final LongUnaryOperator JACKPOT_POOL_CONVERSION = keks -> keks / 8;
    public static final LongUnaryOperator JACKPOT_STORE_CONVERSION = keks -> keks / 25;
    public static final LongUnaryOperator POOL_STORE_CONVERSION = keks -> keks / 25;

    public static final LongUnaryOperator XP_CONVERSION = keks -> Math.round(Math.pow(keks, 0.25));
    public static final LongUnaryOperator XP_CONVERSION_BOOSTED = keks -> Math.round(Math.pow(keks, 0.40));

    public static GambleResult roll(GambleInput gambleInput)
    {
        boolean missedJackpot = false;

        while (true)
        {
            var outcome = IWeightedRandom.choose(EnumGambleOutcome.class);

            if (gambleInput.jackpotCursed() && outcome == EnumGambleOutcome.JACKPOT)
            {
                missedJackpot = true;
                continue;
            }

            var gambleResult = outcome.apply(gambleInput);
            gambleResult.setMissedJackpot(missedJackpot);

            if (outcome == EnumGambleOutcome.JACKPOT)
            {
                var stored = gambleInput.jackpotStored();
                gambleResult.setJackpot(stored, POOL_STORE_CONVERSION.applyAsLong(stored));
            }
            else if (gambleResult.hasLost())
            {
                var lost = -gambleResult.getKekDifference();
                var pool = gambleInput.jackpotPool();
                var stored = gambleInput.jackpotStored();
                gambleResult.setJackpot(pool + JACKPOT_POOL_CONVERSION.applyAsLong(lost),
                    stored + JACKPOT_STORE_CONVERSION.applyAsLong(lost));
            }

            var gambledKeks = gambleInput.gambledKeks();

            var xpConverter = gambleInput.xpBoost() ? XP_CONVERSION_BOOSTED : XP_CONVERSION;
            gambleResult.setXPGained(xpConverter.applyAsLong(gambledKeks));

            return gambleResult;
        }
    }
}
