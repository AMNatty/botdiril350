package cz.tefek.botdiril.userdata.items;

import java.util.List;

import cz.tefek.botdiril.userdata.EnumCurrency;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.userdata.item.*;
import cz.tefek.botdiril.userdata.item.Recipe;
import cz.tefek.botdiril.userdata.items.cardpack.CardPacks;
import cz.tefek.botdiril.userdata.items.crate.Crates;
import cz.tefek.botdiril.userdata.items.pickaxe.Pickaxes;
import cz.tefek.botdiril.userdata.items.scrolls.*;

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
    public static Item trash;

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

    public static Item max;
    public static Item oil;
    public static Item goldenOil;

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

        timewarpCrystal = new Item("timewarpcrystal", Icons.GEM_TIMEWARP, "Timewarp Crystal", "Manipulate the spacetime!");

        strangeMetal = new Item("strangemetal", Icons.MINE_STRANGE_METAL, "Strange Metal", "A shard of some unexplored rare metal, possibly from space.");

        trash = new Item("trash", Icons.ITEM_TRASH, "Trash", "It's just trash, or is it?");
        ShopEntries.addDisenchant(trash, 1000);

        oil = new Item("oil", Icons.OTHER_OIL, "Oil", "This better not start a nuclear war...");
        ShopEntries.addCoinSell(oil, 800);

        goldenOil = new Item("goldenoil", Icons.OTHER_GOLDENOIL, "Golden Oil", "Passively grants +1% bonus sell value, but also grants 0.5% chance for all golden oil barrels to explode, leaving you with no barrels and making you lose 15% from that trade.");

        toolBox = new Item("toolbox", Icons.ITEM_SUSPICIOUS_METAL_BOX, "Tool Box", "I wonder what it's for.");
        ShopEntries.addCoinSell(toolBox, 4_000);
        CraftingEntries.add(new Recipe(List.of(new ItemPair(trash, 10), new ItemPair(greenGem, 12), new ItemPair(strangeMetal, 3)), 1, toolBox));

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

        gemdiril = new Item("gemdiril", Icons.GEM_GEMDIRIL, "Gemdiril", "A very rare gem of an unknown value.");
        CraftingEntries.add(new Recipe(List.of(
            new ItemPair(redGem, 256),
            new ItemPair(greenGem, 256),
            new ItemPair(blueGem, 192),
            new ItemPair(purpleGem, 192),
            new ItemPair(rainbowGem, 128),
            new ItemPair(blackGem, 128),
            new ItemPair(kekium, 1_234_567)),
            1, gemdiril));

        repairKit = new Item("repairkit", Icons.ITEM_REPAIR_KIT, "Repair Kit", "It's handy to have one of these at your disposal when handling fragile tools. Will be **automatically** used and consumed to avoid breaking a tool.");
        CraftingEntries.add(new Recipe(List.of(new ItemPair(toolBox, 1), new ItemPair(oil, 1)), 1, repairKit));

        Scrolls.load();

        max = new Item("Max", Icons.OTHER_MAX, "Max the Doggo", "The goodest boy on Earth.");

        Pickaxes.load();
        Crates.load();
    }
}
