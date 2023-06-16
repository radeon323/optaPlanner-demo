package com.olshevchenko.optaplanner.repository;

import com.olshevchenko.optaplanner.entity.RoutingSolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoutingSolutionRepository {
    @Autowired
    private RoutePointRepository routePointRepository;
    private RoutingSolution routingSolution;

    public Optional<RoutingSolution> solution() {
        if (routingSolution == null) {
            this.routingSolution = new RoutingSolution();
            this.routingSolution.setName("GrandeDolce");
            this.routingSolution.setRoutePointList(routePointRepository.findAll());
        }
        return Optional.of(routingSolution);
    }

    public void update(RoutingSolution routingSolution) {
        this.routingSolution = routingSolution;
    }

}
