package com.olshevchenko.optaplanner.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningListVariable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@PlanningEntity
public class Route {
    private long id;
    private double totalWeight;
    private Integer totalPoints;
    private Car car;
    private Store store;

    @PlanningListVariable(valueRangeProviderRefs = {"routePointList"})
    private List<RoutePoint> routePointList;

    private List<RoutePoint> fullRoutePoints;

    private Map<Long, RouteDistanceDuration> distanceDurationMap = new ConcurrentHashMap<>();

    public double getTotalWeight() {
        double totalWeight = BigDecimal.valueOf(routePointList.stream()
                        .map(RoutePoint::getAddressTotalWeight)
                        .collect(Collectors.summarizingDouble(amount -> amount)).getSum())
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
        return this.totalWeight = totalWeight;
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
                ", Total weight=" + totalWeight + "kg" +
                ", Total distance=" + getRouteDistanceDuration().getDistance() + "meters" +
                ", Total duration=" + hm +
                '}';
    }
}
