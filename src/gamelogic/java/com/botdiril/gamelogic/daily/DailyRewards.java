package com.botdiril.gamelogic.daily;

import com.botdiril.userdata.xp.XPRewards;
import com.botdiril.util.BotdirilRnd;

public class DailyRewards
{
    public static DailyResult generateRewards(int level)
    {
        var lvldata = XPRewards.getLevel(level);

        var xp = Math.round(BotdirilRnd.random().nextDouble() * (lvldata.getDailyMax() - lvldata.getDailyMin()) + lvldata.getDailyMin());
        var rdg = BotdirilRnd.rdg();

        var levelScalingCoins = Math.pow(level, 1.6);
        var coins = rdg.nextLong(Math.round(200 + levelScalingCoins * 100), Math.round(300 + levelScalingCoins * 150));

        var levelScalingKeks = Math.pow(level, 1.8);
        var keks = rdg.nextLong(Math.round(2000 + levelScalingKeks * 1000), Math.round(10000 + levelScalingKeks * 3000));

        var keys = level > 100 ? 5 : 3;

        return new DailyResult(xp, coins, keks, keys);
    }
}
