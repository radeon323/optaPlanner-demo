package com.olshevchenko.optaplanner.entity;

import lombok.*;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

import java.util.List;

import static com.olshevchenko.optaplanner.entity.SolutionStatus.RAW;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@PlanningSolution
public class RoutingSolution {

    private String name;

    private SolutionStatus status = RAW;

    @ProblemFactCollectionProperty
    private List<MapPoint> locationList;

    @ProblemFactCollectionProperty
    private List<Store> storeList;

    @PlanningEntityCollectionProperty
    private List<Route> routeList;

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "routePointList")
    private List<RoutePoint> routePointList;

    @PlanningScore
    private HardSoftLongScore score;


    public RoutingSolution(String name,
                           List<RoutePoint> routePointList) {
        this.name = name;
        this.routePointList = routePointList;
    }

    public long getDistanceMeters() {
        return score == null ? 0 : -score.softScore();
    }

}
