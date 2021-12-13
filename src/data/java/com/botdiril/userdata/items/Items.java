package com.botdiril.userdata.items;

import com.botdiril.userdata.EnumCurrency;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.item.*;
import com.botdiril.userdata.items.cardpack.CardPacks;
import com.botdiril.userdata.items.crate.Crates;
import com.botdiril.userdata.items.pickaxe.Pickaxes;
import com.botdiril.userdata.items.scrolls.Scrolls;

import java.util.List;

public class Items
{
    public static ItemCurrency xp;
    public static ItemCurrency coins;
    public static ItemCurrency keks;
    public static ItemCurrency tokens;
    public static ItemCurrency megakeks;
    public static ItemCurrency dust;
    public static ItemCurrency keys;

    public static Item redGem;
    public static Item greenGem;

    public static Item blueGem;
    public static Item purpleGem;

    public static Item rainbowGem;
    public static Item blackGem;

    public static Item timewarpCrystal;

    public static Item gemdiril;

    public static Item toolBox;
    public static Item ash;
    public static Item prismaticDust;

    public static Item wood;

    public static Item coal;
    public static Item iron;
    public static Item copper;
    public static Item gold;
    public static Item platinum;
    public static Item uranium;
    public static Item kekium;
    public static Item emerald;
    public static Item diamond;

    public static Item strangeMetal;

    public static Item repairKit;

    public static Item oil;
    public static Item goldenOil;
    public static Item prismaticOil;

