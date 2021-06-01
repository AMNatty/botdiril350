package com.botdiril.gamelogic.woodcut;

import com.botdiril.gamelogic.weighted.IWeightedRandom;

import java.util.function.BooleanSupplier;

public enum EnumWoodCutOutcome implements IWeightedRandom<EnumWoodCutOutcome>
{
    // Nothing weird happened
    OAK_FOREST(4.5),
    // Nothing weird happened
    SPRUCE_FOREST(4.5),
    // Nothing weird happened
    BIRCH_FOREST(4.5),
    // Nothing weird happened
    PINE_FOREST(4.5),
    // At this time of year? At this time of the day? In this part of the country?
    AURORA_BOREALIS(0.2),
    // Nothing weird happened
    ANGRY_LOOKING_TREES(0.9),
    // Teleported into a random direction, doubling cooldown BUT also getting Prismatic Dust
    NULL_USER(0.1),
    // Found a dog, decided to take it home; EXTREMELY RARE
    FOUND_MAX(0.0005),
    // Found a key
    FOUND_KEY(1.0),
    // Met a spectral wolf that lead you back home faster, halved timer
    MET_AZOR(0.5, () -> EnumMoonPhase.current() == EnumMoonPhase.NEW_MOON),
    // Met a gnome that gave you a random scroll
    MET_MURRAY(0.2),
    // Got caught, wood yield cut in half
    GOT_CAUGHT(0.8),
    // Got lost, doubled timer
    GOT_LOST(0.9),
    // Escaped near death, losing all wood and getting cursed, can only happen during full moon
    NEARLY_KILLED_BY_WEREWOLF(20.0, () -> /*EnumMoonPhase.current() == EnumMoonPhase.FULL_MOON*/ true),
    // Killed the werewolf, gained AMAZING loot, conditioned by being blessed
    DEFEATED_THE_WEREWOLF(0.01, () -> false),
    // Conditioned by being cursed
    BROKE_AXE(4.0, () -> false),
    // Caused a wildfire by an accident, all that was left is Ash
    CAUSED_WILDFIRE(0.5),
    // Found an abandonned mine with supplies, gives either a toolbox or a repair kit
    ABANDONED_MINE(0.8),
    // Found an old house, gives a card pack
    OLD_HOUSE(0.9);

    private final double weight;
    private final BooleanSupplier condition;

    EnumWoodCutOutcome(double weight)
    {
        this(weight, () -> true);
    }

    EnumWoodCutOutcome(double weight, BooleanSupplier condition)
    {
        this.weight = weight;
        this.condition = condition;
    }

    @Override
    public boolean isApplicable()
    {
        return this.condition.getAsBoolean();
    }

    @Override
    public double getWeight()
    {
        return this.weight;
    }

    public enum EnumWoodCutYield implements IWeightedRandom<EnumWoodCutYield>
    {
        NOTHING(0.0001, 0.0),
        TERRIBLE(1.0, 0.5),
        BAD(5.0, 0.7),
        NORMAL(20.0, 1),
        GOOD(10.0, 1.2),
        GREAT(3.0, 2.0);

        private final double weight;
        private final double modifier;

        EnumWoodCutYield(double weight, double modifier)
        {
            this.weight = weight;
            this.modifier = modifier;
        }

        @Override
        public double getWeight()
        {
            return this.weight;
        }

        public double getModifier()
        {
            return this.modifier;
        }
    }

}
