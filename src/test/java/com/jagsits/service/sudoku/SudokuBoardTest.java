package com.jagsits.service.sudoku;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SudokuBoardTest {

    public static final String BOARD_STRING_UNSOLVED = "4.....8.5.3..........7......2.....6.....8.4......1.......6.3.7.5..2.....1.4......";
    public static final String BOARD_STRING_SOLVED = "417369825632158947958724316825437169791586432346912758289643571573291684164875293";

    @Test
    public void testObject() {
        SudokuBoard board = new SudokuBoard(BOARD_STRING_UNSOLVED);
        SudokuBoard board2 = new SudokuBoard(board);
        assertEquals(BOARD_STRING_UNSOLVED, board.toString());
        assertEquals(BOARD_STRING_UNSOLVED, board2.toString());
        assertEquals(board.hashCode(), board2.hashCode());
        assertTrue(board.equals(board2));

        assertTrue(board.equals(board));
        assertFalse(board.equals(""));
        assertFalse(board.equals(null));
    }

    @Test(expected = AssertionError.class)
    public void testConstructorInvalidValue() {
        new SudokuBoard("1233");
    }

    @Test
    public void testValues() {
        SudokuBoard board = new SudokuBoard(BOARD_STRING_UNSOLVED);
        SudokuCell cell = new SudokuCell(0, 0);
        assertEquals(4, board.getCellValue(cell));
        assertEquals(true, board.isPopulated(cell));
        board.clearValue(cell);
        assertEquals(false, board.isPopulated(cell));
        assertEquals(SudokuBoard.DEFAULT_VALUE, board.getCellValue(cell));
    }

}
