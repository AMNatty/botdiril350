package com.botdiril.gamelogic.daily;

import com.botdiril.userdata.xp.XPRewards;
import com.botdiril.util.BotdirilRnd;

public class DailyRewards
{
    public static DailyResult generateRewards(int level)
    {
        var lvldata = XPRewards.getLevel(level);

        var xp = Math.round(BotdirilRnd.RANDOM.nextDouble() * (lvldata.getDailyMax() - lvldata.getDailyMin()) + lvldata.getDailyMin());

        var levelScalingCoins = Math.pow(level, 1.6);
        var coins = BotdirilRnd.RDG.nextLong(Math.round(200 + levelScalingCoins * 100), Math.round(300 + levelScalingCoins * 150));

        var levelScalingKeks = Math.pow(level, 1.8);
        var keks = BotdirilRnd.RDG.nextLong(Math.round(2000 + levelScalingKeks * 1000), Math.round(10000 + levelScalingKeks * 3000));

        var megakeks = 1;

        var keys = level > 100 ? 5 : 3;

        return new DailyResult(xp, coins, keks, megakeks, keys);
    }
}
