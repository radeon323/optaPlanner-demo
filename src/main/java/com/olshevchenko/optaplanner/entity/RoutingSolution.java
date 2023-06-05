package com.olshevchenko.optaplanner.entity;

import com.olshevchenko.optaplanner.bootstrap.DemoDataBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@PlanningSolution
public class RoutingSolution {

    private String name;

    @ProblemFactCollectionProperty
    private List<MapPoint> locationList;

    @ProblemFactCollectionProperty
    private List<Depot> depotList;

    @PlanningEntityCollectionProperty
    private List<Vehicle> vehicleList;

    @ProblemFactCollectionProperty
    @ValueRangeProvider
    private List<Customer> customerList;

    @PlanningScore
    private HardSoftLongScore score;

    private MapPoint southWestCorner;
    private MapPoint northEastCorner;

    public RoutingSolution(String name,
                           List<MapPoint> locationList,
                           List<Depot> depotList,
                           List<Vehicle> vehicleList,
                           List<Customer> customerList,
                           MapPoint southWestCorner,
                           MapPoint northEastCorner) {
        this.name = name;
        this.locationList = locationList;
        this.depotList = depotList;
        this.vehicleList = vehicleList;
        this.customerList = customerList;
        this.southWestCorner = southWestCorner;
        this.northEastCorner = northEastCorner;
    }

    public static RoutingSolution empty() {
        RoutingSolution problem = DemoDataBuilder.builder()
                .setMinDemand(1)
                .setMaxDemand(2)
                .setVehicleCapacity(77).setCustomerCount(77).setVehicleCount(7).setDepotCount(1)
                .setSouthWestCorner(new MapPoint(0L, 51.44, -0.16))
                .setNorthEastCorner(new MapPoint(0L, 51.56, -0.01)).build();

        problem.setScore(HardSoftLongScore.ZERO);

        return problem;
    }

    public List<MapPoint> getBounds() {
        return Arrays.asList(southWestCorner, northEastCorner);
    }

    public long getDistanceMeters() {
        return score == null ? 0 : -score.softScore();
    }
}
