package cz.tefek.botdiril.userdata.timers;

import java.util.concurrent.TimeUnit;

import cz.tefek.botdiril.userdata.ItemLookup;

public enum EnumTimer
{
    DAILY("daily", "Daily", TimeUnit.HOURS.toMillis(22)),
    MINE("mine", "Mine", TimeUnit.MINUTES.toMillis(2)),
    DRAW("draw", "Draw", TimeUnit.MINUTES.toMillis(6)),
    STEAL("steal", "Steal / Nuke", TimeUnit.HOURS.toMillis(1)),
    PAYOUT("payout", "Payout", TimeUnit.MINUTES.toMillis(1)),
    GAMBLE_XP("gamblexp", "Gambling XP", TimeUnit.SECONDS.toMillis(75)),
    TRAIN("cardtrain", "Card Training", TimeUnit.SECONDS.toMillis(30));

    private final int id;
    private final String name;
    private final String localizedName;
    private final long timeMs;

    EnumTimer(String name, String niceName, long timeMs)
    {
        this.name = "timer_" + name;
        this.timeMs = timeMs;
        this.id = ItemLookup.make(this.name);
        this.localizedName = niceName;
    }

    public int getID()
    {
        return this.id;
    }

    public String getLocalizedName()
    {
        return this.localizedName;
    }

    public String getName()
    {
        return this.name;
    }

    public long getTimeOffset()
    {
        return this.timeMs;
    }
}
