package cz.tefek.botdiril.userdata.items.scrolls;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.userdata.item.*;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.tempstat.Curser;
import cz.tefek.botdiril.userdata.timers.EnumTimer;
import cz.tefek.botdiril.userdata.xp.XPRewards;
import cz.tefek.botdiril.util.BotdirilFmt;

public class Scrolls
{
    public static Item scrollOfCombining;

    public static ItemScroll scrollOfLesserIntelligence;
    public static ItemScroll scrollOfIntelligence;
    public static ItemScroll scrollOfRefreshing;
    public static ItemScroll scrollOfIntelligenceMajor;
    public static ItemScroll scrollOfIntelligenceII;
    public static ItemScroll scrollOfAbundance;
    public static ItemScroll scrollOfSwapping;
    public static ItemScroll scrollOfBlessing;

    public static void load()
    {
        scrollOfCombining = new Item("scrollofcombining", Icons.SCROLL, "Scroll of Combining", "Crafting ingredient for some magical recipes.");
        CraftingEntries.add(new Recipe(List.of(ItemPair.of(Items.blueGem, 1)), 2, Scrolls.scrollOfCombining));

        final long LESSER_INTELLIGENCE_XP = 2_000;
        scrollOfLesserIntelligence = new ItemScroll("lesserscrollofintelligence", Icons.SCROLL, "Lesser Scroll of Intelligence", (co, amount) -> {
            co.respond("You read the **%d %s**... **[+%s XP]**".formatted(amount, scrollOfLesserIntelligence.inlineDescription(), BotdirilFmt.format(amount * LESSER_INTELLIGENCE_XP)));
            co.ui.addXP(co, LESSER_INTELLIGENCE_XP * amount);
        }, "Use to instantly gain **%s XP**.".formatted(BotdirilFmt.format(LESSER_INTELLIGENCE_XP)));

        final long INTELLIGENCE_XP = 40_000;
        scrollOfIntelligence = new ItemScroll("scrollofintelligence", Icons.SCROLL_RARE, "Scroll of Intelligence", (co, amount) -> {
            co.respond("You read the **%d %s**... **[+%s XP]**".formatted(amount, scrollOfIntelligence.inlineDescription(), BotdirilFmt.format(amount * INTELLIGENCE_XP)));
            co.ui.addXP(co, INTELLIGENCE_XP * amount);
        }, "Use to instantly gain **%s XP**.".formatted(BotdirilFmt.format(INTELLIGENCE_XP)));
        CraftingEntries.add(new Recipe(Arrays.asList(
            ItemPair.of(Scrolls.scrollOfLesserIntelligence, 5),
            ItemPair.of(Items.greenGem, 30),
            ItemPair.of(Scrolls.scrollOfCombining, 5),
            ItemPair.of(Items.trash, 32)),
            1, scrollOfIntelligence));

        scrollOfRefreshing = new ItemScroll("scrollofrefreshing", Icons.SCROLL, "Scroll of Refreshing", (co, amount) -> {
            co.ui.resetTimer(EnumTimer.DAILY);
            co.respond("You refreshed your **daily** command cooldown!");
        }, "Instantly refresh the cooldown of your **daily** loot.");
        CraftingEntries.add(new Recipe(Arrays.asList(
            ItemPair.of(Items.timewarpCrystal, 3),
            ItemPair.of(Items.greenGem, 12),
            ItemPair.of(Items.blueGem, 3),
            ItemPair.of(scrollOfCombining, 5),
            ItemPair.of(Items.trash, 24)),
            1, scrollOfRefreshing));

        final long ABUNDANCE_LIMIT = 5_000_000;
        scrollOfAbundance = new ItemScroll("scrollofabundance", Icons.SCROLL_RARE, "Scroll of Abundance", (co, amount) -> {
            var diff = Math.round(Math.min(co.ui.getCoins() * Math.pow(2, amount - 1), ABUNDANCE_LIMIT * amount));
            co.ui.addCoins(diff);
            co.respond(MessageFormat.format("You now have **{0}** more {1}.", diff, Icons.COIN));
        }, "Use to instantly double your %s, %s at most.".formatted(Icons.COIN, BotdirilFmt.format(ABUNDANCE_LIMIT)));

        final long MAJOR_INTELLIGENCE_XP = 500_000;
        scrollOfIntelligenceMajor =  new ItemScroll("majorscrollofintelligence", Icons.SCROLL_RARE, "Major Scroll of Intelligence", (co, amount) -> {
            co.respond("You read the **%d %s**... **[+%s XP]**".formatted(amount, scrollOfIntelligenceMajor.inlineDescription(), BotdirilFmt.format(amount * MAJOR_INTELLIGENCE_XP)));
            co.ui.addXP(co, MAJOR_INTELLIGENCE_XP * amount);
        }, "Use to instantly gain **%s XP**.".formatted(BotdirilFmt.format(MAJOR_INTELLIGENCE_XP)));
        CraftingEntries.add(new Recipe(Arrays.asList(
            ItemPair.of(Scrolls.scrollOfLesserIntelligence, 20),
            ItemPair.of(Scrolls.scrollOfCombining, 50),
            ItemPair.of(Items.blueGem, 25),
            ItemPair.of(Items.rainbowGem, 10),
            ItemPair.of(Items.strangeMetal, 3)),
            1, scrollOfIntelligenceMajor));

        scrollOfIntelligenceII = new ItemScroll("scrollofintelligenceii", Icons.SCROLL_UNIQUE, "Scroll of Intelligence II", (co, amount) -> {
            var xpNeeded = 0L;

            for (int i = 0; i < amount; i++)
            {
                var level = co.ui.getLevel() + i;

                if (level == XPRewards.getMaxLevel())
                    break;

                xpNeeded += XPRewards.getXPNeededForLvlUp(level, i == 0 ? co.ui.getXP() : 0);
            }

            co.ui.addXP(co, xpNeeded);
        }, "Use to instantly **level up**.");

        scrollOfSwapping = new ItemScroll("scrollofswapping", Icons.SCROLL, "Scroll of Swapping", (co, amount) -> {
            co.ui.addItem(scrollOfSwapping, amount - 1);

            // red -> green
            // green -> red
            var red = co.ui.howManyOf(Items.redGem);
            var green = co.ui.howManyOf(Items.greenGem);
            co.ui.setItem(Items.redGem, green);
            co.ui.setItem(Items.greenGem, red);

            // blue -> purple
            // purple -> blue
            var blue = co.ui.howManyOf(Items.blueGem);
            var purple = co.ui.howManyOf(Items.purpleGem);
            co.ui.setItem(Items.purpleGem, blue);
            co.ui.setItem(Items.blueGem, purple);

            // black -> rainbow
            // rainbow -> black
            var black = co.ui.howManyOf(Items.blackGem);
            var rainbow = co.ui.howManyOf(Items.rainbowGem);
            co.ui.setItem(Items.rainbowGem, black);
            co.ui.setItem(Items.blackGem, rainbow);

            co.respond("**You reversed your gems!**");

        }, "Reverse your gems!");
        CraftingEntries.add(new Recipe(List.of(
            ItemPair.of(Items.timewarpCrystal),
            ItemPair.of(Scrolls.scrollOfCombining)),
            1, scrollOfSwapping));

        scrollOfBlessing = new ItemScroll("scrollofblessing", Icons.SCROLL_RARE, "Scroll of Blessing", (co, amount) -> {
            for (int i = 0; i < amount; i++)
                Curser.bless(co);

        }, "You feel blessed.");
        CraftingEntries.add(new Recipe(Arrays.asList(
            ItemPair.of(Items.rainbowGem, 10),
            ItemPair.of(Items.timewarpCrystal)),
            1, scrollOfBlessing));
    }
}