    public static void load()
    {
        xp = new ItemCurrency(EnumCurrency.XP);
        coins = new ItemCurrency(EnumCurrency.COINS);
        keks = new ItemCurrency(EnumCurrency.KEKS);
        tokens = new ItemCurrency(EnumCurrency.TOKENS);
        megakeks = new ItemCurrency(EnumCurrency.MEGAKEKS);
        dust = new ItemCurrency(EnumCurrency.DUST);
        keys = new ItemCurrency(EnumCurrency.KEYS);

        CardPacks.load();

        redGem = new Item("infernalgem", Icons.GEM_RED, "Infernal Gem", "Unleash the fury upon your foes.");
        greenGem = new Item("peacegem", Icons.GEM_GREEN, "Peace Gem", "Avoid conflicts.");

        blueGem = new Item("equlibriumgem", Icons.GEM_BLUE, "Equlibrium Gem", "Remove any differences.");
        purpleGem = new Item("imbalancegem", Icons.GEM_PURPLE, "Imbalance Gem", "The source of imbalance permeating the universe.");

        rainbowGem = new Item("ordergem", Icons.GEM_RAINBOW, "Order Gem", "Natural enemy of chaos.");
        blackGem = new Item("chaosgem", Icons.GEM_BLACK, "Chaos Gem", "The source of all chaos.");

        timewarpCrystal = new Item("timewarpcrystal", Icons.RARE_TIMEWARP, "Timewarp Crystal", "Manipulate the spacetime!");

        strangeMetal = new Item("strangemetal", Icons.RARE_STRANGE_METAL, "Strange Metal", "A shard of some unexplored rare metal, possibly from space.");

        ash = new Item("ash", Icons.ITEM_MISC_ASH, "Ash", "Burnt organic material and chemical reaction leftovers.");
        ShopEntries.addDisenchant(ash, 1000);

        prismaticDust = new Item("prismaticdust", Icons.ITEM_MISC_PRISMATIC_DUST, "Prismatic Dust", "This rare powder sparkles with all colors of the rainbow.");

        oil = new Item("oil", Icons.RARE_OIL, "Oil", "This better not start a nuclear war...");
        ShopEntries.addCoinSell(oil, 800);

        goldenOil = new Item("goldenoil", Icons.RARE_GOLDENOIL, "Golden Oil", "Passively grants +1% bonus sell value, but also grants 0.5% chance for all golden oil barrels to explode, leaving you with no barrels and making you lose 15% from that trade.");

        prismaticOil = new Item("prismaticoil", Icons.RARE_PRISMATICOIL, "Prismatic Oil", "Passively grants +0.75% bonus sell value. Forever.");
        CraftingEntries.add(new Recipe(List.of(
            goldenOil.ofAmount(1),
            prismaticDust.ofAmount(1)
        ), 1, prismaticOil));

        wood = new Item("wood", Icons.COMMON_WOOD, "Wood", "Orgnatic material used in many fields, including construction.");
        ShopEntries.addCoinSell(wood, 20);

        coal = new Item("coal", Icons.MINE_COAL, "Coal", "A very common resource, used as a fuel.");
        ShopEntries.addCoinSell(coal, 3);

        iron = new Item("iron", Icons.MINE_IRON, "Iron", "One of the most common minerals, used pretty much everywhere.");
        ShopEntries.addCoinSell(iron, 8);

        copper = new Item("copper", Icons.MINE_COPPER, "Copper", "A soft metal, usually used in electric devices and alloys (such as bronze, brass, etc.).");
        ShopEntries.addCoinSell(copper, 60);

        gold = new Item("gold", Icons.MINE_GOLD, "Gold", "A rare metal with good properties, very popular in jewelry.");
        ShopEntries.addCoinSell(gold, 1_000);

        platinum = new Item("platinum", Icons.MINE_PLATINUM, "Platinum", "A very rare metal with very good properties.");
        ShopEntries.addCoinSell(platinum, 4_000);

        uranium = new Item("uranium", Icons.MINE_URANIUM, "Uranium", "An extremely dense metal used in nuclear physics.");
        ShopEntries.addCoinSell(uranium, 500);

        kekium = new Item("kekium", Icons.MINE_KEKIUM, "Kekium", "An exceptionally rare metal found in the deepest of mines.");
        ShopEntries.addCoinSell(kekium, 100_000);
        ShopEntries.addTokenBuy(kekium, 80_000);

        emerald = new Item("emerald", Icons.MINE_EMERALD, "Emerald", "An extremely rare gemstone. Emerald is a variant of beryl.");
        ShopEntries.addCoinSell(emerald, 1_000_000);

        diamond = new Item("diamond", Icons.MINE_DIAMOND, "Diamond", "A crystallic form of carbon, diamonds are the hardest known mineral known to humans.");
        ShopEntries.addCoinSell(diamond, 10_000_000);

        gemdiril = new Item("gemdiril", Icons.RARE_GEMDIRIL, "Gemdiril", "A very rare gem of an unknown value.");
        CraftingEntries.add(new Recipe(List.of(
            ItemPair.of(redGem, 256),
            ItemPair.of(greenGem, 256),
            ItemPair.of(blueGem, 192),
            ItemPair.of(purpleGem, 192),
            ItemPair.of(rainbowGem, 128),
            ItemPair.of(blackGem, 128),
            ItemPair.of(kekium, 1_234_567),
            ItemPair.of(prismaticDust, 1)),
            1, gemdiril));

        toolBox = new Item("toolbox", Icons.ITEM_MISC_TOOLBOX, "Tool Box", "I wonder what it's for.");
        ShopEntries.addCoinSell(toolBox, 4_000);
        CraftingEntries.add(new Recipe(List.of(ItemPair.of(wood, 500), ItemPair.of(ash, 10), ItemPair.of(greenGem, 12), ItemPair.of(strangeMetal, 2)), 1, toolBox));

        repairKit = new Item("repairkit", Icons.ITEM_MISC_REPAIR_KIT, "Repair Kit", "It's handy to have one of these at your disposal when handling fragile tools. Will be **automatically** used and consumed to avoid breaking a tool.");
        CraftingEntries.add(new Recipe(List.of(ItemPair.of(toolBox, 1), ItemPair.of(oil, 1)), 1, repairKit));

        Scrolls.load();

        Pickaxes.load();
        Crates.load();
    }
}
