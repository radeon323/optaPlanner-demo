package com.olshevchenko.optaplanner.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningListVariable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@NoArgsConstructor
@PlanningEntity
public class Route {
    private long id;
    private Car car;
    private Store store;

    @PlanningListVariable(valueRangeProviderRefs = {"routePointList"})
    private List<RoutePoint> routePointList;

    private Map<Long, RouteDistanceDuration> distanceDurationMap;

    public Route(long id, Car car, Store store) {
        this.id = id;
        this.car = car;
        this.store = store;
        this.routePointList = new ArrayList<>();
        this.distanceDurationMap = new ConcurrentHashMap<>();
    }

    public List<RoutePoint> getRouteForCar() {
        if (routePointList.isEmpty()) {
            return Collections.emptyList();
        }

        List<RoutePoint> route = new ArrayList<>();
        RoutePoint storePoint = new RoutePoint(0, store.getMapPoint(), 0);
        route.add(storePoint);
        route.addAll(routePointList);
        route.add(storePoint);

        return route;
    }

    public int getTotalDemand() {
        int totalDemand = 0;
        for (RoutePoint routePoint : routePointList) {
            totalDemand += routePoint.getAddressTotalWeight();
        }
        return totalDemand;
    }

    public RouteDistanceDuration getRouteDistanceDuration() {
        RouteDistanceDuration rdd = RouteDistanceDuration.builder().build();
        if (routePointList.isEmpty()) {
            return rdd;
        }
        MapPoint previousLocation = store.getMapPoint();
        distanceDurationMap.put(0L, rdd);
        for (RoutePoint routePoint : routePointList) {
            appendRouteDistanceDuration(rdd, previousLocation, routePoint.getMapPoint());
            distanceDurationMap.put(routePoint.getId(), rdd);
            previousLocation = routePoint.getMapPoint();
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
