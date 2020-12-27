package cz.tefek.botdiril.userdata.xp;

import java.util.HashMap;
import java.util.Map;

import cz.tefek.botdiril.userdata.item.ItemDrops;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.items.cardpack.CardPacks;
import cz.tefek.botdiril.userdata.items.crate.Crates;
import cz.tefek.botdiril.userdata.items.pickaxe.Pickaxes;
import cz.tefek.botdiril.util.BotdirilLog;

public class XPRewards
{
    private static final Map<Integer, LevelData> levels = new HashMap<>();
    private static final int maxLevel = 10000;

    public static void populate()
    {
        BotdirilLog.logger.info("Loading level data...");

        var cumulativeXP = 0L;

        for (int level = 1; level <= maxLevel; level++)
        {
            var xp = Math.round(1000 + Math.pow(level - 1, 1.3) * 100);
            var dailyMin = Math.round(500 + Math.pow(level, 1.275) * 15);
            var dailyMax = Math.round(500 + Math.pow(level, 1.275) * 50);
            var gambleFalloff = Math.max(Math.pow(0.9985, level), 5);
            var drawCount = 1 + Math.round(Math.floor(Math.log(level) / Math.log(2)));
            var loot = new ItemDrops();
            generateLootForLevel(level, loot);
            levels.put(level, new LevelData(xp, cumulativeXP, dailyMin, dailyMax, gambleFalloff, loot, (int) drawCount));
            cumulativeXP += xp;
        }
    }

    private static void generateLootForLevel(int level, ItemDrops loot)
    {
        loot.addItem(Items.keks, Math.round(Math.pow(level, 1.85) * 1000.0));

        if (level <= 5)
            loot.addItem(CardPacks.cardPackBasic);

        if (level < 10)
        {
            if (level % 3 == 1)
                loot.addItem(Pickaxes.pickaxeI);
            else if (level % 3 == 2)
                loot.addItem(Crates.crateBasic);
        }
        else if (level < 20)
        {
            if (level % 3 == 1)
                loot.addItem(Pickaxes.pickaxeII);
            else if (level % 3 == 2)
                loot.addItem(Crates.crateUncommon);
        }
        else if (level < 30)
        {
            if (level % 3 == 1)
                loot.addItem(Pickaxes.pickaxeII);
            else if (level % 3 == 2)
                loot.addItem(Crates.crateEpic);
        }
        else if (level < 40)
        {
            if (level % 3 == 1)
                loot.addItem(Pickaxes.pickaxeIV);
            else if (level % 3 == 2)
                loot.addItem(Crates.crateLegendary);
        }
        else if (level % 15 == 0)
            loot.addItem(Items.repairKit);

        if (level % 3 == 2)
        {
            if (level >= 50)
                loot.addItem(CardPacks.cardPackNormal, level / 50);
            else
                loot.addItem(CardPacks.cardPackBasic, 3);
        }

        if (level % 6 == 0)
            loot.addItem(CardPacks.cardPackGood, (level + 150) / 200);

        if (level % 3 == 0)
            loot.addItem(Items.keys);

        if (level % 10 == 0)
            loot.addItem(Items.strangeMetal, 1 + level / 500);

        if (level % 20 == 0)
            loot.addItem(Items.timewarpCrystal, 1);

        if (level % 30 == 0)
            loot.addItem(Items.oil, 3 + level / 1000);

        if (level % 40 == 0)
            loot.addItem(Items.goldenOil, 1 + level / 2500);
    }

    public static int getMaxLevel()
    {
        return maxLevel;
    }

    public static long getXPAtLevel(int level)
    {
        if (level == getMaxLevel())
        {
            return Long.MAX_VALUE;
        }

        return levels.get(level).getXP();
    }

    public static long getXPNeededForLvlUp(int level, long has)
    {
        return getXPAtLevel(level) - has;
    }

    public static long getCumulativeXPForLevel(int level)
    {
        return levels.get(level).getCumulativeXP();
    }

    public static ItemDrops getRewardsForLvl(int lvl)
    {
        return levels.get(lvl).getLoot();
    }

    public static double getProgress(int lvl, long has)
    {
        return (double) has / (double) getXPAtLevel(lvl);
    }

    public static LevelData getLevel(int level)
    {
        return levels.get(level);
    }
}
