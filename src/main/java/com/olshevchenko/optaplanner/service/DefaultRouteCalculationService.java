package com.olshevchenko.optaplanner.service;

import com.olshevchenko.optaplanner.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultRouteCalculationService {

    public Route calculateRoutes(RoutingSolution routingSolution, Car car) {
        Store store = routingSolution.getStoreList().get(0);

        Route route = new Route();
        route.setId(car.getId());
        route.setCar(car);
        route.setStore(store);

        List<RoutePoint> routePoints = route.getRoutePointList() == null ? new ArrayList<>() : route.getRoutePointList();

        route.setTotalPoints(routePoints.size());
        route.setRoutePointList(routePoints);

        updateRoute(route);

//        ResponsePath routePath = graphhopperService.getRoute(mapPoints);
//        route.setDistance(BigDecimal.valueOf(routePath.getDistance() / 1000)
//                .setScale(2, RoundingMode.HALF_UP)
//                .doubleValue());
//
//        route.setStartTime(routingSolution.getDepotStartTime());
//        route.setFinishTime(routingSolution.getDepotFinishTime());
//        route.setEstimatedTime(Duration.between(
//                        routingSolution.getDepotStartTime(),
//                        routingSolution.getDepotFinishTime())
//                .toMinutes());
        return route;
    }

    public void updateRoute(Route route) {
        updateFullRoutePoints(route);
//        updateTotalWeight(route);
    }

    private void updateFullRoutePoints(Route route) {
        List<RoutePoint> routePointList = new ArrayList<>();
        List<RoutePoint> routePoints = route.getRoutePointList();
        RoutePoint storePoint = new RoutePoint(0, route.getStore().getMapPoint(), 0);
        routePointList.add(storePoint);
        routePointList.addAll(routePoints);
        routePointList.add(storePoint);
        route.setFullRoutePoints(routePointList);
    }

    private void updateTotalWeight(Route route) {
        double totalWeight = BigDecimal.valueOf(route.getRoutePointList().stream()
                        .map(RoutePoint::getAddressTotalWeight)
                        .collect(Collectors.summarizingDouble(amount -> amount)).getSum())
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
        route.setTotalWeight(totalWeight);
    }


}
