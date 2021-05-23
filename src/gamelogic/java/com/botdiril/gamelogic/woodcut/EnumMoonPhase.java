package com.botdiril.gamelogic.woodcut;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public enum EnumMoonPhase
{
    NEW_MOON,
    WAXING_CRESCENT,
    FIRST_QUARTER,
    WAXING_GIBBOUS,
    FULL_MOON,
    WANING_GIBBOUS,
    LAST_QUARTER,
    WANING_CRESCENT;

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
