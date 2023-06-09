package com.olshevchenko.optaplanner.web;

import com.olshevchenko.optaplanner.entity.RoutingSolution;
import com.olshevchenko.optaplanner.entity.Status;
import com.olshevchenko.optaplanner.repository.RoutingSolutionRepository;
import com.olshevchenko.optaplanner.utils.GraphHopperDistanceCalculator;
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
    private final GraphHopperDistanceCalculator distanceCalculator;

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

        Optional<RoutingSolution> optionalRoutingSolution = repository.solution();

        RoutingSolution routingSolution = optionalRoutingSolution.orElse(RoutingSolution.empty());
        return statusFromSolution(routingSolution);
    }


    @PostMapping("/solve")
    public void solve() {
        Optional<RoutingSolution> maybeSolution = repository.solution();

        if (maybeSolution.isEmpty()) {
            System.out.println("No solution found.");
            return;
        }

        RoutingSolution solution = maybeSolution.get();
//        distanceCalculator.initDistanceMaps(solution.getLocationList());
        solverManager.solveAndListen(PROBLEM_ID, id -> solution,
                solution1 -> repository.update(solution1), (problemId, throwable) -> solverError.set(throwable));
    }

    @PostMapping("/stopSolving")
    public void stopSolving() {
        solverManager.terminateEarly(PROBLEM_ID);
    }


}
