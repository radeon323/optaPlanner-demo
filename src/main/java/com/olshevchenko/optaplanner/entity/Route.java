package com.olshevchenko.optaplanner.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningListVariable;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@NoArgsConstructor
@PlanningEntity
public class Route {
    private long id;
    private Car car;
    private Store store;

    @PlanningListVariable(valueRangeProviderRefs = {"customerList"})
    private List<Customer> customerList;


    public Route(long id, Car car, Store store) {
        this.id = id;
        this.car = car;
        this.store = store;
        this.customerList = new ArrayList<>();
    }

    public List<MapPoint> getRouteForCar() {
        if (customerList.isEmpty()) {
            return Collections.emptyList();
        }

        List<MapPoint> route = new ArrayList<>();

        route.add(store.getMapPoint());
        customerList.stream()
                .map(Customer::getMapPoint)
                .forEach(route::add);
        route.add(store.getMapPoint());

        return route;
    }

    public int getTotalDemand() {
        int totalDemand = 0;
        for (Customer customer : customerList) {
            totalDemand += customer.getAddressTotalWeight();
        }
        return totalDemand;
    }

    public RouteDistanceDuration getRouteDistanceDuration() {
        RouteDistanceDuration rdd = RouteDistanceDuration.builder().build();
        if (customerList.isEmpty()) {
            return rdd;
        }
        MapPoint previousLocation = store.getMapPoint();
        for (Customer customer : customerList) {
            appendRouteDistanceDuration(rdd, previousLocation, customer.getMapPoint());
            previousLocation = customer.getMapPoint();
        }
        appendRouteDistanceDuration(rdd, previousLocation, store.getMapPoint());
        return rdd;
    }

    private void appendRouteDistanceDuration(RouteDistanceDuration rdd, MapPoint previousLocation, MapPoint mapPoint) {
        Map<MapPoint, RouteDistanceDuration> routeDistanceDurationMap = previousLocation.getRouteDistanceDurationMap();
        RouteDistanceDuration distanceDuration;
        if (routeDistanceDurationMap == null) {
            distanceDuration = new RouteDistanceDuration(0,0);
        } else {
            distanceDuration = routeDistanceDurationMap.get(mapPoint);
        }
        long distance = distanceDuration.getDistance();
        long duration = distanceDuration.getDuration();

        rdd.setDistance(rdd.getDistance() + distance);
        rdd.setDuration(rdd.getDuration() + duration);
    }

    @Override
    public String toString() {
        long totalDuration = getRouteDistanceDuration().getDuration();
        String hm = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(totalDuration),
                TimeUnit.MILLISECONDS.toMinutes(totalDuration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalDuration)));
        return "Route{" +
                "car=" + car.getName() +
                ", Total weight=" + getTotalDemand() + "kg" +
                ", Total distance=" + getRouteDistanceDuration().getDistance() + "meters" +
                ", Total duration=" + hm +
                '}';
    }
}
