package com.jagsits.service.sudoku;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class SudokuUtilsTest {

    private static final int ATTEMPTS = 10;

    @Test
    public void testGenerate() {
        for (int i = 0; i < ATTEMPTS; i++) {
            SudokuBoard board = SudokuUtils.generateRandomSolvedSudokuBoard();
            assertTrue(SudokuUtils.isSolved(board));
        }
    }

    @Test(expected = RuntimeException.class)
    public void testIsValidValue() {
        SudokuBoard board = new SudokuBoard(SudokuBoardTest.BOARD_STRING_UNSOLVED);
        SudokuUtils.isValidValue(board, new SudokuCell(0, 0), 5);
    }

    @Test
    public void testIsSolved() {
        assertFalse(SudokuUtils.isSolved(null));
        assertFalse(SudokuUtils.isSolved(new SudokuBoard(SudokuBoardTest.BOARD_STRING_UNSOLVED)));
        assertTrue(SudokuUtils.isSolved(new SudokuBoard(SudokuBoardTest.BOARD_STRING_SOLVED)));
        assertFalse(SudokuUtils.isSolved(new SudokuBoard("174385962293467158586192734451923876928674315367851249719548623635219487842736592")));
        assertFalse(SudokuUtils.isSolved(new SudokuBoard("999999999999999999999999999999999999999999999999999999999999999999999999999999999")));
    }

    @Test
    public void testValidValues() {
        SudokuBoard board = new SudokuBoard(SudokuBoardTest.BOARD_STRING_UNSOLVED);
        SudokuCell cell = new SudokuCell(4, 5);
        assertEquals(Arrays.asList(new Integer[]{2, 5, 6, 7, 9}), SudokuUtils.getValidValues(board, cell));
        board.setCellValue(4, 7, 7);
        assertEquals(Arrays.asList(new Integer[]{2, 5, 6, 9}), SudokuUtils.getValidValues(board, cell));
    }

    @Test
    public void testToPrettyString() {
        String expected = System.lineSeparator() + "4 . . |. . . |8 . 5 " + System.lineSeparator() +
                ". 3 . |. . . |. . . " + System.lineSeparator() +
                ". . . |7 . . |. . . " + System.lineSeparator() +
                "------+------+------" + System.lineSeparator() +
                ". 2 . |. . . |. 6 . " + System.lineSeparator() +
                ". . . |. 8 . |4 . . " + System.lineSeparator() +
                ". . . |. 1 . |. . . " + System.lineSeparator() +
                "------+------+------" + System.lineSeparator() +
                ". . . |6 . 3 |. 7 . " + System.lineSeparator() +
                "5 . . |2 . . |. . . " + System.lineSeparator() +
                "1 . 4 |. . . |. . . ";
        assertEquals(expected, SudokuUtils.toPrettyString(new SudokuBoard(SudokuBoardTest.BOARD_STRING_UNSOLVED)));

    }
}
