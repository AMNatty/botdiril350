package cz.tefek.botdiril.gamelogic.gamble;

import java.util.function.LongUnaryOperator;

import cz.tefek.botdiril.gamelogic.weighted.IWeightedRandom;

public class GambleAPI
{
    public static final LongUnaryOperator JACKPOT_POOL_CONVERSION = keks -> keks / 3;
    public static final LongUnaryOperator JACKPOT_STORE_CONVERSION = keks -> keks / 10;
    public static final LongUnaryOperator POOL_STORE_CONVERSION = keks -> keks / 10;

    public static final LongUnaryOperator XP_CONVERSION = keks -> Math.round(Math.pow(keks, 0.25));
    public static final LongUnaryOperator XP_CONVERSION_BOOSTED = keks -> Math.round(Math.pow(keks, 0.40));

    public static GambleResult roll(GambleInput gambleInput)
    {
        boolean missedJackpot = false;

        while (true)
        {
            var outcome = IWeightedRandom.choose(EnumGambleOutcome.class);

            if (gambleInput.isJackpotCursed() && outcome == EnumGambleOutcome.JACKPOT)
            {
                missedJackpot = true;
                continue;
            }

            var gambleResult = outcome.apply(gambleInput);
            gambleResult.setMissedJackpot(missedJackpot);

            if (outcome == EnumGambleOutcome.JACKPOT)
            {
                var stored = gambleInput.getJackpotStored();
                gambleResult.setJackpot(stored, POOL_STORE_CONVERSION.applyAsLong(stored));
            }
            else if (gambleResult.hasLost())
            {
                var lost = -gambleResult.getKekDifference();
                var pool = gambleInput.getJackpotPool();
                var stored = gambleInput.getJackpotStored();
                gambleResult.setJackpot(pool + JACKPOT_POOL_CONVERSION.applyAsLong(lost),
                    stored + JACKPOT_STORE_CONVERSION.applyAsLong(lost));
            }

            var gambledKeks = gambleInput.getGambledKeks();

            var xpConverter = gambleInput.hasXPBoost() ? XP_CONVERSION_BOOSTED : XP_CONVERSION;
            gambleResult.setXPGained(xpConverter.applyAsLong(gambledKeks));

            return gambleResult;
        }
    }
}
