package com.jagsits.service.sudoku;

import com.jagsits.service.sudoku.solver.SudokuSolverAlgorithm;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SudokuResponseTest {

    @Test
    public void testObject() {
        SudokuBoard sudokuBoard = SudokuUtils.generateRandomSolvedSudokuBoard();
        SudokuSolverAlgorithm sudokuSolverAlgorithm = SudokuSolverAlgorithm.KNUTH;
        SudokuDifficultyLevel sudokuDifficultyLevel = SudokuDifficultyLevel.EXTREMELY_EASY;
        Long time = 1L;

        SudokuResponse sudokuResponse = new SudokuResponse(sudokuBoard, sudokuSolverAlgorithm, sudokuDifficultyLevel, time);
        SudokuResponse sudokuResponse2 = new SudokuResponse(sudokuBoard, sudokuSolverAlgorithm, sudokuDifficultyLevel, time);

        assertEquals(sudokuResponse.hashCode(), sudokuResponse2.hashCode());
        assertTrue(sudokuResponse.equals(sudokuResponse2));
        assertEquals(sudokuResponse.toString(), sudokuResponse2.toString());

        assertEquals(sudokuBoard, sudokuResponse.getSudokuBoard());
        assertEquals(sudokuSolverAlgorithm, sudokuResponse.getSudokuSolverAlgorithm());
        assertEquals(sudokuDifficultyLevel, sudokuResponse.getSudokuDifficultyLevel());
        assertEquals(time, sudokuResponse.getTime());

        assertTrue(sudokuResponse.equals(sudokuResponse));
        assertFalse(sudokuResponse.equals(""));
        assertFalse(sudokuResponse.equals(null));
    }
}
