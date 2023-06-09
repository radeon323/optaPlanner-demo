package com.olshevchenko.optaplanner.solver;

import com.olshevchenko.optaplanner.entity.Car;
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
        return factory.forEach(Car.class)
                .filter(car -> car.getTotalDemand() > car.getTotalWeight())
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        car -> car.getTotalDemand() - car.getTotalWeight())
                .asConstraint("vehicleCapacity");
    }

    // ************************************************************************
    // Soft constraints
    // ************************************************************************

    protected Constraint totalDistance(ConstraintFactory factory) {
        return factory.forEach(Car.class)
                .penalizeLong(HardSoftLongScore.ONE_SOFT,
                        Car::getTotalDistanceMeters)
                .asConstraint("distanceFromPreviousStandstill");
    }
}
