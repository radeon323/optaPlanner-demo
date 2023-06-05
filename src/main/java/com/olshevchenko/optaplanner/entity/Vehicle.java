package com.olshevchenko.optaplanner.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningListVariable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@PlanningEntity
public class Vehicle {

    private long id;
    private int capacity;
    private Depot depot;

    @PlanningListVariable
    private List<Customer> customerList;

    public Vehicle(long id, int capacity, Depot depot) {
        this.id = id;
        this.capacity = capacity;
        this.depot = depot;
        this.customerList = new ArrayList<>();
    }

    /**
     * @return route of the vehicle
     */
    public List<MapPoint> getRoute() {
        if (customerList.isEmpty()) {
            return Collections.emptyList();
        }

        List<MapPoint> route = new ArrayList<>();

        route.add(depot.getLocation());
        for (Customer customer : customerList) {
            route.add(customer.getLocation());
        }
        route.add(depot.getLocation());

        return route;
    }

    public int getTotalDemand() {
        int totalDemand = 0;
        for (Customer customer : customerList) {
            totalDemand += customer.getDemand();
        }
        return totalDemand;
    }

    public long getTotalDistanceMeters() {
        if (customerList.isEmpty()) {
            return 0;
        }

        long totalDistance = 0;
        MapPoint previousLocation = depot.getLocation();

        for (Customer customer : customerList) {
            totalDistance += previousLocation.getDistanceTo(customer.getLocation());
            previousLocation = customer.getLocation();
        }
        totalDistance += previousLocation.getDistanceTo(depot.getLocation());

        return totalDistance;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                '}';
    }
}
