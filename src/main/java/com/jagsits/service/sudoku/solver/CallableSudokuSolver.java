package com.jagsits.service.sudoku.solver;

import com.jagsits.service.sudoku.SudokuBoard;
import com.jagsits.service.sudoku.SudokuDifficultyLevel;
import com.jagsits.service.sudoku.SudokuResponse;

import java.util.concurrent.Callable;

public class CallableSudokuSolver implements Callable<SudokuResponse> {

    private SudokuSolver solver;
    private SudokuBoard board;
    private SudokuDifficultyLevel level;

    public CallableSudokuSolver(SudokuSolver solver, SudokuBoard board, SudokuDifficultyLevel level) {
        this.solver = solver;
        this.board = board;
        this.level = level;
    }

    @Override
    public SudokuResponse call() throws Exception {
        SudokuBoard solvedBoard = solver.solve(board);
        return new SudokuResponse(solvedBoard, solver.getAlgorithm(), level, 0L);
    }

}
