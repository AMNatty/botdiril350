package com.botdiril.gamelogic.woodcut;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public enum EnumMoonPhase
{
    NEW_MOON("\uD83C\uDF11 New Moon"),
    WAXING_CRESCENT("\uD83C\uDF12 Waxing Crescent Moon"),
    FIRST_QUARTER("\uD83C\uDF13 First Crescent Moon"),
    WAXING_GIBBOUS("\uD83C\uDF14 Waxing Gibbous Moon"),
    FULL_MOON("\uD83C\uDF15 Full Moon"),
    WANING_GIBBOUS("\uD83C\uDF16 Waning Gibbous Moon"),
    LAST_QUARTER("\uD83C\uDF17 Last Quarter Moon"),
    WANING_CRESCENT("\uD83C\uDF18 Waning Crescent Moon");

    private final String name;

    EnumMoonPhase(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    /**
     * Only a rough approximation!
     * */
    public static EnumMoonPhase current()
    {
        var dt = LocalDateTime.now();
        return EnumMoonPhase.at(dt);
    }

    /**
     * Only a rough approximation!
     * */
    public static EnumMoonPhase at(LocalDateTime dt)
    {
        var nmdt = LocalDateTime.of(2000, 1, 6, 18, 13);
        var dayDifference = ChronoUnit.SECONDS.between(nmdt, dt) / (double) Duration.ofDays(1).toSeconds();
        var moonCycle = 29.53;
        var progress = Math.abs(dayDifference / moonCycle) % 1.0;

        var values = EnumMoonPhase.values();
        var phaseSamples = values.length * 2;

        var phaseNum = (int) Math.round(progress * phaseSamples) % phaseSamples;

        return switch (phaseNum) {
            case 0 -> NEW_MOON;
            case 1, 2, 3 -> WAXING_CRESCENT;
            case 4 -> FIRST_QUARTER;
            case 5, 6, 7 -> WAXING_GIBBOUS;
            case 8 -> FULL_MOON;
            case 9, 10, 11 -> WANING_GIBBOUS;
            case 12 -> LAST_QUARTER;
            case 13, 14, 15 -> WANING_CRESCENT;
            default -> null;
        };
    }
}
