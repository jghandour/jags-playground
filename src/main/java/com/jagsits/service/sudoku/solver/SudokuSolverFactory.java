package com.jagsits.service.sudoku.solver;

public final class SudokuSolverFactory {

    private SudokuSolverFactory() {
    }

    public static SudokuSolver createSudokuSolver(SudokuSolverAlgorithm algorithm) {
        SudokuSolver result = null;
        if (algorithm != null) {
            switch (algorithm) {
                case BACK_TRACKING:
                    result = new BackTrackingSudokuSolver();
                    break;
                case BRUTE_FORCE:
                    result = new BruteForceSudokuSolver();
                    break;
                case KUDOKU:
                    result = new KudokuSudokuSolver();
                    break;
                case KNUTH:
                    result = new KnuthSudokuSolver();
                    break;
                default:
                    result = null;
            }
        }
        return result;
    }

    public static NumberOfSolutionsSudokuSolver createNumberOfSolutionsSudokuSolver() {
        return new KnuthSudokuSolver();
    }

}
