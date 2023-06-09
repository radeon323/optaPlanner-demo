package com.olshevchenko.optaplanner.repository;

import com.olshevchenko.optaplanner.demo.DemoDataBuilder;
import com.olshevchenko.optaplanner.entity.RoutingSolution;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoutingSolutionRepository {

    private RoutingSolution routingSolution;

    public Optional<RoutingSolution> solution() {
        if (routingSolution == null) {
            generateDemoData();
        }
        return Optional.ofNullable(routingSolution);
    }

    public void update(RoutingSolution routingSolution) {
        this.routingSolution = routingSolution;
    }

    private void generateDemoData() {
        this.routingSolution = DemoDataBuilder.builder()
                .setMinDemand(50)
                .setMaxDemand(200)
                .build();
    }
}
