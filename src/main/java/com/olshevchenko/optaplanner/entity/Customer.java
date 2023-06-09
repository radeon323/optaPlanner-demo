package com.olshevchenko.optaplanner.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// RoutePoint
public class Customer {
    private long id;
    private MapPoint mapPoint;
    private int addressTotalWeight;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                '}';
    }
}
