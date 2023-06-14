package com.olshevchenko.optaplanner.repository;

import com.olshevchenko.optaplanner.entity.RoutingSolution;
import com.olshevchenko.optaplanner.utils.GraphHopperDistanceCalculator;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Repository
public class RoutingSolutionRepository {

    private final GraphHopperDistanceCalculator distanceCalculator;
    private RoutingSolution routingSolution;

    public RoutingSolutionRepository(GraphHopperDistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    public Optional<RoutingSolution> solution() {
        if (routingSolution == null) {
            generateDemoData();
        }

        CompletableFuture.runAsync(() -> {
            if (!routingSolution.isDistancesInitialized()) {
                distanceCalculator.initDistanceMaps(routingSolution.getLocationList());
                routingSolution.setDistancesInitialized(true);
            }
        });
        return Optional.of(routingSolution);
    }

    public void update(RoutingSolution routingSolution) {
        this.routingSolution = routingSolution;
    }

    private void generateDemoData() {
        this.routingSolution = DemoDataBuilder.builder().build();
    }
}
