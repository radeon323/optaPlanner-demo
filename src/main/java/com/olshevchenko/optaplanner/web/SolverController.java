package com.olshevchenko.optaplanner.web;

import com.olshevchenko.optaplanner.entity.RoutingSolution;
import com.olshevchenko.optaplanner.entity.SolutionInfo;
import com.olshevchenko.optaplanner.service.DefaultRoutingSolutionService;
import lombok.RequiredArgsConstructor;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.solver.SolutionManager;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.olshevchenko.optaplanner.entity.SolutionStatus.COMPLETE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vrp")
public class SolverController {

    private static final long PROBLEM_ID = 0L;
    private final AtomicReference<Throwable> solverError = new AtomicReference<>();
    private final DefaultRoutingSolutionService routingSolutionService;
    private final SolverManager<RoutingSolution, Long> solverManager;
    private final SolutionManager<RoutingSolution, HardSoftLongScore> solutionManager;

    private SolutionInfo infoFromSolution(RoutingSolution solution) {
        return new SolutionInfo(solution,
                solutionManager.explain(solution).getSummary(),
                solverManager.getSolverStatus(PROBLEM_ID));
    }

    @GetMapping("/info")
    public SolutionInfo solutionInfo() {
        Optional.ofNullable(solverError.getAndSet(null)).ifPresent(throwable -> {
            throw new RuntimeException("Solver failed", throwable);
        });
        RoutingSolution routingSolution = routingSolutionService.getSolution();
        return infoFromSolution(routingSolution);
    }

    @PostMapping("/solve")
    public void solve() {
        RoutingSolution routingSolution = routingSolutionService.getSolution();
        while (routingSolution.getStatus() != COMPLETE) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        solverManager.solveAndListen(PROBLEM_ID, id -> routingSolution,
                routingSolutionService::update, (problemId, throwable) -> solverError.set(throwable));
    }

    @PostMapping("/stopSolving")
    public void stopSolving() {
        solverManager.terminateEarly(PROBLEM_ID);
    }


}
