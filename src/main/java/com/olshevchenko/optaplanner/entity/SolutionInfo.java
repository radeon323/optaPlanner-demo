package com.olshevchenko.optaplanner.entity;

import lombok.ToString;
import org.optaplanner.core.api.solver.SolverStatus;

@ToString
public
class SolutionInfo {

    public final RoutingSolution solution;
    public final String scoreExplanation;
    public final boolean isSolving;

    public SolutionInfo(RoutingSolution solution, String scoreExplanation, SolverStatus solverStatus) {
        this.solution = solution;
        this.scoreExplanation = scoreExplanation;
        this.isSolving = solverStatus != SolverStatus.NOT_SOLVING;
    }
}
