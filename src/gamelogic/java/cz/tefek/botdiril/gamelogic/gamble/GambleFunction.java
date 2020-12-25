package cz.tefek.botdiril.gamelogic.gamble;

@FunctionalInterface
public interface GambleFunction
{
    GambleResult gambleModifier(GambleInput gambleInput);
}
