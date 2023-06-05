package com.olshevchenko.optaplanner.controller;

import com.olshevchenko.optaplanner.entity.RoutingSolution;
import com.olshevchenko.optaplanner.repository.RoutingSolutionRepository;
import lombok.RequiredArgsConstructor;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.solver.SolutionManager;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vrp")
public class SolverController {

    private static final long PROBLEM_ID = 0L;

    private final AtomicReference<Throwable> solverError = new AtomicReference<>();

    private final RoutingSolutionRepository repository;
    private final SolverManager<RoutingSolution, Long> solverManager;
    private final SolutionManager<RoutingSolution, HardSoftLongScore> solutionManager;

    private Status statusFromSolution(RoutingSolution solution) {
        return new Status(solution,
                solutionManager.explain(solution).getSummary(),
                solverManager.getSolverStatus(PROBLEM_ID));
    }

    @GetMapping("/status")
    public Status status() {
        Optional.ofNullable(solverError.getAndSet(null)).ifPresent(throwable -> {
            throw new RuntimeException("Solver failed", throwable);
        });

        Optional<RoutingSolution> s1 = repository.solution();

        RoutingSolution s = s1.orElse(RoutingSolution.empty());
        return statusFromSolution(s);
    }


    @PostMapping("/solve")
    public void solve() {
        Optional<RoutingSolution> maybeSolution = repository.solution();
        maybeSolution.ifPresent(
                vehicleRoutingSolution -> solverManager.solveAndListen(PROBLEM_ID, id -> vehicleRoutingSolution,
                        repository::update, (problemId, throwable) -> solverError.set(throwable)));
    }

    @PostMapping("/stopSolving")
    public void stopSolving() {
        solverManager.terminateEarly(PROBLEM_ID);
    }
}
