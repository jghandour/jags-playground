package com.jagsits.service.sudoku.solver;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SudokuSolverFactoryTest {
    @Test
    public void testIt() {
        assertNull(SudokuSolverFactory.createSudokuSolver(null));
        for (SudokuSolverAlgorithm algorithm : SudokuSolverAlgorithm.values()) {
            assertNotNull(SudokuSolverFactory.createSudokuSolver(algorithm));
        }
    }

}
