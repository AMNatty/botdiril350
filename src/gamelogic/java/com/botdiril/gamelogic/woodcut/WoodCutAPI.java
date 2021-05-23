package com.botdiril.gamelogic.woodcut;

import com.botdiril.gamelogic.weighted.IWeightedRandom;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ItemDrops;
import com.botdiril.userdata.items.Items;
import com.botdiril.userdata.items.cardpack.CardPacks;
import com.botdiril.userdata.items.scrolls.Scrolls;
import com.botdiril.userdata.pools.LootPool;
import com.botdiril.userdata.pools.PoolDrawer;
import com.botdiril.userdata.xp.XPRewards;
import com.botdiril.util.BotdirilRnd;

public class WoodCutAPI
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
        long earnedXP = getXP(multiplier, level);
        var drops = new ItemDrops();

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
                drops.addItem(Items.max);
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
                earnedWood = 0;
                drops.addItem(Items.ash, BotdirilRnd.RDG.nextLong(3, 8));
                break;

            case NULL_USER:
                drops.addItem(Items.prismaticDust);
                break;

            case NEARLY_KILLED_BY_WEREWOLF:
                multiplier = EnumWoodCutOutcome.EnumWoodCutYield.NOTHING;
                earnedWood = 0;
                break;

            case OLD_HOUSE:
                drops.addItem(CardPacks.cardPackNormal);
                break;
        }

        return new WoodCutResult(outcome, multiplier, earnedWood, earnedXP, drops);
    }

    private static long getWood(EnumWoodCutOutcome.EnumWoodCutYield multiplier, int level)
    {
        var base = 20;
        var levelMultiplier = Math.pow((5.0 + level) / 5.0, 2.0);
        var mean = base * multiplier.getModifier() * levelMultiplier;
        return Math.round(BotdirilRnd.RDG.nextGamma(mean, 0.5));
    }

    private static long getXP(EnumWoodCutOutcome.EnumWoodCutYield multiplier, int level)
    {
        var base = 100 + XPRewards.getXPAtLevel(level) * Math.pow(10, -Math.log10(20 + level) * 1.05) * 4.0;
        var amt = BotdirilRnd.RDG.nextGaussian(base, base / 32.0);
        return Math.round(amt * multiplier.getModifier());
    }
}
