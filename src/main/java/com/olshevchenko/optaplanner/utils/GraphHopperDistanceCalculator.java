package com.olshevchenko.optaplanner.utils;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.util.PointList;
import com.graphhopper.util.shapes.GHPoint3D;
import com.olshevchenko.optaplanner.configuration.GraphhopperProperties;
import com.olshevchenko.optaplanner.entity.RouteDistanceDuration;
import com.olshevchenko.optaplanner.entity.MapPoint;
import com.olshevchenko.optaplanner.exception.RoutingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class GraphHopperDistanceCalculator {
    public static final long METERS_PER_DEGREE = 111_000;
    private final GraphhopperProperties properties;
    private final GraphHopper hopper;

    public void initDistanceMaps(Collection<MapPoint> mapPoints) {
        long startTime = System.currentTimeMillis();
        log.info("Distance calculation started at {}", timeFormatter(startTime));
        Thread animationThread = new Thread(this::runAnimation);
        animationThread.start();
        Map<MapPoint, Map<MapPoint, RouteDistanceDuration>> distanceAndDurationMatrix = calculateBulkDistanceAndDuration(mapPoints, mapPoints);
        mapPoints.forEach(point -> point.setRouteDistanceDurationMap(distanceAndDurationMatrix.get(point)));
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        animationThread.interrupt();
        log.info("Distance calculation completed at {}. Calculation time: {} seconds", timeFormatter(endTime), executionTime/1000);
    }

    public RouteDistanceDuration calculateDistanceAndDuration(MapPoint from, MapPoint to) {
        ResponsePath path = getBestRoute(from, to);
        PointList points = path.getPoints();

        double totalDistance = 0.0;

        for (int i = 0; i < points.size() - 1; i++) {
            GHPoint3D startPoint = points.get(i);
            GHPoint3D endPoint = points.get(i + 1);
            double latitudeDiff = endPoint.lat - startPoint.lat;
            double longitudeDiff = endPoint.lon - startPoint.lon;
            double distance = Math.sqrt(latitudeDiff * latitudeDiff + longitudeDiff * longitudeDiff);
            totalDistance += distance;
        }

        return RouteDistanceDuration.builder()
                .distance((long) Math.ceil(totalDistance * METERS_PER_DEGREE))
                .duration(path.getTime())
                .build();
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

    private Map<MapPoint, Map<MapPoint, RouteDistanceDuration>> calculateBulkDistanceAndDuration(
            Collection<MapPoint> fromLocations,
            Collection<MapPoint> toLocations) {
        return fromLocations.parallelStream().collect(Collectors.toMap(
                Function.identity(),
                from -> toLocations.stream().collect(Collectors.toMap(
                        Function.identity(),
                        to -> calculateDistanceAndDuration(from, to)
                ))
        ));
    }



    //utils
    private String timeFormatter(long time) {
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(date);
    }

    private void runAnimation() {
        String[] FRAMES = {" ", ".", "..", "...", "....", "....."};
        int currentIndex = 0;

        try {
            while (!Thread.interrupted()) {
                String frame = FRAMES[currentIndex];
                System.out.print("\r" + "Distance calculation in progress" + frame);
                currentIndex = (currentIndex + 1) % FRAMES.length;
                Thread.sleep(300);
            }
        } catch (InterruptedException e) {
            System.out.println();
            // Animation interrupted
        }
    }


}
