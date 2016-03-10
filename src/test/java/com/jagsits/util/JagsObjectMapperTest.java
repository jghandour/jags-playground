package com.jagsits.util;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jagsits.service.sudoku.*;
import com.jagsits.service.sudoku.solver.SudokuSolverAlgorithm;
import com.jagsits.service.version.VersionResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static com.jagsits.util.CharsetUtils.toUTF8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JagsObjectMapperTest {

    private static ObjectMapper objectMapper = new JagsObjectMapper();

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

    private static class JsonUtils {
        public static <T> T fromJson(String jsonContent, Class<T> clazz) {
            return fromJson(toUTF8(jsonContent), clazz);
        }

        private static <T> T fromJson(byte[] jsonContent, Class<T> clazz) {
            T result = null;
            if (ArrayUtils.isNotEmpty(jsonContent)) {
                try {
                    result = objectMapper.readValue(jsonContent, clazz);
                } catch (Exception e) {
                    throw new RuntimeException("Error converting from JSON!", e);
                }
            }
            return result;
        }

        public static String toJsonString(Object object) {
            return toUTF8(toJsonBytes(object, objectMapper));
        }

        private static byte[] toJsonBytes(Object object, ObjectMapper objectMapper) {
            byte[] result = null;
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, JsonEncoding.UTF8);
                objectMapper.writeValue(generator, object);
                result = outputStream.toByteArray();
            } catch (Exception e) {
                throw new RuntimeException("Error converting to JSON!", e);
            }
            return result;
        }

    }


}
