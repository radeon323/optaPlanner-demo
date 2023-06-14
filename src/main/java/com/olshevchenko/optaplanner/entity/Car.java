package com.olshevchenko.optaplanner.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Car {
    private int id;
    private String name;
    private boolean available;
    private boolean cooler;
    private String licencePlate;
    private int weightCapacity;
    private int travelCost;
    private boolean deleted = false;
}