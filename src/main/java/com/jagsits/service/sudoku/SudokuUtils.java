package com.jagsits.service.sudoku;

import com.jagsits.util.JagsUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.MathArrays;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.jagsits.service.sudoku.SudokuBoard.BOARD_DIMENSION;
import static com.jagsits.service.sudoku.SudokuBoard.SUB_BOARD_DIMENSION;

public final class SudokuUtils {

    private SudokuUtils() {
    }

    /**
     * Generates a random solved Sudoku Board
     * <p/>
     * Approach:
     * Generate a puzzle using 9 symbols.
     * Randomly assign symbol to a value of 1-9
     * Randomly transpose the grid
     * Randomly shuffle rows / cols / row groups / col groups
     */
    public static SudokuBoard generateRandomSolvedSudokuBoard() {
        int[][] cells = generateRandomSolvedBoard();
        shuffle(cells, 10_000);
        return new SudokuBoard(cells);
    }

    private static int[][] generateRandomSolvedBoard() {
        // Generate a randomly sorted array of values 1-9
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        MathArrays.shuffle(values);

        // Generate a board using symbols
        int[][] result = new int[BOARD_DIMENSION][BOARD_DIMENSION];
        for (int r = 0; r < BOARD_DIMENSION; r++) {
            for (int c = 0; c < BOARD_DIMENSION; c++) {
                int index = (r * SUB_BOARD_DIMENSION + r / SUB_BOARD_DIMENSION + c) % (BOARD_DIMENSION);
                result[r][c] = values[index];
            }
        }
        return result;
    }

    private static void shuffle(int[][] cells, int count) {
        Random random = new Random();

        // Shuffle rows & cols around
        for (int i = 0; i < count; i++) {

            // Transpose the array (switch rows to cols and cols to rows)
            if (random.nextBoolean()) {
                JagsUtils.transpose(cells);
            }

            // Swap individual rows
            if (random.nextBoolean()) {
                int firstRow = random.nextInt(SUB_BOARD_DIMENSION) * SUB_BOARD_DIMENSION; // 0, 3, 6
                int lastRow = firstRow + random.nextInt(SUB_BOARD_DIMENSION); // 0 -> 1,2 ; 3 -> 4,5; 6 -> 7,8
                if (firstRow != lastRow) {
                    swapRows(cells, firstRow, lastRow);
                }
            }

            // Swap groups of rows
            if (random.nextBoolean()) {
                int firstGroup = random.nextInt(SUB_BOARD_DIMENSION);
                int lastGroup = random.nextInt(SUB_BOARD_DIMENSION);
                if (firstGroup != lastGroup) {
                    for (int j = 0; j < SUB_BOARD_DIMENSION; j++) {
                        swapRows(cells, (firstGroup * SUB_BOARD_DIMENSION) + j, (lastGroup * SUB_BOARD_DIMENSION) + j);
                    }
                }
            }
        }
    }

    private static void swapRows(int[][] input, int firstRow, int lastRow) {
        int[] tmp = input[firstRow];
        input[firstRow] = input[lastRow];
        input[lastRow] = tmp;
    }

    public static SudokuDifficultyLevel getDifficultyLevel(SudokuBoard sudokuBoard) {
        SudokuDifficultyLevel levels[] = SudokuDifficultyLevel.values();
        Arrays.sort(levels, SudokuDifficultyLevel.MISSING_CELLS_COMPARATOR);

        int missingCells = sudokuBoard.getUnpopulatedCells().size();
        for (SudokuDifficultyLevel level : levels) {
            if (missingCells <= level.getMissingCellCount()) {
                return level;
            }
        }
        return levels[levels.length - 1]; // Most Difficult
    }

    public static List<Integer> getValidValues(SudokuBoard board, int r, int c) {
        return getValidValues(board, new SudokuCell(r, c));
    }

    public static List<Integer> getValidValues(SudokuBoard board, SudokuCell cell) {
        List<Integer> result = null;
        if (!board.isPopulated(cell)) {
            result = new LinkedList<>();
            for (int i = 1; i < BOARD_DIMENSION + 1; i++) {
                if (isValidValue(board, cell, i)) {
                    result.add(i);
                }
            }
        }
        return result;
    }

