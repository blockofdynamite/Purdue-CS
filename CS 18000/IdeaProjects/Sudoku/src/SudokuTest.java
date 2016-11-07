import junit.framework.Assert;
import junit.framework.TestCase;

public class SudokuTest extends TestCase {

    public void testBoard() {
        int[][] board = {{0, 6, 0, 1, 0, 4, 0, 5, 0},
                {0, 0, 8, 3, 0, 5, 6, 0, 0},
                {2, 0, 0, 0, 0, 0, 0, 0, 1},
                {8, 0, 0, 4, 0, 7, 0, 0, 6},
                {0, 0, 6, 0, 0, 0, 3, 0, 0},
                {7, 0, 0, 9, 0, 1, 0, 0, 4},
                {5, 0, 0, 0, 0, 0, 0, 0, 2},
                {0, 0, 7, 2, 0, 6, 9, 0, 0},
                {0, 4, 0, 5, 0, 8, 0, 7, 0}};
        Sudoku testing = new Sudoku(board);
        int[][] doubleBoard = testing.board();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                assertTrue(doubleBoard[i][j] == board[i][j]);
            }
        }

        Sudoku derp = new Sudoku();
        int[][] derp2 = testing.board();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                assertTrue(doubleBoard[i][j] == board[i][j]);
            }
        }
    }

    public void testCandidates() {
        int[][] board = {{0, 6, 0, 1, 0, 4, 0, 5, 0},
                {0, 0, 8, 3, 0, 5, 6, 0, 0},
                {2, 0, 0, 0, 0, 0, 0, 0, 1},
                {8, 0, 0, 4, 0, 7, 0, 0, 6},
                {0, 0, 6, 0, 0, 0, 3, 0, 0},
                {7, 0, 0, 9, 0, 1, 0, 0, 4},
                {5, 0, 0, 0, 0, 0, 0, 0, 2},
                {0, 0, 7, 2, 0, 6, 9, 0, 0},
                {0, 4, 0, 5, 0, 8, 0, 7, 0}};
        Sudoku testing = new Sudoku(board);
        boolean[] test = testing.candidates(6, 6);
        boolean[] toCompare = {false, true, false, false, true, false, false, false, true, false};
        for (int i = 0; i < test.length; i++) {
            assertTrue(test[i] == toCompare[i]);
        }
    }

    public void testCandidates2() {
        int[][] board = {{0, 6, 0, 1, 0, 4, 0, 5, 0},
                {0, 0, 8, 3, 0, 5, 6, 0, 0},
                {2, 0, 0, 0, 0, 0, 0, 0, 1},
                {8, 0, 0, 4, 0, 7, 0, 0, 6},
                {0, 0, 6, 0, 0, 0, 3, 0, 0},
                {7, 0, 0, 9, 0, 1, 0, 0, 4},
                {5, 0, 0, 0, 0, 0, 0, 0, 2},
                {0, 0, 7, 2, 0, 6, 9, 0, 0},
                {0, 4, 0, 5, 0, 8, 0, 7, 0}};
        Sudoku testing = new Sudoku(board);
        boolean[] test = testing.candidates(0, 1);
        boolean[] toCompare = {false, false, false, false, false, false, false, false, false, false};
        for (int i = 0; i < test.length; i++) {
            assertTrue(test[i] == toCompare[i]);
        }
    }

    public void testCandidates3() {
        int[][] board = {{0, 6, 0, 1, 0, 4, 0, 5, 0},
                {0, 0, 8, 3, 0, 5, 6, 0, 0},
                {2, 0, 0, 0, 0, 0, 0, 0, 1},
                {8, 0, 0, 4, 0, 7, 0, 0, 6},
                {0, 0, 6, 0, 0, 0, 3, 0, 0},
                {7, 0, 0, 9, 0, 1, 0, 0, 4},
                {5, 0, 0, 0, 0, 0, 0, 0, 2},
                {0, 0, 7, 2, 0, 6, 9, 0, 0},
                {0, 4, 0, 5, 0, 8, 0, 7, 0}};
        Sudoku testing = new Sudoku(board);
        boolean[] test = testing.candidates(0, 2);
        boolean[] toCompare = {false, false, false, true, false, false, false, false, false, true};
        for (int i = 0; i < test.length; i++) {
            assertTrue(test[i] == toCompare[i]);
        }
    }

    public void testCandidates4() {
        int[][] board = {{0, 6, 0, 1, 0, 4, 0, 5, 0},
                {0, 0, 8, 3, 0, 5, 6, 0, 0},
                {2, 0, 0, 0, 0, 0, 0, 0, 1},
                {8, 0, 0, 4, 0, 7, 0, 0, 6},
                {0, 0, 6, 0, 0, 0, 3, 0, 0},
                {7, 0, 0, 9, 0, 1, 0, 0, 4},
                {5, 0, 0, 0, 0, 0, 0, 0, 2},
                {0, 0, 7, 2, 0, 6, 9, 0, 0},
                {0, 4, 0, 5, 0, 8, 0, 7, 0}};
        Sudoku testing = new Sudoku(board);
        boolean[] test = testing.candidates(0, 4);
        boolean[] toCompare = {false, false, true, false, false, false, false, true, true, true};
        for (int i = 0; i < test.length; i++) {
            assertTrue(test[i] == toCompare[i]);
        }
    }

    public void testIsSolved() {
        int[][] board = {{4, 7, 2, 6, 3, 8, 1, 9, 5},
                {8, 3, 9, 1, 5, 2, 6, 4, 7},
                {1, 6, 5, 7, 4, 9, 8, 2, 3},
                {2, 1, 8, 4, 9, 7, 3, 5, 6},
                {3, 9, 7, 8, 6, 5, 2, 1, 4},
                {5, 4, 6, 3, 2, 1, 7, 8, 9},
                {6, 8, 3, 9, 1, 4, 5, 7, 2},
                {9, 5, 1, 2, 7, 6, 4, 3, 8},
                {7, 2, 4, 5, 8, 3, 9, 6, 1}};
        Sudoku testing = new Sudoku(board);
        assertTrue(testing.isSolved());

    }

    public void testIsSolved2() {
        int[][] board = {{4, 7, 0, 6, 3, 8, 1, 9, 5},
                {8, 3, 9, 1, 5, 2, 6, 4, 7},
                {1, 6, 5, 7, 4, 9, 8, 2, 3},
                {2, 1, 8, 4, 9, 7, 3, 5, 6},
                {3, 9, 7, 8, 6, 5, 2, 1, 4},
                {5, 4, 6, 3, 2, 1, 7, 8, 9},
                {6, 8, 3, 9, 1, 4, 5, 7, 2},
                {9, 5, 1, 2, 7, 6, 4, 3, 8},
                {7, 2, 4, 5, 8, 3, 9, 6, 1}};
        Sudoku testing = new Sudoku(board);
        assertTrue(!testing.isSolved());

    }

    public void testNakedSingles() {
        int[][] board = {{0, 6, 0, 1, 0, 4, 0, 5, 0},
                {0, 0, 8, 3, 0, 5, 6, 0, 0},
                {2, 0, 0, 0, 0, 0, 0, 0, 1},
                {8, 0, 0, 4, 0, 7, 0, 0, 6},
                {0, 0, 6, 0, 0, 0, 3, 0, 0},
                {7, 0, 0, 9, 0, 1, 0, 0, 4},
                {5, 0, 0, 0, 0, 0, 0, 0, 2},
                {0, 0, 7, 2, 0, 6, 9, 0, 0},
                {0, 4, 0, 5, 0, 8, 0, 7, 0}};
        Sudoku testing = new Sudoku(board);
        assertTrue(testing.nakedSingles());
    }

    public void testHiddenSingles() {
        int[][] board = {{0, 6, 0, 1, 0, 4, 0, 5, 0},
                {0, 0, 8, 3, 0, 5, 6, 0, 0},
                {2, 0, 0, 0, 0, 0, 0, 0, 1},
                {8, 0, 0, 4, 0, 7, 0, 0, 6},
                {0, 0, 6, 0, 0, 0, 3, 0, 0},
                {7, 0, 0, 9, 0, 1, 0, 0, 4},
                {5, 0, 0, 0, 0, 0, 0, 0, 2},
                {0, 0, 7, 2, 0, 6, 9, 0, 0},
                {0, 4, 0, 5, 0, 8, 0, 7, 0}};
        Sudoku testing = new Sudoku(board);
        assertTrue(testing.hiddenSingles());
    }

    public void testSolveAndIsSolved() {
        int[][] board = {{0, 6, 0, 1, 0, 4, 0, 5, 0},
                {0, 0, 8, 3, 0, 5, 6, 0, 0},
                {2, 0, 0, 0, 0, 0, 0, 0, 1},
                {8, 0, 0, 4, 0, 7, 0, 0, 6},
                {0, 0, 6, 0, 0, 0, 3, 0, 0},
                {7, 0, 0, 9, 0, 1, 0, 0, 4},
                {5, 0, 0, 0, 0, 0, 0, 0, 2},
                {0, 0, 7, 2, 0, 6, 9, 0, 0},
                {0, 4, 0, 5, 0, 8, 0, 7, 0}};
        Sudoku testing = new Sudoku(board);
        testing.solve();
    }

}