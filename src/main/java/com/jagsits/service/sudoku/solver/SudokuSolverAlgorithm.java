package com.jagsits.service.sudoku.solver;

import java.util.HashMap;
import java.util.Map;

public enum SudokuSolverAlgorithm {
    BACK_TRACKING,
    BRUTE_FORCE,
    KNUTH,
    KUDOKU;

    private static final Map<String, SudokuSolverAlgorithm> map = new HashMap<>();

    static {
        for (SudokuSolverAlgorithm value : values()) {
            map.put(value.toString(), value);
        }
    }

    public static SudokuSolverAlgorithm getAlgorithm(String algorithm) {
        return map.get(algorithm);
    }
}

