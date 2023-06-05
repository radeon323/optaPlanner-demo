package com.olshevchenko.optaplanner.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private long id;
    private MapPoint location;
    private int demand;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                '}';
    }
}
