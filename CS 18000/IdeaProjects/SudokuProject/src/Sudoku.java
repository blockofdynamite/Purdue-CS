/**
 * CS180 - Project04 - Sudoku
 * <p/>
 * This class will solve a Sudoku puzzle.
 *
 * @author hughe127
 * @author ispooner
 * @lab 802 - hughe127
 * @lab 805 - ispooner
 * @date 03/11/14
 */

public class Sudoku {

    private int[][] sudokuBoard;
    private boolean[][][] boardCandidates = new boolean[9][9][10];

    /**
     * Makes a zero'd out board
     */
    public Sudoku() {
        int board[][] = new int[9][9];
        new Sudoku(board);
    }

    /**
     * Initializes the board from the int[][] passed
     *
     * @param board - a 9 x 9 array that will initialize the board.
     */
    public Sudoku(int[][] board) {
        sudokuBoard = board.clone();
    }

    /**
     * Returns a copy of the board
     *
     * @return sudokuBoard
     */
    public int[][] board() {
        int[][] copyOfBoard = sudokuBoard.clone();
        return copyOfBoard;
    }

    /**
     * This method returns if the board is solved.
     *
     * @return if board is solved
     */
    public boolean isSolved() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudokuBoard[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * I have a feeling Ian will call me an idiot for this, but it works!
     * TODO FLIP ALL I'S AND J'S
     *
     * @param row
     * @param column
     * @return boolean array of candidates, from 0 -> 9
     */
    public boolean[] candidates(int row, int column) {
        boolean[] toReturn = new boolean[10];
        System.out.println(row + " down " + column + " over");

        row--;
        column--;

        if (sudokuBoard[row][column] != 0) {
            for (int i = 0; i < 10; i++) toReturn[i] = false;
            return toReturn;
        }
        //initialize to zero
        for (int i = 0; i < 10; i++) {
            toReturn[i] = true;
        }
        toReturn[0] = false;
        //Iterate through row
        for (int i = 0; i < 9; i++) {
            if (sudokuBoard[row][i] != 0) {
                toReturn[sudokuBoard[row][i]] = false;
            }
        }
        //Iterate through column
        for (int i = 0; i < 9; i++) {
            if (sudokuBoard[i][column] != 0) {
                toReturn[sudokuBoard[i][column]] = false;
            }
        }
        //Iterate through box
        //Left column
        if (column < 3) {
            //Left column, top box
            if (row < 3) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (sudokuBoard[j][i] != 0) {
                            toReturn[sudokuBoard[j][i]] = false;
                        }
                    }
                }
            }
            //left column, middle box
            else if (row < 6) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 3; j < 6; j++) {
                        if (sudokuBoard[j][i] != 0) {
                            toReturn[sudokuBoard[j][i]] = false;
                        }
                    }
                }
            }
            //left column, bottom box
            else if (row < 9) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 6; j < 9; j++) {
                        if (sudokuBoard[j][i] != 0) {
                            toReturn[sudokuBoard[j][i]] = false;
                        }
                    }
                }
            }
        }
        //middle column
        else if (column < 6) {
            //middle column, top box
            if (row < 3) {
                for (int i = 3; i < 6; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (sudokuBoard[j][i] != 0) {
                            toReturn[sudokuBoard[j][i]] = false;
                        }
                    }
                }
            }
            //middle column, middle box
            else if (row < 6) {
                for (int i = 3; i < 6; i++) {
                    for (int j = 3; j < 6; j++) {
                        if (sudokuBoard[j][i] != 0) {
                            toReturn[sudokuBoard[j][i]] = false;
                        }
                    }
                }
            }
            //middle column, bottom box
            else if (row < 9) {
                for (int i = 3; i < 6; i++) {
                    for (int j = 6; j < 9; j++) {
                        if (sudokuBoard[j][i] != 0) {
                            toReturn[sudokuBoard[j][i]] = false;
                            System.out.println("Test");

                        }
                    }
                }
            }
        }
        //right column
        else if (column < 9) {
            //right, top
            if (row < 3) {
                for (int i = 6; i < 9; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (sudokuBoard[j][i] != 0) {
                            toReturn[sudokuBoard[j][i]] = false;
                        }
                    }
                }
            }
            //right, middle
            else if (row < 6) {
                for (int i = 6; i < 9; i++) {
                    for (int j = 3; j < 6; j++) {
                        if (sudokuBoard[j][i] != 0) {
                            toReturn[sudokuBoard[j][i]] = false;
                        }
                    }
                }
            }
            //right, bottom
            else if (row < 9) {
                for (int i = 6; i < 9; i++) {
                    for (int j = 6; j < 9; j++) {
                        if (sudokuBoard[j][i] != 0) {
                            toReturn[sudokuBoard[j][i]] = false;
                        }
                    }
                }
            }
        }
        return toReturn;
    }

    /**
     * TODO
     * <p/>
     * Solves the board.
     */
    public void solve() {
        while (!isSolved() && (nakedSingles() || hiddenSingles())) {

        }
    }

    /**
     * TODO Fix?
     * Tests to see if there's only one possible candidate for a box, and sets it if true;
     *
     * @return true if set value, else false
     */
    public boolean nakedSingles() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudokuBoard[i][j] == 0) {
                    boolean[] toTest = this.candidates(i + 1, j + 1);
                    int toCatch = 0;
                    int counter = 0;
                    for (int k = 1; k < toTest.length; k++) {
                        if (toTest[k]) {
                            counter++;
                            toCatch = k;
                        }
                    }
                    if (counter == 1) {
                        sudokuBoard[i][j] = toCatch;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * TODO I DON'T EVEN
     * Tests to see if there's only one number left in a row or column, and sets the last box to that value;
     *
     * @return true if set, else false
     */
    public boolean hiddenSingles() {
        return false;
    }

    public static void main(String[] args) {
        int[][] board = {{0, 6, 0, 1, 0, 4, 0, 5, 0},
                {0, 0, 8, 3, 0, 5, 6, 0, 0},
                {2, 0, 0, 0, 0, 0, 0, 0, 1},
                {8, 0, 0, 4, 0, 7, 0, 0, 6},
                {0, 0, 6, 0, 0, 0, 3, 0, 0},
                {7, 0, 0, 9, 0, 1, 0, 0, 4},
                {5, 0, 0, 0, 0, 0, 0, 0, 2},
                {0, 0, 7, 2, 0, 6, 9, 0, 0},
                {0, 4, 0, 5, 0, 8, 0, 7, 0}};
        Sudoku derp = new Sudoku(board);
        print(derp.board());
        boolean[] test = derp.candidates(2, 2);
        for (boolean aTest : test) {
            System.out.println(aTest);
        }
    }

    /**
     * TESTING PURPOSES ONLY
     * <p/>
     * Prints the board
     *
     * @param board
     */
    static void print(int[][] board) {
        for (int i = 0; i < 9; ++i) {
            if (i == 0 || i == 3 || i == 6) System.out.println("+-------+-------+-------+");
            for (int j = 0; j < 9; ++j) {
                if (j % 3 == 0) System.out.print("| ");
                if (board[i][j] == 0) System.out.print(" ");
                else System.out.print(Integer.toString(board[i][j]));
                System.out.print(' ');
            }
            System.out.println("|");
        }
        System.out.println("+-------+-------+-------+");
    }

}
