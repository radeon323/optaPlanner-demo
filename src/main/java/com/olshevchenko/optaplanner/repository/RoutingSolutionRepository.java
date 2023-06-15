package com.olshevchenko.optaplanner.repository;

import com.olshevchenko.optaplanner.entity.RoutingSolution;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoutingSolutionRepository {

    private RoutingSolution routingSolution;

    public Optional<RoutingSolution> solution() {
        if (routingSolution == null) {
            this.routingSolution = DemoDataBuilder.builder().build();
        }
        return Optional.of(routingSolution);
    }

    public void update(RoutingSolution routingSolution) {
        this.routingSolution = routingSolution;
    }

}
