package com.jagsits.service.sudoku;

import com.jagsits.service.sudoku.solver.SudokuSolverAlgorithm;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SudokuResponse {

    private final SudokuBoard sudokuBoard;
    private final SudokuSolverAlgorithm sudokuSolverAlgorithm;
    private final SudokuDifficultyLevel sudokuDifficultyLevel;
    private final Long time;

    public SudokuResponse(SudokuBoard sudokuBoard, SudokuSolverAlgorithm sudokuSolverAlgorithm, SudokuDifficultyLevel sudokuDifficultyLevel, Long time) {
        this.sudokuBoard = sudokuBoard;
        this.sudokuSolverAlgorithm = sudokuSolverAlgorithm;
        this.sudokuDifficultyLevel = sudokuDifficultyLevel;
        this.time = time;
    }

    public SudokuBoard getSudokuBoard() {
        return sudokuBoard;
    }

    public SudokuSolverAlgorithm getSudokuSolverAlgorithm() {
        return sudokuSolverAlgorithm;
    }

    public SudokuDifficultyLevel getSudokuDifficultyLevel() {
        return sudokuDifficultyLevel;
    }

    public Long getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SudokuResponse that = (SudokuResponse) o;
        return new EqualsBuilder().append(getSudokuBoard(), that.getSudokuBoard()).append(getSudokuSolverAlgorithm(), that.getSudokuSolverAlgorithm()).append(getSudokuDifficultyLevel(), that.getSudokuDifficultyLevel()).append(getTime(), that.getTime()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getSudokuBoard()).append(getSudokuSolverAlgorithm()).append(getSudokuDifficultyLevel()).append(getTime()).toHashCode();
    }

}
