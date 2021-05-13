package com.botdiril.userdata.xp;

import com.botdiril.userdata.item.ItemDrops;

public class LevelData
{
    private final long xp;
    private final long cumulativeXP;
    private final long dailyMin;
    private final long dailyMax;
    private final double gambleFalloff;
    private final ItemDrops loot;
    private final int drawPotency;

    public LevelData(long xp, long cumulativeXp, long dailyMin, long dailyMax, double gambleFalloff, ItemDrops loot, int drawPotency)
    {
        this.xp = xp;
        this.cumulativeXP = cumulativeXp;
        this.dailyMin = dailyMin;
        this.dailyMax = dailyMax;
        this.gambleFalloff = gambleFalloff;
        this.loot = loot;
        this.drawPotency = drawPotency;
    }

    public long getXP()
    {
        return xp;
    }

    public long getCumulativeXP()
    {
        return cumulativeXP;
    }

    public long getDailyMin()
    {
        return dailyMin;
    }

    public long getDailyMax()
    {
        return dailyMax;
    }

    public double getGambleFalloff()
    {
        return gambleFalloff;
    }

    public ItemDrops getLoot()
    {
        return loot;
    }

    public int getDrawPotency()
    {
        return drawPotency;
    }
}
