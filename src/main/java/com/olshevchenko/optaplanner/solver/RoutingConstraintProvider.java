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
                totalDistance(factory),
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

    // ************************************************************************
    // Soft constraints
    // ************************************************************************

    protected Constraint totalDistance(ConstraintFactory factory) {
        return factory.forEach(Route.class)
                .penalizeLong(HardSoftLongScore.ONE_SOFT,
                        route -> route.getRouteDistanceDuration().getDistance())
                .asConstraint("distanceFromPreviousStandstill");
    }
}
