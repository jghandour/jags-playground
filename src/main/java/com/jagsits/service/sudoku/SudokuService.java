package com.jagsits.service.sudoku;

import com.jagsits.service.sudoku.solver.SudokuSolverAlgorithm;

public interface SudokuService {

    SudokuBoard createBoard(SudokuDifficultyLevel level);

    SudokuResponse solve(String algorithmString, String boardString);

    SudokuResponse solve(SudokuSolverAlgorithm algorithm, String boardString);

    SudokuResponse solve(String boardString);

    SudokuResponse solve(SudokuBoard board);
}
