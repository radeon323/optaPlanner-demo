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
public class Car {

    private long id;
    private int capacity;
    private Store store;

    @PlanningListVariable
    private List<Customer> customerList;

    public Car(long id, int capacity, Store store) {
        this.id = id;
        this.capacity = capacity;
        this.store = store;
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

        route.add(store.getMapPoint());
        for (Customer customer : customerList) {
            route.add(customer.getMapPoint());
        }
        route.add(store.getMapPoint());

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
        MapPoint previousLocation = store.getMapPoint();

        for (Customer customer : customerList) {
            totalDistance += previousLocation.getDistanceTo(customer.getMapPoint());
            previousLocation = customer.getMapPoint();
        }
        totalDistance += previousLocation.getDistanceTo(store.getMapPoint());

        return totalDistance;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                '}';
    }
}
