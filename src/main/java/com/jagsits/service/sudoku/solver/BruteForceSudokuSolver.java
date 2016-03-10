package com.jagsits.service.sudoku.solver;

import com.jagsits.service.sudoku.SudokuBoard;
import com.jagsits.service.sudoku.SudokuCell;
import com.jagsits.service.sudoku.SudokuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Loosely based on http://en.wikipedia.org/wiki/Sudoku_solving_algorithms#Brute-force_algorithm
 */
public class BruteForceSudokuSolver implements SudokuSolver {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public SudokuSolverAlgorithm getAlgorithm() {
        return SudokuSolverAlgorithm.BRUTE_FORCE;
    }

    @Override
    public SudokuBoard solve(SudokuBoard sudokuBoard) {
        doElimination(sudokuBoard);
        populateCell(sudokuBoard, sudokuBoard.getUnpopulatedCells(), 0);
        return sudokuBoard;
    }

    private Collection<SudokuCell> doElimination(SudokuBoard board) {
        Collection<SudokuCell> result = new LinkedList<>();
        boolean updated;

        do {
            updated = false;
            for (SudokuCell cell : board.getUnpopulatedCells()) {
                if (!board.isPopulated(cell)) {
                    List<Integer> possibleValues = SudokuUtils.getValidValues(board, cell);
                    if (possibleValues.size() == 1) {
                        board.setCellValue(cell, possibleValues.get(0));
                        result.add(cell);
                        updated = true;
                    }
                }
            }
        }
        while (updated);

        return result;
    }

    public boolean populateCell(SudokuBoard board, List<SudokuCell> cellsToSolve, int index) {
        if (SudokuUtils.isSolved(board)) {
            return true;
        }

        if (index != cellsToSolve.size()) {
            SudokuCell cell = cellsToSolve.get(index);
            for (int potentialValue : SudokuUtils.getValidValues(board, cell)) {
                board.setCellValue(cell, potentialValue);
                boolean solved = populateCell(board, cellsToSolve, index + 1);
                if (solved) {
                    return true;
                }
            }
            board.clearValue(cell);
        }

        return false;
    }

}
