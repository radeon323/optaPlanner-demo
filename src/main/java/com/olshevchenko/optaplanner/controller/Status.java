package com.olshevchenko.optaplanner.controller;

import com.olshevchenko.optaplanner.entity.RoutingSolution;
import lombok.ToString;
import org.optaplanner.core.api.solver.SolverStatus;

@ToString
class Status {

    public final RoutingSolution solution;
    public final String scoreExplanation;
    public final boolean isSolving;

    Status(RoutingSolution solution, String scoreExplanation, SolverStatus solverStatus) {
        this.solution = solution;
        this.scoreExplanation = scoreExplanation;
        this.isSolving = solverStatus != SolverStatus.NOT_SOLVING;
    }
}
