package com.olshevchenko.optaplanner.service;

import com.olshevchenko.optaplanner.entity.RoutingSolution;
import com.olshevchenko.optaplanner.repository.RoutingSolutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultRoutingSolutionService {
    private final RoutingSolutionRepository repository;

    public RoutingSolution getSolution() {
        return repository.solution().orElseThrow(() -> new NoSuchElementException("No solution found."));
    }

    public void update(RoutingSolution solution) {
        repository.update(solution);
    }
}
