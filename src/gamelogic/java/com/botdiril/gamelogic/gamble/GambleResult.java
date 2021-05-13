package com.botdiril.gamelogic.gamble;

public final class GambleResult
{
    private boolean hasLost;
    private boolean missedJackpot;
    private long kekDifference;
    private long xpGained;

    private boolean updateJackpotPool;
    private long newJackpotPool;
    private long newJackpotStored;

    private EnumGambleOutcome outcome;

    private GambleResult()
    {

    }

    public static GambleResult of(long kekDifference)
    {
        var gr = new GambleResult();
        gr.kekDifference = kekDifference;
        gr.hasLost = kekDifference < 0;

        return gr;
    }

    void setMissedJackpot(boolean missedJackpot)
    {
        this.missedJackpot = missedJackpot;
    }

    void setOutcome(EnumGambleOutcome outcome)
    {
        this.outcome = outcome;
    }

    public boolean hasMissedJackpot()
    {
        return this.missedJackpot;
    }

    public long getKekDifference()
    {
        return this.kekDifference;
    }

    public boolean hasLost()
    {
        return this.hasLost;
    }

    public EnumGambleOutcome getOutcome()
    {
        return this.outcome;
    }

    void setJackpot(long pool, long stored)
    {
        this.updateJackpotPool = true;
        this.newJackpotPool = pool;
        this.newJackpotStored = stored;
    }

    public long getNewJackpotPool()
    {
        return this.newJackpotPool;
    }

    public long getNewJackpotStored()
    {
        return this.newJackpotStored;
    }

    public boolean shouldUpdateJackpotPool()
    {
        return this.updateJackpotPool;
    }

    void setXPGained(long xpGained)
    {
        this.xpGained = xpGained;
    }

    public long getXPGained()
    {
        return this.xpGained;
    }
}
