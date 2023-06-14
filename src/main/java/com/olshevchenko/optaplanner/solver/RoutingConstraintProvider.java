package com.olshevchenko.optaplanner.solver;

import com.olshevchenko.optaplanner.entity.Route;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

public class RoutingConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[] {
                vehicleCapacity(factory),
                totalDuration(factory),
                totalDistance(factory),
                travelCost(factory)
        };
    }

    // ************************************************************************
    // Hard constraints
    // ************************************************************************

    protected Constraint vehicleCapacity(ConstraintFactory factory) {
        return factory.forEach(Route.class)
                .filter(route -> route.getTotalDemand() > route.getCar().getWeightCapacity())
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        route -> route.getTotalDemand() - route.getCar().getWeightCapacity())
                .asConstraint("vehicleCapacity");
    }

    protected Constraint totalDuration(ConstraintFactory factory) {
        return factory.forEach(Route.class)
                .filter(route -> route.getRouteDistanceDuration().getDuration() > 4 * 60 * 60 * 1000 + 30 * 60 * 1000)
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        route -> route.getRouteDistanceDuration().getDuration() - 4 * 60 * 60 * 1000 + 30 * 60 * 1000)
                .asConstraint("totalDuration");
    }

    // ************************************************************************
    // Soft constraints
    // ************************************************************************

    protected Constraint totalDistance(ConstraintFactory factory) {
        return factory.forEach(Route.class)
                .penalizeLong(HardSoftLongScore.ONE_SOFT,
                        route -> route.getRouteDistanceDuration().getDistance())
                .asConstraint("distanceFromPreviousStandstill");
    }

    protected Constraint travelCost(ConstraintFactory factory) {
        return factory.forEach(Route.class)
                .penalizeLong(HardSoftLongScore.ONE_SOFT,
                        route -> route.getCar().getTravelCost())
                .asConstraint("travelCost");
    }

}
