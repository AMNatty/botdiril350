package cz.tefek.botdiril.gamelogic.card;

import java.util.LinkedHashMap;
import java.util.Map;

import cz.tefek.botdiril.gamelogic.weighted.IWeightedRandom;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.items.scrolls.Scrolls;

public class TrainAPI
{
    public static final Map<Item, Long> TRAINING_ITEMS = new LinkedHashMap<>();

    static
    {
        TRAINING_ITEMS.put(Scrolls.scrollOfLesserIntelligence, 1_000L);
        TRAINING_ITEMS.put(Scrolls.scrollOfIntelligence, 40_000L);
        TRAINING_ITEMS.put(Scrolls.scrollOfIntelligenceMajor, 500_000L);
        TRAINING_ITEMS.put(Scrolls.scrollOfIntelligenceII, 100_000_000L);

        TRAINING_ITEMS.put(Items.redGem, 200L);
        TRAINING_ITEMS.put(Items.greenGem, 200L);
        TRAINING_ITEMS.put(Items.blueGem, 5_000L);
        TRAINING_ITEMS.put(Items.purpleGem, 5_000L);
        TRAINING_ITEMS.put(Items.rainbowGem, 30_000L);
        TRAINING_ITEMS.put(Items.blackGem, 30_000L);

        TRAINING_ITEMS.put(Items.gemdiril, 50_000_000L);

        TRAINING_ITEMS.put(Items.goldenOil, 50_000L);
    }

    public static TrainResult roll(Item item, long amount)
    {
        var outcome = IWeightedRandom.choose(EnumTrainResult.class);
        return new TrainResult(outcome, Math.round(outcome.getMultiplier() * TRAINING_ITEMS.get(item) * amount));
    }
}
