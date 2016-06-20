package com.jagsits.service.sudoku.solver;

import com.jagsits.service.sudoku.SudokuBoard;

public interface NumberOfSolutionsSudokuSolver {
    int getNumberOfPossibleSolutions(SudokuBoard sudokuBoard);
}
