package cz.tefek.botdiril.userdata.items.pickaxe;

import com.google.gson.Gson;

import java.io.FileReader;
import java.util.List;

import cz.tefek.botdiril.MajorFailureException;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.userdata.item.*;
import cz.tefek.botdiril.userdata.item.Recipe;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.util.BotdirilFmt;

public class Pickaxes
{
    public static ItemPickaxe pickaxeI;
    public static ItemPickaxe pickaxeII;
    public static ItemPickaxe pickaxeIII;
    public static ItemPickaxe pickaxeIV;

    public static ItemPickaxe pickaxeV;
    public static ItemPickaxe pickaxeVI;
    public static ItemPickaxe pickaxeVII;
    public static ItemPickaxe pickaxeVIII;
    public static ItemPickaxe pickaxeIX;

    public static ItemPickaxe pickaxeX;
    public static ItemPickaxe pickaxeXI;
    public static ItemPickaxe pickaxeXII;
    public static ItemPickaxe pickaxeXIII;
    public static ItemPickaxe pickaxeXIV;

    public static ItemPickaxe pickaxeXV;
    public static ItemPickaxe pickaxeXVI;
    public static ItemPickaxe pickaxeXVII;
    public static ItemPickaxe pickaxeXVIII;
    public static ItemPickaxe pickaxeXIX;

    public static ItemPickaxe pickaxeXX;

    public static void load()
    {
        final String[] minorNames = { "Clumsy", "Lesser", "", "Improved", "Master" };
        final String[] majorNames = { "Basic", "Good", "Epic", "Legendary", "Arcane" };

        var gson = new Gson();

        try (var fr = new FileReader("assets/miningData/pickaxes.json"))
        {
            var pickaxeData = gson.fromJson(fr, PickaxeMetadata[].class);

            ItemPickaxe prevPickaxe = null;

            for (var pickaxe : pickaxeData)
            {
                var numeral = pickaxe.getRomanNumeral();
                var pickaxeField = Pickaxes.class.getField("pickaxe" + numeral);

                var id = "pickaxe" + numeral.toLowerCase();
                var name = "Pickaxe " + numeral;

                String icon;
                int pickaxeTier = pickaxe.getTier();

                icon = switch (pickaxeTier) {
                    case 0 -> Icons.PICKAXE_I;
                    case 1 -> Icons.PICKAXE_II;
                    case 2 -> Icons.PICKAXE_III;
                    case 3 -> Icons.PICKAXE_IV;
                    case 4 -> Icons.PICKAXE_V;
                    default -> throw new MajorFailureException("Pickaxe item data not found or malformed! Aborting: Missing pickaxe icon!");
                };

                var multiplier = pickaxe.getMultiplier();
                var budget = pickaxe.getBudget();
                var breakChance = pickaxe.getBreakChance();

                String description = """
                    %s %s Pickaxe
                    
                    **Mining value:**
                    %s
                    **Break chance:**
                    %s%%
                    **Rare drop multiplier:**
                    %s                    
                    """.formatted(minorNames[pickaxe.getMiniTier()], majorNames[pickaxe.getTier()],
                    BotdirilFmt.format(budget),
                    BotdirilFmt.format(breakChance * 100),
                    BotdirilFmt.format(multiplier))
                    .trim();

                var item = new ItemPickaxe(id, icon, name, multiplier, budget, breakChance, prevPickaxe, description);
                prevPickaxe = item;

                pickaxeField.set(null, item);
            }
        }
        catch (Exception e)
        {
            throw new MajorFailureException("Pickaxe item data not found or malformed!", e);
        }

        ShopEntries.addCoinBuy(pickaxeI, 500);
        CraftingEntries.add(new Recipe(List.of(new ItemPair(Items.coal, 80)), 1, pickaxeI));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeI, 3), new ItemPair(Items.iron, 40)), 1, pickaxeII));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeII, 2), new ItemPair(Items.copper, 40)), 1, pickaxeIII));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeIII, 1), new ItemPair(Items.uranium, 10)), 1, pickaxeIV));

        CraftingEntries.add(new Recipe(List.of(
            new ItemPair(Items.coal, 6_000),
            new ItemPair(Items.iron, 2_400),
            new ItemPair(Items.copper, 150),
            new ItemPair(Items.uranium, 15),
            new ItemPair(Items.gold, 5),
            new ItemPair(Items.redGem, 1)),
            1, pickaxeV));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeV, 3), new ItemPair(Items.copper, 80)), 1, pickaxeVI));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeVI, 3), new ItemPair(Items.uranium, 100)), 1, pickaxeVII));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeVII, 3), new ItemPair(Items.gold, 500)), 1, pickaxeVIII));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeVIII, 3), new ItemPair(Items.platinum, 400)), 1, pickaxeIX));

        CraftingEntries.add(new Recipe(List.of(
            new ItemPair(Items.coal, 512_000),
            new ItemPair(Items.iron, 256_000),
            new ItemPair(Items.copper, 32_768),
            new ItemPair(Items.uranium, 3_072),
            new ItemPair(Items.gold, 2_048),
            new ItemPair(Items.redGem, 2),
            new ItemPair(Items.purpleGem, 1)),
            1, pickaxeX));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeX, 3), new ItemPair(Items.gold, 600)), 1, pickaxeXI));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeXI, 3), new ItemPair(Items.platinum, 300)), 1, pickaxeXII));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeXII, 3), new ItemPair(Items.kekium, 200)), 1, pickaxeXIII));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeXIII, 3), new ItemPair(Items.emerald, 300)), 1, pickaxeXIV));

        CraftingEntries.add(new Recipe(List.of(
            new ItemPair(Items.coal, 128_000_000),
            new ItemPair(Items.iron, 48_912_000),
            new ItemPair(Items.copper, 5_242_880),
            new ItemPair(Items.uranium, 655_360),
            new ItemPair(Items.gold, 320_768),
            new ItemPair(Items.platinum, 64_000),
            new ItemPair(Items.redGem, 8),
            new ItemPair(Items.purpleGem, 4),
            new ItemPair(Items.blackGem, 1)),
            1, pickaxeXV));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeXV, 4), new ItemPair(Items.kekium, 1_600)), 1, pickaxeXVI));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeXVI, 5), new ItemPair(Items.emerald, 150)), 1, pickaxeXVII));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeXVII, 6), new ItemPair(Items.diamond, 200)), 1, pickaxeXVIII));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeXVIII, 7), new ItemPair(Items.gemdiril, 1)), 1, pickaxeXIX));

        CraftingEntries.add(new Recipe(List.of(new ItemPair(pickaxeXIX, 1), new ItemPair(Items.strangeMetal, 1_000)), 1, pickaxeXX));
    }
}
