package com.jagsits.service.sudoku.solver;

import com.jagsits.service.sudoku.SudokuBoard;
import com.jagsits.util.DancingLinks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Adapted from the Hadoop samples for Sudoku solutions to use my abstraction of a SudokuBoard
 */
class KnuthSudokuSolver implements SudokuSolver, NumberOfSolutionsSudokuSolver {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public SudokuSolverAlgorithm getAlgorithm() {
        return SudokuSolverAlgorithm.KNUTH;
    }

    @Override
    public SudokuBoard solve(SudokuBoard sudokuBoard) {
        DancingLinks<ColumnName> model = makeModel(sudokuBoard);
        KnuthSolutionAcceptor solutionAcceptor = new KnuthSolutionAcceptor();
        int results = model.solve(solutionAcceptor);
        if (results > 1) {
            log.warn("HAS MORE THAN ONE SOLUTION!");
        }
        return new SudokuBoard(solutionAcceptor.getSolution());
    }

    @Override
    public int getNumberOfPossibleSolutions(SudokuBoard sudokuBoard) {
        DancingLinks<ColumnName> model = makeModel(sudokuBoard);
        KnuthSolutionAcceptor solutionAcceptor = new KnuthSolutionAcceptor();
        return model.solve(solutionAcceptor);
    }

    private static DancingLinks<ColumnName> makeModel(SudokuBoard board) {

        DancingLinks<ColumnName> model = new DancingLinks<>();
        // create all of the columns constraints
        for (int x = 0; x < SudokuBoard.BOARD_DIMENSION; ++x) {
            for (int num = 1; num <= SudokuBoard.BOARD_DIMENSION; ++num) {
                model.addColumn(new ColumnConstraint(num, x));
            }
        }
        // create all of the row constraints
        for (int y = 0; y < SudokuBoard.BOARD_DIMENSION; ++y) {
            for (int num = 1; num <= SudokuBoard.BOARD_DIMENSION; ++num) {
                model.addColumn(new RowConstraint(num, y));
            }
        }
        // create the square constraints
        for (int x = 0; x < SudokuBoard.SUB_BOARD_DIMENSION; ++x) {
            for (int y = 0; y < SudokuBoard.SUB_BOARD_DIMENSION; ++y) {
                for (int num = 1; num <= SudokuBoard.BOARD_DIMENSION; ++num) {
                    model.addColumn(new SquareConstraint(num, x, y));
                }
            }
        }
        // create the cell constraints
        for (int x = 0; x < SudokuBoard.BOARD_DIMENSION; ++x) {
            for (int y = 0; y < SudokuBoard.BOARD_DIMENSION; ++y) {
                model.addColumn(new CellConstraint(x, y));
            }
        }

        boolean[] rowValues = new boolean[SudokuBoard.BOARD_DIMENSION * SudokuBoard.BOARD_DIMENSION * 4];
        for (int c = 0; c < SudokuBoard.BOARD_DIMENSION; ++c) {
            for (int r = 0; r < SudokuBoard.BOARD_DIMENSION; ++r) {
                if (!board.isPopulated(r, c)) {
                    // try each possible value in the cell
                    for (int num = 1; num <= SudokuBoard.BOARD_DIMENSION; ++num) {
                        model.addRow(generateRow(rowValues, c, r, num));
                    }
                } else {
                    // put the given cell in place
                    model.addRow(generateRow(rowValues, c, r, board.getCellValue(r, c)));
                }
            }
        }
        return model;
    }

    /**
     * Create a row that places num in cell x, y.
     *
     * @param rowValues a scratch pad to mark the bits needed
     * @param x         the horizontal offset of the cell
     * @param y         the vertical offset of the cell
     * @param num       the number to place
     * @return a bitvector of the columns selected
     */
    private static boolean[] generateRow(boolean[] rowValues, int x, int y, int num) {
        // clear the scratch array
        for (int i = 0; i < rowValues.length; ++i) {
            rowValues[i] = false;
        }
        // find the square coordinates
        int xBox = x / SudokuBoard.SUB_BOARD_DIMENSION;
        int yBox = y / SudokuBoard.SUB_BOARD_DIMENSION;
        // mark the column
        rowValues[x * SudokuBoard.BOARD_DIMENSION + num - 1] = true;
        // mark the row
        rowValues[SudokuBoard.BOARD_DIMENSION * SudokuBoard.BOARD_DIMENSION + y * SudokuBoard.BOARD_DIMENSION + num - 1] = true;
        // mark the square
        rowValues[2 * SudokuBoard.BOARD_DIMENSION * SudokuBoard.BOARD_DIMENSION + (xBox * SudokuBoard.SUB_BOARD_DIMENSION + yBox) * SudokuBoard.BOARD_DIMENSION + num - 1] = true;
        // mark the cell
        rowValues[3 * SudokuBoard.BOARD_DIMENSION * SudokuBoard.BOARD_DIMENSION + SudokuBoard.BOARD_DIMENSION * x + y] = true;
        return rowValues;
    }

    /**
     * This interface is a marker class for the columns created for the
     * Sudoku solver.
     */
    protected interface ColumnName {
    }

    /**
     * A constraint that each number can appear just once in a column.
     */
    private static class ColumnConstraint implements ColumnName {
        int num;
        int column;

        ColumnConstraint(int num, int column) {
            this.num = num;
            this.column = column;
        }
    }

    /**
     * A constraint that each number can appear just once in a row.
     */
    private static class RowConstraint implements ColumnName {
        int num;
        int row;

        RowConstraint(int num, int row) {
            this.num = num;
            this.row = row;
        }
    }

    /**
     * A constraint that each number can appear just once in a square.
     */
    private static class SquareConstraint implements ColumnName {
        int num;
        int x;
        int y;

        SquareConstraint(int num, int x, int y) {
            this.num = num;
            this.x = x;
            this.y = y;
        }
    }

    /**
     * A constraint that each cell can only be used once.
     */
    private static class CellConstraint implements ColumnName {
        int x;
        int y;

        CellConstraint(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * An acceptor to get the solutions to the puzzle as they are generated.
     */
    private static class KnuthSolutionAcceptor implements DancingLinks.SolutionAcceptor<ColumnName> {
        private SudokuBoard solution;

        @Override
        public void solution(List<List<ColumnName>> solution) {
            int[][] result = new int[SudokuBoard.BOARD_DIMENSION][SudokuBoard.BOARD_DIMENSION];

            // go through the rows selected in the model and build a picture of the solution.
            for (List<ColumnName> row : solution) {
                int x = SudokuBoard.DEFAULT_VALUE;
                int y = SudokuBoard.DEFAULT_VALUE;
                int num = SudokuBoard.DEFAULT_VALUE;
                for (ColumnName item : row) {
                    if (item instanceof ColumnConstraint) {
                        x = ((ColumnConstraint) item).column;
                        num = ((ColumnConstraint) item).num;
                    } else if (item instanceof RowConstraint) {
                        y = ((RowConstraint) item).row;
                    }
                }
                result[y][x] = num;
            }
            this.solution = new SudokuBoard(result);
        }

        public SudokuBoard getSolution() {
            return solution;
        }
    }

}
