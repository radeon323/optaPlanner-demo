package com.olshevchenko.optaplanner.utils;

import com.olshevchenko.optaplanner.entity.MapPoint;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface DistanceCalculator {

    /**
     * Calculate the distance between {@code from} and {@code to} in meters.
     *
     * @param from starting location
     * @param to   target location
     * @return distance in meters
     */
    long calculateDistance(MapPoint from, MapPoint to);

    /**
     * Bulk calculation of distance.
     * Typically much more scalable than {@link #calculateDistance(MapPoint, MapPoint)} iteratively.
     *
     * @param fromLocations never null
     * @param toLocations   never null
     * @return never null
     */
    default Map<MapPoint, Map<MapPoint, Long>> calculateBulkDistance(
            Collection<MapPoint> fromLocations,
            Collection<MapPoint> toLocations) {
        return fromLocations.stream().collect(Collectors.toMap(
                Function.identity(),
                from -> toLocations.stream().collect(Collectors.toMap(
                        Function.identity(),
                        to -> calculateDistance(from, to)
                ))
        ));
    }

    /**
     * Calculate distance matrix for the given list of locations and assign distance maps accordingly.
     *
     * @param locationList
     */
    default void initDistanceMaps(Collection<MapPoint> locationList) {
        Map<MapPoint, Map<MapPoint, Long>> distanceMatrix = calculateBulkDistance(locationList, locationList);
        locationList.forEach(location -> location.setDistanceMap(distanceMatrix.get(location)));
    }
}
