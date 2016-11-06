package com.jagsits.util;

import com.jagsits.service.sudoku.*;
import com.jagsits.service.sudoku.solver.SudokuSolverAlgorithm;
import com.jagsits.service.version.VersionResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JagsObjectMapperHolderTest {

    @Test
    public void testVersionResponse() {
        VersionResponse original = new VersionResponse("ABC", "123");
        String serialized = JsonUtils.toJsonString(original);
        assertNotNull(serialized);
        VersionResponse deserialized = JsonUtils.fromJson(serialized, VersionResponse.class);
        assertEquals(original, deserialized);
    }

    @Test
    public void testSudokoCell() {
        SudokuCell original = new SudokuCell(4, 1);
        String serialized = JsonUtils.toJsonString(original);
        assertNotNull(serialized);
        SudokuCell deserialized = JsonUtils.fromJson(serialized, SudokuCell.class);
        assertEquals(original, deserialized);
    }

    @Test
    public void testSudokoBoard() {
        SudokuBoard original = new SudokuBoard(SudokuBoardTest.BOARD_STRING_SOLVED);
        String serialized = JsonUtils.toJsonString(original);
        assertNotNull(serialized);
        SudokuBoard deserialized = JsonUtils.fromJson(serialized, SudokuBoard.class);
        assertEquals(original, deserialized);
    }

    @Test
    public void testSudokuResponse() {
        SudokuResponse original = new SudokuResponse(new SudokuBoard(SudokuBoardTest.BOARD_STRING_SOLVED), SudokuSolverAlgorithm.BRUTE_FORCE, SudokuDifficultyLevel.EVIL, 100L);
        String serialized = JsonUtils.toJsonString(original);
        assertNotNull(serialized);
        SudokuResponse deserialized = JsonUtils.fromJson(serialized, SudokuResponse.class);
        assertEquals(original, deserialized);
    }

}