    public static boolean isValidValue(SudokuBoard board, SudokuCell cell, int value) {

        if (board.isPopulated(cell)) {
            throw new RuntimeException("Cannot call for SudokuCell which already has a value");
        }

        // Check Row
        for (int c = 0; c < BOARD_DIMENSION; c++) {
            if (board.getCellValue(cell.getR(), c) == value) {
                return false;
            }
        }

        // Check Column
        for (int r = 0; r < BOARD_DIMENSION; r++) {
            if (board.getCellValue(r, cell.getC()) == value)
                return false;
        }

        // Check SubGrid
        int r1 = SUB_BOARD_DIMENSION * (cell.getR() / SUB_BOARD_DIMENSION);
        int c1 = SUB_BOARD_DIMENSION * (cell.getC() / SUB_BOARD_DIMENSION);
        int r2 = r1 + 2;
        int c2 = c1 + 2;

        for (int r = r1; r <= r2; r++) {
            for (int c = c1; c <= c2; c++) {
                if (board.getCellValue(r, c) == value) {
                    return false;
                }
            }
        }

        // It is valid!
        return true;
    }

    public static String toPrettyString(SudokuBoard board) {
        StringBuilder result = new StringBuilder();
        for (int r = 0; r < BOARD_DIMENSION; r++) {
            for (int c = 0; c < BOARD_DIMENSION; c++) {

                if (c == 0 && r != 0 && (r % SUB_BOARD_DIMENSION) == 0) {
                    String repeatedString = StringUtils.repeat("-", SUB_BOARD_DIMENSION * 2);
                    result.append(JagsUtils.LINE_SEPARATOR);
                    result.append(repeatedString).append("+");
                    result.append(repeatedString).append("+");
                    result.append(repeatedString);
                } else if (c != 0 && c % SUB_BOARD_DIMENSION == 0) {
                    result.append("|");
                }

                if (c == 0) {
                    result.append(JagsUtils.LINE_SEPARATOR);
                }

                result.append(toString(board, r, c)).append(" ");
            }
        }
        return result.toString();
    }

    public static String toString(SudokuBoard board, int r, int c) {
        String result = ".";
        int cellValue = board.getCellValue(r, c);
        if (cellValue != SudokuBoard.DEFAULT_VALUE) {
            result = String.valueOf(cellValue);
        }
        return result;
    }

    public static boolean isSolved(SudokuBoard board) {
        // Check Rows & Columns
        for (int r = 0; r < BOARD_DIMENSION; r++) {
            boolean[] rowValues = new boolean[BOARD_DIMENSION];
            boolean[] colValues = new boolean[BOARD_DIMENSION];
            for (int c = 0; c < BOARD_DIMENSION; c++) {

                // Row
                int rowValue = board.getCellValue(r, c);
                if (rowValue == SudokuBoard.DEFAULT_VALUE) {
                    return false;
                }
                rowValues[rowValue - 1] = true;

                // Column
                int colValue = board.getCellValue(c, r);
                if (colValue == SudokuBoard.DEFAULT_VALUE) {
                    return false;
                }
                colValues[colValue - 1] = true;
            }

            // Make sure they are all populated
            for (int i = 0; i < BOARD_DIMENSION; i++) {
                if (!rowValues[i] || !colValues[i]) {
                    return false;
                }
            }
        }

        // Check SubGrid
        for (int topR = 0; topR < SUB_BOARD_DIMENSION; topR = topR + 3) {
            for (int topC = 0; topC < SUB_BOARD_DIMENSION; topC = topC + 3) {

                boolean[] values = new boolean[BOARD_DIMENSION];
                int r1 = SUB_BOARD_DIMENSION * (topR / SUB_BOARD_DIMENSION);
                int c1 = SUB_BOARD_DIMENSION * (topC / SUB_BOARD_DIMENSION);
                int r2 = r1 + 2;
                int c2 = c1 + 2;

                for (int r = r1; r <= r2; r++) {
                    for (int c = c1; c <= c2; c++) {
                        int value = board.getCellValue(r, c);
                        if (value == SudokuBoard.DEFAULT_VALUE) {
                            return false;
                        }
                        values[value - 1] = true;
                    }
                }

                // Make sure they are all populated
                for (int i = 0; i < BOARD_DIMENSION; i++) {
                    if (!values[i]) {
                        return false;
                    }
                }
            }
        }


        return true;
    }
}
