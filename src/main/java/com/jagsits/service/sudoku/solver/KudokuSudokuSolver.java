package com.jagsits.service.sudoku.solver;

import com.jagsits.service.sudoku.SudokuBoard;

/**
 * Based on https://github.com/attractivechaos/plb/tree/master/sudoku
 * <p/>
 * For Sudoku, there are 9x9x9=729 possible choices (9 numbers to choose for
 * each cell in a 9x9 grid), and 4x9x9=324 constraints with each constraint
 * representing a set of choices that are mutually conflictive with each other.
 * The 324 constraints are classified into 4 categories:
 * <p/>
 * 1. row-column where each cell contains only one number
 * 2. box-number where each number appears only once in one 3x3 box
 * 3. row-number where each number appears only once in one row
 * 4. col-number where each number appears only once in one column
 * <p/>
 * Each category consists of 81 constraints. We number these constraints from 0
 * to 323. In this program, for example, constraint 0 requires that the (0,0)
 * cell contains only one number; constraint 81 requires that number 1 appears
 * only once in the upper-left 3x3 box; constraint 162 requires that number 1
 * appears only once in row 1; constraint 243 requires that number 1 appears
 * only once in column 1.
 * <p/>
 * Noting that a constraint is a subset of choices, we may represent a
 * constraint with a binary vector of 729 elements. Thus we have a 729x324
 * binary matrix M with M(r,c)=1 indicating the constraint c involves choice r.
 * Solving a Sudoku is reduced to finding a subset of choices such that no
 * choices are present in the same constaint. This is equivalent to finding the
 * minimal subset of choices intersecting all constraints, a minimum hitting
 * set problem or a eqivalence of the exact cover problem.
 * <p/>
 * The 729x324 binary matrix is a sparse matrix, with each row containing 4
 * non-zero elements and each column 9 non-zero elements. In practical
 * implementation, we store the coordinate of non-zero elements instead of
 * the binary matrix itself. We use a binary row vector to indicate the
 * constraints that have not been used and use a column vector to keep the
 * number of times a choice has been forbidden. When we set a choice, we will
 * use up 4 constraints and forbid other choices in the 4 constraints. When we
 * make wrong choices, we will find an unused constraint with all choices
 * forbidden, in which case, we have to backtrack to make new choices. Once we
 * understand what the 729x324 matrix represents, the backtracking algorithm
 * itself is easy.
 * <p/>
 * A major difference between the algorithm implemented here and Guenter
 * Stertenbrink's suexco.c lies in how to count the number of the available
 * choices for each constraint. Suexco.c computes the count with a loop, while
 * the algorithm here keeps the count in an array. The latter is a little more
 * complex to implement as we have to keep the counts synchronized all the time,
 * but it is 50-100% faster, depending on the input.
 */
public class KudokuSudokuSolver implements SudokuSolver {

    private static int[][] R;
    private static int[][] C;

    static {
        generateMatrix();
    }

    private static void generateMatrix() {
        R = new int[324][9];
        C = new int[729][4];
        int[] nr = new int[324];
        int i;
        int j;
        int k;
        int r;
        int c;
        int c2;
        for (i = r = 0; i < 9; ++i) // generate c[729][4]
            for (j = 0; j < 9; ++j)
                for (k = 0; k < 9; ++k) { // this "9" means each cell has 9 possible numbers
                    C[r][0] = 9 * i + j;                  // row-column constraint
                    C[r][1] = (i / 3 * 3 + j / 3) * 9 + k + 81; // box-number constraint
                    C[r][2] = 9 * i + k + 162;            // row-number constraint
                    C[r][3] = 9 * j + k + 243;            // col-number constraint
                    ++r;
                }
        for (c = 0; c < 324; ++c) {
            nr[c] = 0;
        }
        for (r = 0; r < 729; ++r) // generate r[][] from c[][]
            for (c2 = 0; c2 < 4; ++c2) {
                k = C[r][c2];
                R[k][nr[k]++] = r;
            }
    }

    @Override
    public SudokuSolverAlgorithm getAlgorithm() {
        return SudokuSolverAlgorithm.KUDOKU;
    }

    @Override
    public SudokuBoard solve(SudokuBoard sudokuBoard) {
        Kudoku kudoku = new Kudoku();
        String result = kudoku.solve(sudokuBoard.toString());
        return new SudokuBoard(result);
    }

