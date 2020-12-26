package cz.tefek.botdiril.userdata.pools;

import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.items.cardpack.CardPacks;
import cz.tefek.botdiril.userdata.items.pickaxe.Pickaxes;
import cz.tefek.botdiril.userdata.items.scrolls.Scrolls;

public class CratePools
{
    public static final LootPool<Item> terribleRewards = new LootPool<>();
    public static final LootPool<Item> badRewards = new LootPool<>();
    public static final LootPool<Item> normalRewards = new LootPool<>();
    public static final LootPool<Item> goodRewards = new LootPool<>();
    public static final LootPool<Item> greatRewards = new LootPool<>();
    public static final LootPool<Item> amazingRewards = new LootPool<>();
    public static final LootPool<Item> hyperRewards = new LootPool<>();
    public static final LootPool<Item> goldenRewards = new LootPool<>();
    public static final LootPool<Item> ultraRewards = new LootPool<>();
    public static final LootPool<Item> godlyRewards = new LootPool<>();

    public static final LootPool<Item> staticRewards = new LootPool<>();

    public static final PoolDrawer<Item> basicCrate = new PoolDrawer<Item>()
        .add(5, terribleRewards)
        .add(20, badRewards)
        .add(16, normalRewards)
        .add(1, goodRewards);

    public static final PoolDrawer<Item> uncommonCrate = new PoolDrawer<Item>()
        .add(5, terribleRewards)
        .add(20, badRewards)
        .add(36, normalRewards)
        .add(8, goodRewards)
        .add(1, greatRewards);

    public static final PoolDrawer<Item> epicCrate = new PoolDrawer<Item>()
        .add(4, terribleRewards)
        .add(12, normalRewards)
        .add(1, goodRewards);

    public static final PoolDrawer<Item> legendaryCrate = new PoolDrawer<Item>()
        .add(100, goodRewards)
        .add(4, greatRewards)
        .add(1, amazingRewards);

    public static final PoolDrawer<Item> goldenCrate = new PoolDrawer<Item>()
        .add(80, goodRewards)
        .add(120, greatRewards)
        .add(8, amazingRewards)
        .add(2, hyperRewards)
        .add(1, goldenRewards);

    public static final PoolDrawer<Item> ultimateCrate = new PoolDrawer<Item>()
        .add(10, terribleRewards)
        .add(40, goodRewards)
        .add(20, greatRewards)
        .add(4, amazingRewards)
        .add(1, hyperRewards);

    public static final PoolDrawer<Item> hyperCrate = new PoolDrawer<Item>()
        .add(800, greatRewards)
        .add(400, amazingRewards)
        .add(60, hyperRewards)
        .add(10, ultraRewards)
        .add(1, godlyRewards);

    public static final PoolDrawer<Item> infernalCrate = new PoolDrawer<Item>()
        .add(400, amazingRewards)
        .add(80, hyperRewards)
        .add(20, ultraRewards)
        .add(1, godlyRewards);

    static
    {
        terribleRewards.add(Items.trash);

        badRewards.add(Pickaxes.pickaxeII);

        normalRewards.add(CardPacks.cardPackNormal);
        normalRewards.add(Scrolls.scrollOfLesserIntelligence);
        normalRewards.add(Items.redGem);
        normalRewards.add(Items.greenGem);

        goodRewards.add(Scrolls.scrollOfCombining);
        goodRewards.add(Pickaxes.pickaxeIV);
        goodRewards.add(Items.purpleGem);
        goodRewards.add(Items.blueGem);

        greatRewards.add(Items.toolBox);
        greatRewards.add(CardPacks.cardPackGood);
        greatRewards.add(Pickaxes.pickaxeV);

        amazingRewards.add(Items.emerald);
        amazingRewards.add(Scrolls.scrollOfAbundance);
        amazingRewards.add(CardPacks.cardPackVoid);
        amazingRewards.add(Scrolls.scrollOfIntelligence);
        amazingRewards.add(Items.diamond);
        amazingRewards.add(Items.rainbowGem);
        amazingRewards.add(Items.blackGem);
        amazingRewards.add(Pickaxes.pickaxeVI);

        hyperRewards.add(Pickaxes.pickaxeIX);
        hyperRewards.add(Scrolls.scrollOfIntelligenceMajor);

        goldenRewards.add(Pickaxes.pickaxeX);

        ultraRewards.add(Pickaxes.pickaxeXVII);

        godlyRewards.add(Pickaxes.pickaxeXVIII);
        godlyRewards.add(Scrolls.scrollOfIntelligenceII);
        godlyRewards.add(Items.gemdiril);
    }
}
