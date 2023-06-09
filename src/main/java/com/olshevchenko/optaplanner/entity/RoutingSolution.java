package com.olshevchenko.optaplanner.entity;

import com.olshevchenko.optaplanner.demo.DemoDataBuilder;
import lombok.*;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;

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
    private List<Store> storeList;

    @PlanningEntityCollectionProperty
    private List<Car> carList;

    @ProblemFactCollectionProperty
    @ValueRangeProvider
    private List<Customer> customerList;

    @PlanningScore
    private HardSoftLongScore score;

    public RoutingSolution(String name,
                           List<MapPoint> locationList,
                           List<Store> storeList,
                           List<Car> carList,
                           List<Customer> customerList) {
        this.name = name;
        this.locationList = locationList;
        this.storeList = storeList;
        this.carList = carList;
        this.customerList = customerList;
    }

    public static RoutingSolution empty() {
        RoutingSolution problem = DemoDataBuilder.builder()
                .setMinDemand(1)
                .setMaxDemand(2)
                .build();

        problem.setScore(HardSoftLongScore.ZERO);

        return problem;
    }

    public long getDistanceMeters() {
        return score == null ? 0 : -score.softScore();
    }

}
