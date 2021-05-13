package com.botdiril.gamelogic.gamble;

@FunctionalInterface
public interface GambleFunction
{
    GambleResult gambleModifier(GambleInput gambleInput);
}
