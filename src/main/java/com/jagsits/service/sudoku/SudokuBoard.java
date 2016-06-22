package com.jagsits.service.sudoku;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SudokuBoard {

    public static final int DEFAULT_VALUE = 0;
    public static final int SUB_BOARD_DIMENSION = 3;
    public static final int BOARD_DIMENSION = 9;
    public static final int BOARD_CELL_COUNT = BOARD_DIMENSION * BOARD_DIMENSION;

    private int[][] cells = new int[BOARD_DIMENSION][BOARD_DIMENSION];

    public SudokuBoard(SudokuBoard board) {
        this(board.toString());
    }

    public SudokuBoard(int[][] values) {
        this.cells = values;
    }

    @JsonCreator
    public SudokuBoard(String values) {
        assert values != null && values.length() == BOARD_CELL_COUNT;
        for (int i = 0; i < BOARD_CELL_COUNT; i++) {
            int r = getR(i);
            int c = getC(i);
            int parsedCellValue = parseCellValue(values.charAt(i));
            this.cells[r][c] = parsedCellValue;
        }
    }

    private static int getR(int i) {
        return i / BOARD_DIMENSION;
    }

    private static int getC(int i) {
        return i % BOARD_DIMENSION;
    }

    private static int parseCellValue(char value) {
        int parsed = Character.getNumericValue(value);
        if (parsed > 0 && parsed < 10) {
            return parsed;
        }
        return DEFAULT_VALUE;
    }

    public boolean isPopulated(SudokuCell cell) {
        return isPopulated(cell.getR(), cell.getC());
    }

    public boolean isPopulated(int r, int c) {
        return getCellValue(r, c) != DEFAULT_VALUE;
    }

    public int getCellValue(SudokuCell cell) {
        return getCellValue(cell.getR(), cell.getC());
    }

    public int getCellValue(int r, int c) {
        return cells[r][c];
    }

    public void clearValue(Collection<SudokuCell> cells) {
        if (CollectionUtils.isNotEmpty(cells)) {
            cells.forEach(this::clearValue);
        }
    }

    public void clearValue(SudokuCell cell) {
        clearValue(cell.getR(), cell.getC());
    }

    public void clearValue(int r, int c) {
        cells[r][c] = DEFAULT_VALUE;
    }

    public void setCellValue(SudokuCellValue value) {
        setCellValue(value.getSudokuCell(), value.getValue());
    }

    public void setCellValue(SudokuCell cell, int value) {
        setCellValue(cell.getR(), cell.getC(), value);
    }

    public void setCellValue(int r, int c, int value) {
        cells[r][c] = value;
    }

    public List<SudokuCell> getUnpopulatedCells() {
        return getAllCells().stream().filter(cell -> !isPopulated(cell)).collect(Collectors.toList());
    }

    public List<SudokuCell> getPopulatedCells() {
        return getAllCells().stream().filter(this::isPopulated).collect(Collectors.toList());
    }

    public List<SudokuCellValue> getAllCellsValues() {
        List<SudokuCellValue> result = new ArrayList<>(BOARD_CELL_COUNT);
        result.addAll(getAllCells().stream().map(cell -> new SudokuCellValue(cell, getCellValue(cell))).collect(Collectors.toList()));
        return result;
    }

    public List<SudokuCell> getAllCells() {
        List<SudokuCell> result = new ArrayList<>(BOARD_CELL_COUNT);
        for (int r = 0; r < BOARD_DIMENSION; r++) {
            for (int c = 0; c < BOARD_DIMENSION; c++) {
                result.add(new SudokuCell(r, c));
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SudokuBoard that = (SudokuBoard) o;
        return new EqualsBuilder().append(cells, that.cells).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(cells).toHashCode();
    }

    @JsonValue
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < BOARD_DIMENSION; r++) {
            for (int c = 0; c < BOARD_DIMENSION; c++) {
                sb.append(SudokuUtils.toString(this, r, c));
            }
        }
        return sb.toString();
    }

}