package com.olshevchenko.optaplanner.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Builder
public class RouteDistanceDuration {
    private long distance;
    private long duration;

    @Override
    public String toString() {
        return "RouteDistanceDuration{" +
                "distance=" + distance + "meters" +
                ", duration=" + TimeUnit.MILLISECONDS.toMinutes(duration) + "minutes" +
                '}';
    }
}
