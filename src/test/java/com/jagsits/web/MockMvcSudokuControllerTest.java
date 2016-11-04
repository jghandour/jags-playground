package com.jagsits.web;

import com.jagsits.service.sudoku.SudokuBoardTest;
import com.jagsits.service.sudoku.solver.SudokuSolverAlgorithm;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebAppConfiguration
@EnableAutoConfiguration
public class MockMvcSudokuControllerTest extends BaseMockMvcSpringControllerTest {

    @Test
    public void testSolve() throws Exception {
        String algorithm = SudokuSolverAlgorithm.KNUTH.toString();
        MvcResult mvcResult;
        Map responseMap;

        // Specify Algorithm
        mvcResult = mockMvc.perform(get("/api/v1/sudoku/solve")
                .with(csrf().asHeader())
                .param(SudokuController.REQUEST_PARAM_ALGORITHM, algorithm)
                .param(SudokuController.REQUEST_PARAM_BOARD, SudokuBoardTest.BOARD_STRING_UNSOLVED))
                .andReturn();
        responseMap = getResultMap(mvcResult);
        assertEquals(SudokuBoardTest.BOARD_STRING_SOLVED, responseMap.get(SudokuController.RESPONSE_BOARD));
        assertEquals(algorithm, responseMap.get(SudokuController.RESPONSE_ALGORITHM));
        assertNotNull(responseMap.get(SudokuController.RESPONSE_TIME));
        assertNotNull(responseMap.get(SudokuController.RESPONSE_DIFFICULTY));

        // No algorithm
        mvcResult = mockMvc.perform(get("/api/v1/sudoku/solve")
                .with(csrf().asHeader())
                .param(SudokuController.REQUEST_PARAM_BOARD, SudokuBoardTest.BOARD_STRING_UNSOLVED))
                .andReturn();
        responseMap = getResultMap(mvcResult);
        assertEquals(SudokuBoardTest.BOARD_STRING_SOLVED, responseMap.get(SudokuController.RESPONSE_BOARD));
        assertNotNull(responseMap.get(SudokuController.RESPONSE_ALGORITHM));
        assertNotNull(responseMap.get(SudokuController.RESPONSE_TIME));
        assertNotNull(responseMap.get(SudokuController.RESPONSE_DIFFICULTY));
    }

    @Test
    public void testRandom() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/sudoku/random").with(csrf().asHeader())).andReturn();
        String result = getResultString(mvcResult);
        assertNotNull(result);
    }

    @Test
    public void testAlgorithms() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/sudoku/algorithms").with(csrf().asHeader())).andReturn();
        List result = getResultList(mvcResult);
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testDifficultyLevels() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/sudoku/difficultyLevels").with(csrf().asHeader())).andReturn();
        List result = getResultList(mvcResult);
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }


}
