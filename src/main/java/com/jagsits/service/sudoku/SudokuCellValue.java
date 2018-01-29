package com.jagsits.service.sudoku;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

class SudokuCellValue {
    private final SudokuCell sudokuCell;
    private final int value;

    public SudokuCellValue(SudokuCell sudokuCell, int value) {
        this.sudokuCell = sudokuCell;
        this.value = value;
    }

    public SudokuCell getSudokuCell() {
        return sudokuCell;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SudokuCellValue that = (SudokuCellValue) o;

        return new EqualsBuilder().append(getValue(), that.getValue()).append(getSudokuCell(), that.getSudokuCell()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getSudokuCell()).append(getValue()).toHashCode();
    }
}
