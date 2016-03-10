package com.jagsits.util;

import com.jagsits.service.sudoku.solver.SudokuSolverAlgorithm;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

import static junit.framework.TestCase.assertEquals;

public class JagsUtilsTest {

    @Test
    public void testTranspose() {
        int[][] input = new int[5][10];
        for (int r = 0; r < input.length; r++) {
            for (int c = 0; c < input[0].length; c++) {
                input[r][c] = RandomUtils.nextInt(1, 10000);
            }
        }

        int[][] transposed = JagsUtils.transpose(input);
        assertEquals(input.length, transposed[0].length);
        assertEquals(input[0].length, transposed.length);

        // Validate
        for (int r = 0; r < transposed.length; r++) {
            for (int c = 0; c < transposed[0].length; c++) {
                assertEquals(transposed[r][c], input[c][r]);
            }
        }
    }

    @Test
    public void testGetEnumValues() {
        Collection collection = JagsUtils.getEnumNames(SudokuSolverAlgorithm.class);
        Assert.assertTrue(CollectionUtils.isNotEmpty(collection));
    }

    @Test
    public void testRepeatStringSuccess() {
        assertEquals("A", JagsUtils.repeatString("A", 1));
        assertEquals("ABABABAB", JagsUtils.repeatString("AB", 4));
        assertEquals("", JagsUtils.repeatString("", 100));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRepeatStringException1() {
        JagsUtils.repeatString("A", -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRepeatStringException2() {
        JagsUtils.repeatString(null, 10);
    }

}
