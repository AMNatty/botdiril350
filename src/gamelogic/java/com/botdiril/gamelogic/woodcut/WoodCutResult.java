package com.botdiril.gamelogic.woodcut;

import com.botdiril.userdata.achievement.Achievement;
import com.botdiril.userdata.item.ItemDrops;

import java.util.Set;

public record WoodCutResult(
    EnumWoodCutOutcome outcome,
    EnumWoodCutOutcome.EnumWoodCutYield yieldModifier,
    long earnedWood,
    long earnedXP,
    ItemDrops earnedItems,
    Set<Achievement> achievements
)
{
}
