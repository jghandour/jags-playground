package com.jagsits.service.sudoku;

import java.util.Comparator;

public enum SudokuDifficultyLevel {
    EASY(40),
    EXTREMELY_EASY(35),
    MEDIUM(45),
    DIFFICULT(55),
    EVIL(60); // TODO: Ideally we can get this higher

    public static final Comparator<SudokuDifficultyLevel> MISSING_CELLS_COMPARATOR = new CaseInsensitiveComparator();

    private int missingCellCount;

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
