package com.botdiril.gamelogic.gamble;

public record GambleInput(long gambledKeks,
                          boolean xpBoost,
                          boolean jackpotCursed,
                          long jackpotPool,
                          long jackpotStored)
{
}
