package com.jagsits.service.sudoku;

import org.junit.Test;

import static org.junit.Assert.*;

public class SudukoCellTest {

    @Test
    public void testObject() {
        int R = 5;
        int C = 7;

        SudokuCell cell = new SudokuCell(R, C);
        SudokuCell cell2 = new SudokuCell(R, C);
        assertEquals(cell.toString(), cell2.toString());
        assertEquals(cell.hashCode(), cell2.hashCode());
        assertTrue(cell.equals(cell2));

        assertEquals(R, cell.getR());
        assertEquals(C, cell.getC());

        assertTrue(cell.equals(cell));
        assertFalse(cell.equals(""));
        assertFalse(cell.equals(null));
    }

}
