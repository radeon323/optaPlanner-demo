package com.olshevchenko.optaplanner.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Depot {
    private final long id;
    private final MapPoint location;
}
