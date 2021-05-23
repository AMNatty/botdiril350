package com.botdiril.gamelogic.mine;

import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ItemDrops;
import com.botdiril.userdata.item.ShopEntries;
import com.botdiril.userdata.items.Items;
import com.botdiril.userdata.items.scrolls.Scrolls;
import com.botdiril.util.BotdirilRnd;

import java.util.*;

public class MineAPI
{
    public static final double KITLESS_BONUS_THRESHOLD = 2;
    public static final double CHANCE_INSTAMINE = 0.4;
    public static final double KITLESS_MULTIPLIER = 1.5;

    private static final Map<Item, Double> boosters = new HashMap<>();

    static
    {
        boosters.put(Items.toolBox, 4.0);
        boosters.put(Items.oil, 8.0);
        boosters.put(Items.goldenOil, 20.0);
    }

    private static final Item[] minerals = { Items.coal, Items.iron, Items.copper, Items.gold, Items.platinum,
            Items.uranium, Items.kekium, Items.emerald, Items.diamond };

    private static final Item[] evilGems = { Items.redGem, Items.purpleGem, Items.blackGem };

    private static final Item[] goodGems = { Items.greenGem, Items.blueGem, Items.rainbowGem };

    public static Map<Item, Double> getBoosters()
    {
        return Collections.unmodifiableMap(boosters);
    }

    public static boolean canBoost(Item item)
    {
        return boosters.containsKey(item);
    }

    public static Double getBoostValue(Item item)
    {
         return boosters.get(item);
    }

    private static void generateMinerals(ItemDrops drops, double pickaxeBudget)
    {
        for (var mineral : minerals)
        {
            double sellVal = ShopEntries.getSellValue(mineral);
            var budgetSellValRatio = pickaxeBudget / sellVal;

            var scale = 0.5;

            var roll = BotdirilRnd.RDG.nextGamma(budgetSellValRatio / scale, scale);

            var count = (long) Math.floor(roll);

            if (count == 0)
            {
                continue;
            }

            drops.addItem(mineral, count);
        }
    }

    private static double getMeanGemChance(double pickaxeBudget, int gemTier)
    {
        var generalOccurenceCount = Math.max(Math.log(Math.max(Math.log(pickaxeBudget / 100.0) - (gemTier * 4), 1)), 0);
        var saturatedMax = Math.pow(0.5, gemTier + 1) * 5;
        var growthModifier = 1 - gemTier / 8;

        return Math.pow(generalOccurenceCount, growthModifier) * saturatedMax;
    }

    private static void generateGems(ItemDrops drops, double pickaxeBudget)
    {
        for (int gemTier = 0; gemTier < goodGems.length; gemTier++)
        {
            var mean = getMeanGemChance(pickaxeBudget, gemTier);

            if (mean == 0)
            {
                continue;
            }

            drops.addItem(goodGems[gemTier], BotdirilRnd.RDG.nextPoisson(mean));
        }

        for (int gemTier = 0; gemTier < evilGems.length; gemTier++)
        {
            var mean = getMeanGemChance(pickaxeBudget, gemTier);

            if (mean == 0)
            {
                continue;
            }

            drops.addItem(evilGems[gemTier], BotdirilRnd.RDG.nextPoisson(mean));
        }
    }

    public static MineResult mine(ItemDrops loot, MineInput inputData)
    {
        var pickaxe = inputData.getPickaxe();

        var chanceToBreak = pickaxe.getChanceToBreak();
        var rareDropMultiplier = pickaxe.getRareDropMultiplier();
        var pickaxeBudget = pickaxe.getPickaxeValue();

        var multiplierMap = new EnumMap<EnumMineMultiplier, Double>(EnumMineMultiplier.class);

        if (inputData.isCursedDoubleBreak())
        {
            chanceToBreak *= 2;
        }

        var useRepairKit = inputData.isPreferenceRepairKitEnabled();

        useRepairKit &= inputData.getRepairKitsAvailable() > 0;
        useRepairKit &= inputData.isPickaxeBreakable();

        var multiplier = 1.0;

        if (!useRepairKit && rareDropMultiplier >= KITLESS_BONUS_THRESHOLD)
        {
            rareDropMultiplier += 1;
            multiplier *= KITLESS_MULTIPLIER;
            multiplierMap.put(EnumMineMultiplier.MLT_KITLESS, KITLESS_MULTIPLIER);
        }

        var boosterItem = inputData.getBoosterItem();
        if (boosterItem != null)
        {
            var boostMultiplier = getBoostValue(boosterItem);

            var rareDropBoostRatio = 0.025;
            rareDropMultiplier *= 1.0 + boostMultiplier * rareDropBoostRatio;

            multiplier *= boostMultiplier;
            multiplierMap.put(EnumMineMultiplier.MLT_ITEM_BOOST, boostMultiplier);

            chanceToBreak /= boostMultiplier;
        }

        var randomModifier = BotdirilRnd.RDG.nextGaussian(1, 0.1);
        multiplier *= randomModifier;

        var level = inputData.getUserLevel();
        var levelModifier = 1 + Math.log1p(level) / 3.0 + Math.pow(Math.log10(level), 10.0) / 20_000;
        multiplier *= levelModifier;

        generateMinerals(loot, pickaxeBudget * multiplier);
        generateGems(loot, pickaxeBudget * randomModifier);

        loot.addItem(Items.timewarpCrystal, BotdirilRnd.RDG.nextPoisson(0.02 + 0.01 * rareDropMultiplier));
        loot.addItem(Items.keys, BotdirilRnd.RDG.nextPoisson(0.03));
        loot.addItem(Items.strangeMetal, BotdirilRnd.RDG.nextPoisson(0.1 + 0.015 * rareDropMultiplier));
        loot.addItem(Items.oil, BotdirilRnd.RDG.nextPoisson(0.027));
        loot.addItem(Items.goldenOil, BotdirilRnd.RDG.nextPoisson(0.003));
        loot.addItem(Scrolls.scrollOfLesserIntelligence, BotdirilRnd.RDG.nextPoisson(0.05 * rareDropMultiplier));
        loot.addItem(Scrolls.scrollOfIntelligence, BotdirilRnd.RDG.nextPoisson(0.005 * rareDropMultiplier));
        loot.addItem(Scrolls.scrollOfIntelligenceII, BotdirilRnd.RDG.nextPoisson(0.00003));
        loot.addItem(Items.ash, BotdirilRnd.RDG.nextPoisson(0.04 * rareDropMultiplier));
        loot.addItem(Items.prismaticDust, BotdirilRnd.RDG.nextPoisson(0.002));
        loot.addItem(Items.max, BotdirilRnd.RDG.nextPoisson(0.00001));

        var xp = Math.round(Math.pow(BotdirilRnd.RDG.nextGaussian(15, 2), 1.6 + rareDropMultiplier / 2.25));

        var lostItems = new ItemDrops();

        if (BotdirilRnd.rollChance(chanceToBreak) && inputData.isPickaxeBreakable())
        {
            if (useRepairKit)
            {
                lostItems.addItem(Items.repairKit);
            }
            else
            {
                lostItems.addItem(pickaxe);
            }
        }

        boolean instantlyRefreshed = inputData.isBlessedMiningSurge() && BotdirilRnd.rollChance(CHANCE_INSTAMINE);

        multiplierMap.put(EnumMineMultiplier.MLT_EXPERIENCE, levelModifier);
        multiplierMap.put(EnumMineMultiplier.MLT_RANDOM, randomModifier);

        return new MineResult(xp, lostItems, !useRepairKit, multiplierMap, instantlyRefreshed);
    }
}
