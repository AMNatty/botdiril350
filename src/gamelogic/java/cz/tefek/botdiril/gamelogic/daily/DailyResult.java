package cz.tefek.botdiril.gamelogic.daily;

public class DailyResult
{
    private final long xp;
    private final long coins;
    private final long keks;
    private final long megaKeks;
    private final long keys;

    public DailyResult(long xp, long coins, long keks, long megaKeks, long keys)
    {
        this.xp = xp;
        this.coins = coins;
        this.keks = keks;
        this.megaKeks = megaKeks;
        this.keys = keys;
    }

    public long getXP()
    {
        return this.xp;
    }

    public long getCoins()
    {
        return this.coins;
    }

    public long getKeks()
    {
        return this.keks;
    }

    public long getMegaKeks()
    {
        return this.megaKeks;
    }

    public long getKeys()
    {
        return this.keys;
    }
}
