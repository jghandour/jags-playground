package com.jagsits.service.sudoku.solver;

import com.jagsits.service.sudoku.SudokuBoard;
import com.jagsits.service.sudoku.SudokuCell;
import com.jagsits.service.sudoku.SudokuUtils;

public class BackTrackingSudokuSolver implements SudokuSolver {

    @Override
    public SudokuSolverAlgorithm getAlgorithm() {
        return SudokuSolverAlgorithm.BACK_TRACKING;
    }

    @Override
    public SudokuBoard solve(SudokuBoard board) {
        solveCell(board, new SudokuCell(0, 0));
        // FIXME handle solved = false
        return board;
    }

    private boolean solveCell(SudokuBoard board, SudokuCell cell) {

        if (cell == null) {
            return true;
        }

        if (board.isPopulated(cell)) {
            return solveCell(board, getNextSudokuCell(cell));
        }

        for (int i = 1; i <= SudokuBoard.BOARD_DIMENSION; i++) {
            if (!SudokuUtils.isValidValue(board, cell, i)) {
                continue;
            }
            board.setCellValue(cell, i);
            boolean solved = solveCell(board, getNextSudokuCell(cell));
            if (solved) {
                return true;
            } else {
                board.clearValue(cell);
            }
        }

        return false;
    }

    private SudokuCell getNextSudokuCell(SudokuCell cell) {
        int r = cell.getR();
        int c = cell.getC() + 1;

        // Last Col?
        if (c > SudokuBoard.BOARD_DIMENSION - 1) {
            c = 0;
            r++;
        }

        // Last Row?
        SudokuCell result = null;
        if (r < SudokuBoard.BOARD_DIMENSION) {
            result = new SudokuCell(r, c);
        }

        return result;
    }

}
