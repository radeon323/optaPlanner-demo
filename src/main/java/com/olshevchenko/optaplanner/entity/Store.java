package com.olshevchenko.optaplanner.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    private long id;
    private String name;
    private String address;
    private MapPoint mapPoint;
}
