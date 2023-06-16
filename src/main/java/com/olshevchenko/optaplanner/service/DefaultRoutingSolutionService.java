package com.olshevchenko.optaplanner.service;

import com.olshevchenko.optaplanner.entity.*;
import com.olshevchenko.optaplanner.repository.CarRepository;
import com.olshevchenko.optaplanner.repository.RoutingSolutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static com.olshevchenko.optaplanner.entity.SolutionStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultRoutingSolutionService {
    private final RoutingSolutionRepository repository;
    private final DefaultGraphhopperService graphhopperService;
    private final CarRepository carRepository;
    private final DefaultRouteCalculationService routeCalculationService;
    private final DefaultStoreService storeService;

    public RoutingSolution getSolution() {
        RoutingSolution routingSolution = repository.solution().orElseThrow(() -> new NoSuchElementException("No solution found."));

        if (routingSolution.getStatus() == RAW) {
            routingSolution.setStatus(INITIALIZING);

            List<Store> storeList = storeService.getMainStore();
            routingSolution.setStoreList(storeList);

            List<RoutePoint> routePointList = routingSolution.getRoutePointList();
            List<MapPoint> locationList = Stream.concat(
                            routePointList.stream().map(RoutePoint::getMapPoint),
                            storeList.stream().map(Store::getMapPoint))
                    .toList();
            routingSolution.setLocationList(locationList);

            List<Route> routeList = new ArrayList<>();
            List<Car> carList = carRepository.findAll();
            for (Car car : carList) {
                Route route = routeCalculationService.calculateRoutes(routingSolution, car);
                routeList.add(route);
            }
            routingSolution.setRouteList(routeList);

            update(routingSolution);
            CompletableFuture.runAsync(() -> {
                graphhopperService.initDistanceMaps(routingSolution.getLocationList());
                routingSolution.setStatus(COMPLETE);
                update(routingSolution);
            });
        }

        return routingSolution;
    }

    public void update(RoutingSolution solution) {
        for (Route route : solution.getRouteList()) {
            routeCalculationService.updateRoute(route);
        }
        repository.update(solution);
    }
}
