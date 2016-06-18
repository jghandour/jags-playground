package com.jagsits.service.sudoku;

import com.jagsits.service.BaseService;
import com.jagsits.service.sudoku.solver.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

@Service
@ManagedResource
public class DefaultSudokuService extends BaseService implements SudokuService {

    // TODO: Find a better way to implement this!
    @Override
    public SudokuBoard createBoard(SudokuDifficultyLevel difficultyLevel) {
        KnuthSudokuSolver knuthSudokuSolver = new KnuthSudokuSolver();

        // Remove random values ensuring the result is always a board with only one solution
        int maxAttempts = 100;
        while (true) {
            SudokuBoard result = SudokuUtils.generateRandomSolvedSudokuBoard();
            int attempts = 0;
            int missingCells = 0;

            do {
                List<SudokuCell> populatedCells = result.getPopulatedCells();
                Collections.shuffle(populatedCells);
                for (SudokuCell cell : populatedCells) {
                    int cellValue = result.getCellValue(cell);
                    result.clearValue(cell);
                    missingCells++;

                    if (knuthSudokuSolver.getNumberOfPossibleSolutions(result) == 1) {
                        attempts = 0;
                        if (missingCells == difficultyLevel.getMissingCellCount()) {
                            return result;
                        }
                    } else {
                        missingCells--;
                        result.setCellValue(cell, cellValue);
                        attempts++;
                    }
                }
            } while (attempts < maxAttempts);
        }
    }

    @ManagedOperation
    public String solveAsString(String algorithm, String boardString) {
        return solve(algorithm, boardString).getSudokuBoard().toString();
    }

    @ManagedOperation
    public String solveAsString(String boardString) {
        return solve(boardString).getSudokuBoard().toString();
    }

    @Override
    public SudokuResponse solve(String algorithmString, String boardString) {
        SudokuSolverAlgorithm algorithm = SudokuSolverAlgorithm.getAlgorithm(algorithmString);
        SudokuResponse result;
        if (algorithm != null) {
            result = solve(algorithm, boardString);
        } else {
            result = solve(boardString);
        }
        return result;
    }

    // Single threaded solution, using a specific algorithm

    @Override
    public SudokuResponse solve(SudokuSolverAlgorithm algorithm, String boardString) {
        SudokuSolver solver = SudokuSolverFactory.createSudokuSolver(algorithm);
        SudokuResponse result = getResult(solver, boardString);
        log.info("Solving took {} mills.", result.getTime());
        return result;
    }

    private SudokuResponse getResult(SudokuSolver solver, String boardString) {
        SudokuBoard board = new SudokuBoard(boardString);
        SudokuDifficultyLevel level = SudokuUtils.getDifficultyLevel(board);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        board = solver.solve(board);
        stopWatch.stop();
        return new SudokuResponse(board, solver.getAlgorithm(), level, stopWatch.getTime());
    }

    // Multi threaded option, using the fastest solution

    @Override
    public SudokuResponse solve(String boardString) {
        SudokuBoard board = new SudokuBoard(boardString);
        return solve(board);
    }

    @Override
    public SudokuResponse solve(SudokuBoard board) {
        SudokuDifficultyLevel level = SudokuUtils.getDifficultyLevel(board);
        SudokuResponse result = new SudokuResponse(board, null, level, 0L);

        List<SudokuCell> cellsToSolve = board.getUnpopulatedCells();
        if (CollectionUtils.isNotEmpty(cellsToSolve)) {
            // Create a worker for every potential algorithm
            List<Callable<SudokuResponse>> workers = new LinkedList<>();
            for (SudokuSolverAlgorithm algorithm : SudokuSolverAlgorithm.values()) {
                workers.add(new CallableSudokuSolver(SudokuSolverFactory.createSudokuSolver(algorithm), new SudokuBoard(board), level));
            }

            // Run the executors
            result = solve(workers);
        }
        return result;
    }

    private SudokuResponse solve(List<Callable<SudokuResponse>> workers) {
        SudokuResponse result = null;

        // Submit each of the workers to the executor
        int count = workers.size();
        Executor executor = Executors.newFixedThreadPool(workers.size());
        ExecutorCompletionService<SudokuResponse> ecs = new ExecutorCompletionService<>(executor);
        List<Future<SudokuResponse>> futures = new ArrayList<>(count);
        workers.forEach(it -> futures.add(ecs.submit(it)));

        // Get the Future's back from the executor completion service in a blocking fashion and return the first one that has a solution
        try {
            for (int i = 0; i < count; ++i) {
                try {
                    Future<SudokuResponse> future = ecs.take(); // Blocking
                    SudokuResponse sudokuResult = future.get();
                    // Just because we have something on the queue, doesn't mean it is an actual solution
                    if (sudokuResult != null && SudokuUtils.isSolved(sudokuResult.getSudokuBoard())) {
                        result = sudokuResult;
                        break;
                    }
                } catch (ExecutionException e) {
                    log.debug("ExecutionException.", e);
                }
            }
        } catch (InterruptedException e) {
            log.warn("InterruptedException.", e);
        } finally {
            for (Future<SudokuResponse> f : futures) {
                f.cancel(true);
            }
        }

        return result;
    }

}
