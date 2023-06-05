package com.olshevchenko.optaplanner.repository;

import com.olshevchenko.optaplanner.entity.RoutingSolution;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class VehicleRoutingSolutionRepository {

    private RoutingSolution routingSolution;

    public Optional<RoutingSolution> solution() {
        return Optional.ofNullable(routingSolution);
    }

    public void update(RoutingSolution routingSolution) {
        this.routingSolution = routingSolution;
    }
}
