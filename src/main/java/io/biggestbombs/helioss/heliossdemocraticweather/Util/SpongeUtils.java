package io.biggestbombs.helioss.heliossdemocraticweather.Util;

import org.spongepowered.api.world.storage.WorldProperties;

public class SpongeUtils {

    /**
     * Turns the return value of {@link WorldProperties#getWorldTime()} into a day based tick. (0 - 24000)
     * @param ticks An un-normalized tick amount
     * @return The time of day in a MC world
     */
    public static long normalizeWorldTime(long ticks) {
        if (ticks < 0 || ticks > 23999) {
            // Normalise
            ticks = ticks % 24000;
        }

        return ticks;
    }
}
