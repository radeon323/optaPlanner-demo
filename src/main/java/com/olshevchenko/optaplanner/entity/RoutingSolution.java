package com.olshevchenko.optaplanner.entity;

import com.olshevchenko.optaplanner.repository.DemoDataBuilder;
import lombok.*;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@PlanningSolution
public class RoutingSolution {

    private String name;

    private boolean isDistancesInitialized = false;

    @ProblemFactCollectionProperty
    private List<MapPoint> locationList;

    @ProblemFactCollectionProperty
    private List<Store> storeList;

    @PlanningEntityCollectionProperty
    private List<Route> routeList;

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "customerList")
    private List<Customer> customerList;

    @PlanningScore
    private HardSoftLongScore score;


    public RoutingSolution(String name,
                           List<MapPoint> locationList,
                           List<Store> storeList,
                           List<Route> routeList,
                           List<Customer> customerList) {
        this.name = name;
        this.locationList = locationList;
        this.storeList = storeList;
        this.routeList = routeList;
        this.customerList = customerList;
    }

    public static RoutingSolution empty() {
        RoutingSolution problem = DemoDataBuilder.builder().build();

        problem.setScore(HardSoftLongScore.ZERO);

        return problem;
    }

    public long getDistanceMeters() {
        return score == null ? 0 : -score.softScore();
    }

}
