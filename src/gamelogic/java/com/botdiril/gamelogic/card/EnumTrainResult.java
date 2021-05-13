package com.botdiril.gamelogic.card;

import com.botdiril.gamelogic.weighted.IWeightedRandom;

public enum EnumTrainResult implements IWeightedRandom<EnumTrainResult>
{
    BAD(0.3, 0.75),
    NORMAL(0.4, 1),
    GOOD(0.2, 1.2),
    VERY_GOOD(0.1, 1.5);

    private final double weight;
    private final double multiplier;

    EnumTrainResult(double weight, double multiplier)
    {
        this.weight = weight;
        this.multiplier = multiplier;
    }

    @Override
    public double getWeight()
    {
        return this.weight;
    }

    public double getMultiplier()
    {
        return this.multiplier;
    }
}
