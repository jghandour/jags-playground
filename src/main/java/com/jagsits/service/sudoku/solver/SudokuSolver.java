package com.jagsits.service.sudoku.solver;

import com.jagsits.service.sudoku.SudokuBoard;

public interface SudokuSolver {

    SudokuSolverAlgorithm getAlgorithm();

    SudokuBoard solve(SudokuBoard sudokuBoard);
}
