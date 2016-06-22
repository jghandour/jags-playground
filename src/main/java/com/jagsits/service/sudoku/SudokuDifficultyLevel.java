package com.jagsits.service.sudoku;

import java.util.Comparator;

public enum SudokuDifficultyLevel {
    EXTREMELY_EASY(35),
    EASY(40),
    MEDIUM(45),
    DIFFICULT(55),
    EVIL(60);

    public static final Comparator<SudokuDifficultyLevel> MISSING_CELLS_COMPARATOR = new CaseInsensitiveComparator();

    private final int missingCellCount;

    SudokuDifficultyLevel(int missingCellCount) {
        this.missingCellCount = missingCellCount;
    }

    public int getMissingCellCount() {
        return missingCellCount;
    }

    private static class CaseInsensitiveComparator implements Comparator<SudokuDifficultyLevel> {
        @Override
        public int compare(SudokuDifficultyLevel level1, SudokuDifficultyLevel level2) {
            return level1.getMissingCellCount() - level2.getMissingCellCount();
        }
    }

}
