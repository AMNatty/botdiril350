package com.botdiril.gamelogic.woodcut;

import com.botdiril.userdata.item.ItemDrops;

public record WoodCutResult(
    EnumWoodCutOutcome outcome,
    EnumWoodCutOutcome.EnumWoodCutYield yieldModifier,
    long earnedWood,
    long earnedXP,
    ItemDrops earnedItems
)
{
}
