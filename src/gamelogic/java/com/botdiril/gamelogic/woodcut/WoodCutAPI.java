package com.botdiril.gamelogic.woodcut;

import com.botdiril.gamelogic.GameAPI;
import com.botdiril.gamelogic.weighted.IWeightedRandom;
import com.botdiril.userdata.achievement.Achievement;
import com.botdiril.userdata.achievement.Achievements;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ItemDrops;
import com.botdiril.userdata.items.Items;
import com.botdiril.userdata.items.cardpack.CardPacks;
import com.botdiril.userdata.items.scrolls.Scrolls;
import com.botdiril.userdata.pools.LootPool;
import com.botdiril.userdata.pools.PoolDrawer;
import com.botdiril.userdata.xp.XPRewards;
import com.botdiril.util.BotdirilRnd;

import java.util.HashSet;

public class WoodCutAPI extends GameAPI
{
    private static final PoolDrawer<Item> SCROLL_POOL = new PoolDrawer<>();

    static
    {
        var scrolls = new LootPool<Item>();
        scrolls.add(Scrolls.scrollOfAbundance);
        scrolls.add(Scrolls.scrollOfBlessing);
        scrolls.add(Scrolls.scrollOfRefreshing);

        SCROLL_POOL.add(1, scrolls);
    }

    public static WoodCutResult woodCut(WoodCutInput input)
    {
        var outcome = IWeightedRandom.choose(EnumWoodCutOutcome.class);
        var multiplier = IWeightedRandom.choose(EnumWoodCutOutcome.EnumWoodCutYield.class);
        var level = input.playerLevel();
        long earnedWood = getWood(multiplier, level);
        long earnedXP = getXP(level);
        var drops = new ItemDrops();
        var achivements = new HashSet<Achievement>();

        if (input.blessed())
            earnedWood *= 4;

        if (input.cursed())
        {
            if (BotdirilRnd.rollChance(0.2))
                outcome = EnumWoodCutOutcome.BROKE_AXE;
            else
                earnedWood /= 2;
        }

        switch (outcome)
        {
            case OAK_FOREST,
                BIRCH_FOREST,
                PINE_FOREST,
                SPRUCE_FOREST,
                AURORA_BOREALIS,
                ANGRY_LOOKING_TREES,
                GOT_LOST,
                MET_AZOR:
                break;

            case GOT_CAUGHT:
                earnedWood /= 2;
                break;

            case FOUND_KEY:
                drops.addItem(Items.keys);
                break;

            case FOUND_MAX:
                achivements.add(Achievements.max);
                break;

            case MET_MURRAY:
                drops.addFromPool(SCROLL_POOL);
                break;

            case ABANDONED_MINE:
                if (BotdirilRnd.rollChance(0.20))
                    drops.addItem(Items.repairKit);
                else
                    drops.addItem(Items.toolBox);
                break;

            case CAUSED_WILDFIRE:
                multiplier = EnumWoodCutOutcome.EnumWoodCutYield.NOTHING;
                earnedWood = 0;
                drops.addItem(Items.ash, BotdirilRnd.rdg().nextLong(3, 8));
                break;

            case NULL_USER:
                drops.addItem(Items.prismaticDust);
                break;

            case BROKE_AXE:
                multiplier = EnumWoodCutOutcome.EnumWoodCutYield.NOTHING;
                earnedWood = 0;
                break;

            case NEARLY_KILLED_BY_WEREWOLF:
                if (input.blessed())
                {
                    outcome = EnumWoodCutOutcome.DEFEATED_THE_WEREWOLF;
                    drops.addItem(Items.timewarpCrystal, 3);
                    break;
                }

                multiplier = EnumWoodCutOutcome.EnumWoodCutYield.NOTHING;
                earnedWood = 0;
                break;

            case OLD_HOUSE:
                drops.addItem(CardPacks.cardPackNormal);
                break;
        }

        return new WoodCutResult(outcome, multiplier, earnedWood, earnedXP, drops, achivements);
    }

    private static long getWood(EnumWoodCutOutcome.EnumWoodCutYield multiplier, int level)
    {
        var base = 5;
        var levelMultiplier = Math.pow((10.0 + level) / 5.0, 1.9);
        var mean = base * multiplier.getModifier() * levelMultiplier;
        return Math.round(BotdirilRnd.rdg().nextGamma(mean, 0.5));
    }

    private static long getXP(int level)
    {
        var base = 100 + XPRewards.getXPAtLevel(level) * Math.pow(10, -Math.log10(20 + level) * 1.05) * 4.0;
        var amt = BotdirilRnd.rdg().nextGaussian(base, base / 32.0);
        return Math.round(amt);
    }
}