    private static class Kudoku {

        // solve a Sudoku; _s is the standard dot/number representation
        public String solve(String boardString) {
            StringBuilder sb = new StringBuilder(81);
            int i; // dir=1: forward; dir=-1: backtrack
            int j;
            int r;
            int c;
            int r2;
            int dir;
            int cand;
            int min;
            int hints = 0;
            int[] sr = new int[729];
            int[] cr = new int[81];
            int[] sc = new int[324];
            int[] cc = new int[81];
            int[] out = new int[81];
            for (r = 0; r < 729; ++r) {
                sr[r] = 0;
            } // no row is forbidden
            for (c = 0; c < 324; ++c) {
                sc[c] = 9;
            } // 9 allowed choices; no constraint has been used
            for (i = 0; i < 81; ++i) {
                int a = boardString.charAt(i) >= '1' && boardString.charAt(i) <= '9' ? boardString.codePointAt(i) - '1' : -1; // number from -1 to 8
                if (a >= 0) {
                    sd_update(sr, sc, i * 9 + a, 1);
                } // set the choice
                if (a >= 0) {
                    ++hints;
                } // count the number of hints
                cr[i] = cc[i] = -1;
                out[i] = a;
            }
            i = 0;
            dir = 1;
            cand = 655360;
            for (; ; ) {
                while (i >= 0 && i < 81 - hints) { // maximum 81-hints steps
                    if (dir == 1) {
                        min = cand >> 16;
                        cc[i] = cand & 0xffff;
                        if (min > 1) {
                            for (c = 0; c < 324; ++c) {
                                if (sc[c] < min) {
                                    min = sc[c];
                                    cc[i] = c; // choose the top constraint
                                    if (min <= 1) {
                                        break; // this is for acceleration; slower without this line
                                    }
                                }
                            }
                        }
                        if (min == 0 || min == 10) {
                            cr[i--] = dir = -1; // backtrack
                        }
                    }
                    c = cc[i];
                    if (dir == -1 && cr[i] >= 0) {
                        sd_update(sr, sc, R[c][cr[i]], -1); // revert the choice
                    }
                    for (r2 = cr[i] + 1; r2 < 9; ++r2) // search for the choice to make
                        if (sr[R[c][r2]] == 0) {
                            break; // found if the state equals 0
                        }
                    if (r2 < 9) {
                        cand = sd_update(sr, sc, R[c][r2], 1); // set the choice
                        cr[i++] = r2;
                        dir = 1; // moving forward
                    } else cr[i--] = dir = -1; // backtrack
                }
                if (i < 0) {
                    break;
                }
                char[] y = new char[81];
                for (j = 0; j < 81; ++j) {
                    y[j] = (char) (out[j] + '1');
                }
                for (j = 0; j < i; ++j) {
                    r = R[cc[j]][cr[j]];
                    y[r / 9] = (char) (r % 9 + '1');
                }
                sb.append(new String(y));
                --i;
                dir = -1; // backtrack
            }
            return sb.toString();
        }

        private int sd_update(int[] sr, int[] sc, int r, int v) {
            int c2;
            int min = 10;
            int min_c = 0;
            for (c2 = 0; c2 < 4; ++c2) {
                sc[C[r][c2]] += v << 7;
            }
            for (c2 = 0; c2 < 4; ++c2) { // update # available choices
                int r2, rr, cc2, c = C[r][c2];
                if (v > 0) { // move forward
                    for (r2 = 0; r2 < 9; ++r2) {
                        if (sr[rr = R[c][r2]]++ != 0) {
                            continue; // update the row status
                        }
                        for (cc2 = 0; cc2 < 4; ++cc2) {
                            int cc = C[rr][cc2];
                            if (--sc[cc] < min) { // update # allowed choices
                                min = sc[cc];
                                min_c = cc; // register the minimum number
                            }
                        }
                    }
                } else { // revert
                    int[] p;
                    for (r2 = 0; r2 < 9; ++r2) {
                        if (--sr[rr = R[c][r2]] != 0) {
                            continue; // update the row status
                        }
                        p = C[rr];
                        ++sc[p[0]];
                        ++sc[p[1]];
                        ++sc[p[2]];
                        ++sc[p[3]]; // update the count array
                    }
                }
            }
            return min << 16 | min_c; // return the col that has been modified and with the minimal available choices
        }
    }
}