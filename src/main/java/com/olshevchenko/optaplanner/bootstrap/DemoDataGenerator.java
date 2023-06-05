package com.olshevchenko.optaplanner.bootstrap;

import com.olshevchenko.optaplanner.entity.MapPoint;
import com.olshevchenko.optaplanner.entity.RoutingSolution;
import com.olshevchenko.optaplanner.repository.VehicleRoutingSolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DemoDataGenerator {

    private final VehicleRoutingSolutionRepository repository;

    @EventListener(ApplicationReadyEvent.class)
    public void generateDemoData() {
        RoutingSolution solution = DemoDataBuilder.builder()
                .setMinDemand(1)
                .setMaxDemand(2)
                .setVehicleCapacity(25)
                .setCustomerCount(77)
                .setVehicleCount(6)
                .setDepotCount(1)
                .setSouthWestCorner(new MapPoint(0L, 50.333959, 30.258722))
                .setNorthEastCorner(new MapPoint(0L, 50.588556, 30.725106))
                .build();
        repository.update(solution);
    }
}
