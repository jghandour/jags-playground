package com.jagsits.web;

import com.jagsits.service.sudoku.SudokuDifficultyLevel;
import com.jagsits.service.sudoku.SudokuResponse;
import com.jagsits.service.sudoku.SudokuService;
import com.jagsits.service.sudoku.solver.SudokuSolverAlgorithm;
import com.jagsits.util.JagsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(SudokuController.MAPPING)
public class SudokuController extends BaseController {

    static final String MAPPING = MAPPING_API_PREFIX + "sudoku/";

    static final String MAPPING_SOLVE = "solve";
    static final String MAPPING_RANDOM = "random";
    static final String MAPPING_ALGORITHMS = "algorithms";
    static final String MAPPING_DIFFICULTY = "difficultyLevels";

    static final String REQUEST_PARAM_ALGORITHM = "algorithm";
    static final String REQUEST_PARAM_BOARD = "board";
    static final String REQUEST_PARAM_DIFFICULTY = "difficultyLevel";

    static final String RESPONSE_BOARD = "board";
    static final String RESPONSE_TIME = "time";
    static final String RESPONSE_ALGORITHM = "algorithm";
    static final String RESPONSE_DIFFICULTY = "difficultyLevel";

    @Autowired
    private SudokuService sudokuService;

    @RequestMapping(value = MAPPING_SOLVE, method = RequestMethod.GET)
    public Object solve(@RequestParam(value = REQUEST_PARAM_ALGORITHM, required = false) String algorithm,
                        @RequestParam(value = REQUEST_PARAM_BOARD, required = true) String board) {

        SudokuResponse sudokuResult = sudokuService.solve(algorithm, board);
        return buildResponse(buildResultMap(sudokuResult));
    }

    private static Map<String, Object> buildResultMap(SudokuResponse sudokuResult) {
        Map<String, Object> result = new HashMap<>();
        result.put(RESPONSE_BOARD, sudokuResult.getSudokuBoard().toString());
        result.put(RESPONSE_TIME, sudokuResult.getTime());
        result.put(RESPONSE_DIFFICULTY, sudokuResult.getSudokuDifficultyLevel());
        result.put(RESPONSE_ALGORITHM, sudokuResult.getSudokuSolverAlgorithm());
        return result;
    }

    @RequestMapping(value = MAPPING_RANDOM, method = RequestMethod.GET)
    public Object random(@RequestParam(value = REQUEST_PARAM_DIFFICULTY, required = false, defaultValue = "MEDIUM") SudokuDifficultyLevel difficultyLevel) {
        return buildResponse(sudokuService.createBoard(difficultyLevel).toString());
    }

    @RequestMapping(value = MAPPING_ALGORITHMS, method = RequestMethod.GET)
    public Object algorithms() {
        return buildResponse(JagsUtils.getEnumNames(SudokuSolverAlgorithm.class));
    }

    @RequestMapping(value = MAPPING_DIFFICULTY, method = RequestMethod.GET)
    public Object levels() {
        return buildResponse(JagsUtils.getEnumNames(SudokuDifficultyLevel.class));
    }

}
