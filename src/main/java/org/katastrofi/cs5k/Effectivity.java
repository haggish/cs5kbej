package org.katastrofi.cs5k;

import com.google.common.collect.Range;

import java.time.LocalDateTime;

public class Effectivity {

    private final Range<LocalDateTime> timeRange;

    Effectivity(Range<LocalDateTime> timeRange) {
        this.timeRange = timeRange;
    }

    Range<LocalDateTime> timeRange() {
        return timeRange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Effectivity)) return false;

        Effectivity that = (Effectivity) o;

        if (!timeRange.equals(that.timeRange)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return timeRange.hashCode();
    }

    @Override
    public String toString() {
        return "Effectivity{" +
                "timeRange=" + timeRange +
                '}';
    }
}
