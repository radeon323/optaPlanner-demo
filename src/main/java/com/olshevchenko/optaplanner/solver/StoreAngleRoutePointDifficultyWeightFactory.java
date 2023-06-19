package com.olshevchenko.optaplanner.solver;

import com.olshevchenko.optaplanner.entity.RoutePoint;
import com.olshevchenko.optaplanner.entity.RoutingSolution;
import com.olshevchenko.optaplanner.entity.Store;
import lombok.AllArgsConstructor;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;

import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.comparingLong;

import java.util.Comparator;

public class StoreAngleRoutePointDifficultyWeightFactory
        implements SelectionSorterWeightFactory<RoutingSolution, RoutePoint> {

    @Override
    public StoreAngleRoutePointDifficultyWeight createSorterWeight(RoutingSolution routingSolution,
                                                                   RoutePoint routePoint) {
        Store store = routingSolution.getStoreList().get(0);
        return new StoreAngleRoutePointDifficultyWeight(routePoint,
                routePoint.getMapPoint().getAngle(store.getMapPoint()),
                routePoint.getMapPoint().getDistanceTo(store.getMapPoint())
                        + store.getMapPoint().getDistanceTo(routePoint.getMapPoint()));
    }

    @AllArgsConstructor
    public static class StoreAngleRoutePointDifficultyWeight
            implements Comparable<StoreAngleRoutePointDifficultyWeight> {

        private static final Comparator<StoreAngleRoutePointDifficultyWeight> COMPARATOR = comparingDouble(
                (StoreAngleRoutePointDifficultyWeight weight) -> weight.storeAngle)
                .thenComparingLong(weight -> weight.depotRoundTripDistance) // Ascending (further from the depot are more difficult)
                .thenComparing(weight -> weight.routePoint, comparingLong(RoutePoint::getId));

        private final RoutePoint routePoint;
        private final double storeAngle;
        private final long depotRoundTripDistance;

        @Override
        public int compareTo(StoreAngleRoutePointDifficultyWeight other) {
            return COMPARATOR.compare(this, other);
        }
    }
}
