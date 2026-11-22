package com.amigoscode.streams.person;

import java.util.IntSummaryStatistics;

public record AgeStats(long count, int min, int max, double average, long sum) {

    static AgeStats from(IntSummaryStatistics s) {
        return new AgeStats(s.getCount(), s.getMin(), s.getMax(), s.getAverage(), s.getSum());
    }
}
