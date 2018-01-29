package com.jagsits.util;

import com.jagsits.service.sudoku.*;
import com.jagsits.service.sudoku.solver.SudokuSolverAlgorithm;
import com.jagsits.service.version.VersionResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class JsonUtilsTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    @Test
    public void testToJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("KEY_1", "VALUE_1");
        map.put("KEY_2", null);
        String actualJson = CharsetUtils.toUTF8(JsonUtils.toJson(map));
        StringBuilder expected = new StringBuilder().append("{").append(LINE_SEPARATOR).append("  \"KEY_1\" : \"VALUE_1\"").append(LINE_SEPARATOR).append("}");
        assertEquals(expected.toString(), actualJson);
    }

    @Test
    public void testFromJson() {
        String jsonString = "{\"KEY_1\":\"VALUE_1\", \"KEY_2\":null}";

        Map map = JsonUtils.fromJson(CharsetUtils.toUTF8(jsonString), Map.class);
        assertFalse(map.isEmpty());
        assertEquals(2, map.size());
        assertEquals("VALUE_1", map.get("KEY_1"));
        assertEquals(null, map.get("KEY_2"));
    }

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
