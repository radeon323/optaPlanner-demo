package com.olshevchenko.optaplanner.utils;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.util.PointList;
import com.graphhopper.util.shapes.GHPoint3D;
import com.olshevchenko.optaplanner.configuration.GraphhopperProperties;
import com.olshevchenko.optaplanner.entity.MapPoint;
import com.olshevchenko.optaplanner.exception.RoutingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class GraphHopperDistanceCalculator implements DistanceCalculator {

    public static final long METERS_PER_DEGREE = 111_000;

    private final GraphhopperProperties properties;

    private final GraphHopper hopper;

    @Override
    public long calculateDistance(MapPoint from, MapPoint to) {
        PointList points = getBestRoute(from, to).getPoints();
        double totalDistance = 0.0;

        for (int i = 0; i < points.size() - 1; i++) {
            GHPoint3D startPoint = points.get(i);
            GHPoint3D endPoint = points.get(i + 1);
            double latitudeDiff = endPoint.lat - startPoint.lat;
            double longitudeDiff = endPoint.lon - startPoint.lon;
            double distance = Math.sqrt(latitudeDiff * latitudeDiff + longitudeDiff * longitudeDiff);
            totalDistance += distance;
        }

        return (long) Math.ceil(totalDistance * METERS_PER_DEGREE);
    }

    private ResponsePath getBestRoute(MapPoint from, MapPoint to) {
        GHRequest request = new GHRequest(
                from.getLatitude(),
                from.getLongitude(),
                to.getLatitude(),
                to.getLongitude())
                .setProfile(properties.getProfiles().getVehicle())
                .setLocale(Locale.UK);
        GHResponse response = hopper.route(request);

        if (response.hasErrors()) {
            throw new RoutingException("No route from (" + from + ") to (" + to + ")", response.getErrors().get(0));
        }
        return response.getBest();
    }
}
