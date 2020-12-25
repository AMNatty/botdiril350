package cz.tefek.botdiril.gamelogic.gamble;

public final class GambleInput
{
    private final long gambledKeks;
    private final boolean jackpotCursed;
    private final boolean xpBoost;
    private final long jackpotPool;
    private final long jackpotStored;

    public GambleInput(long gambledKeks, boolean xpBoost, boolean jackpotCursed, long jackpotPool, long jackpotStored)
    {
        this.gambledKeks = gambledKeks;
        this.xpBoost = xpBoost;
        this.jackpotCursed = jackpotCursed;
        this.jackpotPool = jackpotPool;
        this.jackpotStored = jackpotStored;
    }

    public long getJackpotPool()
    {
        return this.jackpotPool;
    }

    public long getJackpotStored()
    {
        return this.jackpotStored;
    }

    public boolean hasXPBoost()
    {
        return this.xpBoost;
    }

    public boolean isJackpotCursed()
    {
        return this.jackpotCursed;
    }

    public long getGambledKeks()
    {
        return this.gambledKeks;
    }
}
