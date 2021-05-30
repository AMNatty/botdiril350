package com.botdiril.userdata.items.crate;

import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.item.*;
import com.botdiril.userdata.items.Items;
import com.botdiril.userdata.items.pickaxe.ItemPickaxe;
import com.botdiril.userdata.pools.CratePools;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.util.BotdirilRnd;
import com.google.common.base.Predicates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Crates
{
    public static ItemCrate crateBasic;
    public static ItemCrate crateUncommon;
    public static ItemCrate crateIron;
    public static ItemCrate crateEpic;
    public static ItemCrate crateLegendary;
    public static ItemCrate crateGolden;
    public static ItemCrate crateUltimate;
    public static ItemCrate crateHyper;
    public static ItemCrate crateGlitchy;
    public static ItemCrate crateInfernal;
    public static ItemCrate crateVoid;

    public static void load()
    {
        crateBasic = new ItemCrateGeneric("crate", Icons.CRATE_BASIC, "Crate",
            5, CratePools.basicCrate, "Just an ordinary crate, nothing to see here.");
        ShopEntries.addCoinBuy(crateBasic, 15_000);
        ShopEntries.addTokenBuy(crateBasic, 100);
        ShopEntries.addCoinSell(crateBasic, 3_000);


        crateUncommon = new ItemCrateGeneric("uncommoncrate", Icons.CRATE_UNCOMMON, "Uncommon Crate",
            7, CratePools.uncommonCrate, "A rare version of the Common Crate. Uncommon crates are, well... *uncommon*.");
        ShopEntries.addCoinBuy(crateUncommon, 115_200);
        ShopEntries.addCoinSell(crateUncommon, 57_600);
        ShopEntries.addTokenBuy(crateUncommon, 1_200);


        crateIron = new ItemKekCrate("ironcrate", Icons.CRATE_IRON, "Iron Crate",
            9, CratePools.uncommonCrate, 80_000, 120_000, "Despite the name, I don't think the crate is trash tier.");
        CraftingEntries.add(new Recipe(Arrays.asList(ItemPair.of(Items.iron, 1_000), ItemPair.of(crateBasic)), 1, crateIron));


        crateEpic = new ItemCrateGeneric("epiccrate", Icons.CRATE_EPIC, "Epic Crate",
            8, CratePools.basicCrate, "No. Just because it has epic in the name, it doesn't mean it contains Fortnite skins.");
        ShopEntries.addCoinBuy(crateEpic, 360_000);
        ShopEntries.addCoinSell(crateEpic, 72_000);
        ShopEntries.addTokenBuy(crateEpic, 4_500);


        crateLegendary = new ItemCrateGeneric("legendarycrate", Icons.CRATE_LEGENDARY, "Legendary Crate",
            8, CratePools.legendaryCrate, "Contrary to popular beliefs, this crate is actually worth opening.");
        ShopEntries.addCoinBuy(crateLegendary, 1_300_000);
        ShopEntries.addCoinSell(crateLegendary, 260_000);
        ShopEntries.addTokenBuy(crateLegendary, 10_000);


        crateGolden = new ItemKekCrate("goldencrate", Icons.CRATE_GOLDEN, "Golden Crate",
            12, CratePools.goldenCrate, 2_000_000, 5_000_000, "Ever wondered where all the keks you gamble away go? They are in these crates.");
        CraftingEntries.add(new Recipe(Arrays.asList(ItemPair.of(Items.gold, 1_000), ItemPair.of(crateLegendary)), 1, crateGolden));


        crateUltimate = new ItemCrateGeneric("ultimatecrate", Icons.CRATE_ULTIMATE, "Ultimate Crate",
            9, CratePools.ultimateCrate, "The ultimate evolution of crate opening experience.");
        ShopEntries.addCoinBuy(crateUltimate, 25_000_000);
        ShopEntries.addCoinSell(crateUltimate, 1_000_000);
        ShopEntries.addTokenBuy(crateUltimate, 200_000);


        crateVoid = new ItemCrateSimple("voidcrate", Icons.CRATE_VOID, "Void Crate", (co, id) -> {
            final int contents = 11;
            final double emptyChance = 0.45;

            if (!BotdirilRnd.rollChance(co.rdg, emptyChance))
                id.addFromPool(CratePools.infernalCrate, contents);

        }, "This crate in a quantum superposition of being empty or full at one time. The only way to check is to look inside.");
        ShopEntries.addCoinBuy(crateVoid, 10_000_000_000L);
        ShopEntries.addTokenBuy(crateVoid, 120_000_000);


        crateHyper = new ItemCrateGeneric("hypercrate", Icons.CRATE_HYPER, "Hyper Crate", 8, CratePools.hyperCrate, (co) -> {
            final double BLESS_CHANCE = 0.3;

            if (co.rdg.nextUniform(0, 1) < BLESS_CHANCE)
                Curser.bless(co);

        }, "This crate has been so overpowered that even when the universe reached heat death, this crate was overflowing with energy.");
        ShopEntries.addCoinBuy(crateHyper, 18_000_000_000L);
        ShopEntries.addTokenBuy(crateHyper, 200_000_000);


        crateInfernal = new ItemCrateGeneric("infernalcrate", Icons.CRATE_INFERNAL, "Infernal Crate", 8, CratePools.infernalCrate, (co) -> {
            final double CURSE_CHANCE = 0.3;

            if (BotdirilRnd.rdg().nextUniform(0, 1) < CURSE_CHANCE)
                Curser.curse(co);

        }, "This crate, forged in the depths of hell, will have the items that everyone would like to have. It might curse you, so be careful.");
        ShopEntries.addCoinBuy(crateInfernal, 15_000_000_000L);
        ShopEntries.addTokenBuy(crateInfernal, 100_000_000);


        crateGlitchy = new ItemCrateSimple("glitchycrate", Icons.CRATE_GLITCHY, "Glitchy Crate", (callObj, id) -> {
            var candidates = new ArrayList<>(Item.items());
            candidates.removeAll(List.of(Items.gemdiril));
            candidates.removeIf(Predicates.instanceOf(ItemPickaxe.class));

            for (int i = 0; i < 9; i++)
            {
                id.addItem(BotdirilRnd.choose(candidates));
            }
        }, "47 6f 6f 64 20 6a 6f 62 20 79 6f 75 20 63 61 6e 20 72 65 61 64 20 74 68 69 73 20 74 65 78 74 2e 20 4e 6f 77 20 6f 70 65 6e 20 74 68 65 20 63 72 61 74 65 2e");
        ShopEntries.addCoinBuy(crateGlitchy, 1_000_000_000_000L);
        ShopEntries.addTokenBuy(crateGlitchy, 1_500_000_000);
    }
}
