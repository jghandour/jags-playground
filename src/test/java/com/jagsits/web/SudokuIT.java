package com.jagsits.web;

import com.jagsits.service.sudoku.SudokuBoardTest;
import com.jagsits.service.sudoku.SudokuDifficultyLevel;
import com.jagsits.service.sudoku.solver.SudokuSolverAlgorithm;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SudokuIT extends BaseIT {

    @Test
    public void testSolve() {
        Map result;
        String algorithm = SudokuSolverAlgorithm.KNUTH.toString();

        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put(SudokuController.REQUEST_PARAM_ALGORITHM, algorithm);
        urlVariables.put(SudokuController.REQUEST_PARAM_BOARD, SudokuBoardTest.BOARD_STRING_UNSOLVED);

        // With Algorithm
        result = getResultMap(doGet(buildPathSuffix(SudokuController.MAPPING, SudokuController.MAPPING_SOLVE), urlVariables));
        assertEquals(SudokuBoardTest.BOARD_STRING_SOLVED, result.get(SudokuController.RESPONSE_BOARD));
        assertEquals(algorithm, result.get(SudokuController.RESPONSE_ALGORITHM));
        assertNotNull(result.get(SudokuController.RESPONSE_TIME));
        assertNotNull(result.get(SudokuController.RESPONSE_DIFFICULTY));

        // No Algorithm
        urlVariables.remove(SudokuController.REQUEST_PARAM_ALGORITHM);
        result = getResultMap(doGet(buildPathSuffix(SudokuController.MAPPING, SudokuController.MAPPING_SOLVE), urlVariables));
        assertEquals(SudokuBoardTest.BOARD_STRING_SOLVED, result.get(SudokuController.RESPONSE_BOARD));
        assertNotNull(result.get(SudokuController.RESPONSE_ALGORITHM));
        assertNotNull(result.get(SudokuController.RESPONSE_TIME));
        assertNotNull(result.get(SudokuController.RESPONSE_DIFFICULTY));

        // Invalid Board - expect exception
        urlVariables.put(SudokuController.REQUEST_PARAM_BOARD, "invalid");
        result = doGet(buildPathSuffix(SudokuController.MAPPING, SudokuController.MAPPING_SOLVE), urlVariables);
        assertEquals(500, result.get(BaseController.RESPONSE_STATUS));

    }

    @Test
    public void testRandom() {
        String result = getResultString(doGet(buildPathSuffix(SudokuController.MAPPING, SudokuController.MAPPING_RANDOM)));
        assertNotNull(result);
    }

    @Test
    public void testAlgorithms() {
        List result = getResultList(doGet(buildPathSuffix(SudokuController.MAPPING, SudokuController.MAPPING_ALGORITHMS)));
        assertNotNull(result);
        assertEquals(SudokuSolverAlgorithm.values().length, result.size());
    }

    @Test
    public void testDifficultyLevels() {
        List result = getResultList(doGet(buildPathSuffix(SudokuController.MAPPING, SudokuController.MAPPING_DIFFICULTY)));
        assertNotNull(result);
        assertEquals(SudokuDifficultyLevel.values().length, result.size());
    }

}
