package com.olshevchenko.optaplanner.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoutePoint {
    private long id;
    private MapPoint mapPoint;
    private int addressTotalWeight;

    @Override
    public String toString() {
        return "RoutePoint{" +
                "id=" + id +
                '}';
    }
}
