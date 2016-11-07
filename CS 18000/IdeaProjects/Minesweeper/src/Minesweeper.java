import java.security.SecureRandom;
import java.util.Scanner;

/**
 * CS180 - Lab05 - Minesweeper
 *
 * Make a minesweeper game (CLI) that uses arrays.
 *
 * All JavaDoc comments are from Wiki
 *
 * @author hughe127
 *
 * @lab 802
 *
 * @date 03/10/14
 */
public class Minesweeper {

    /**
     * the minefield, stores the locations of the mines
     */
    private final boolean[][] minefield;

    /**
     * the clue grid, keeps track of clue values
     */
    private final int[][] clueGrid;

    /**
     * the checked grid, keeps track of locations checked by user
     */
    private boolean[][] checked;

    /**
     * number of rows in the game
     */
    private int rows;

    /**
     * number of columns in the game
     */
    private int columns;

    /**
     * construct a new Minesweeper game
     * @param rows - number of rows
     * @param columns - number of columns
     * @param mines - number of mines
     */
    public Minesweeper(int rows, int columns, int mines) {

        minefield = new boolean[rows][columns];

        clueGrid = new int[rows][columns];

        checked = new boolean[rows][columns];

        this.columns = columns;

        this.rows = rows;

        generateMinefield(mines);

        generateClueGrid();

        for (int i = 0; i < checked.length; i++) {
            for (int j = 0; j < checked[i].length; j++) {
                this.checked[i][j] = false;
            }
        }
    }

    /**
     * randomly picks mines number of cells to hold mines and sets those cells to true in the minefield.
     * sets all other cells to false
     * @param number of mines to place
     */
    private void generateMinefield(int mines) {
        //set all cells to false
        for (int i = 0; i > minefield.length; i++) {
            for (int j = 0; j < minefield[i].length; j++) {
                minefield[i][j] = false;
            }
        }

        //Initialize entropy-based random number generator
        SecureRandom random = new SecureRandom();

        //place mines
        while (mines > 0) {
            int r = random.nextInt(minefield.length);
            int c = random.nextInt(minefield[r].length);

            if (!minefield[r][c]) {
                minefield[r][c] = true;
            }

            mines--;

        }

    }

    private void generateClueGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                clueGrid[i][j] = minefield[i][j] ? -1 : countMines(i,j);
            }
        }
    }



    /**
     *
     * @param row
     * @param column
     * @return the number of mines surrounding the specified cells
     */
    private int countMines(int row, int column) {
        int n = 0;
        //Run the three X three grid and test the number of mines around it
        for (int i = Math.max(row-1,0); i <= Math.min(row-1,row+1); i++) {
            for (int j = Math.max(column-1,0); j <= Math.min(column-1,column+1); j++) {
                if (!(i == row && j == column) && minefield[i][j]) {
                    n += 1;
                }
            }
        }
        return n;
    }

    /**
     * print the column numbers, row letters, and {?, ,#,*} depending on the contents and checked state of the cell.<br>
     * ? - unchecked cells<br>
     * * - checked cells with a mine
     * # - checked cells with 1-8 surrounding mines, replace # with the actual number<br>
     *   - checked cells with 0 surrounding mines or funky clue value<br>
     */
    private void printBoard() {
        System.out.print("   ");
        for (int j = 0; j < columns; j++)
            System.out.print(" " + (j+1));
        System.out.println();
        System.out.print("  +-");
        for (int j = 0; j < columns; j++)
            System.out.print("--");
        System.out.println();
        char row_letter = 'A';
        for (int i = 0; i < rows; i++) {
            System.out.print(row_letter + " |");
            for (int j = 0; j < columns; j++) {
                char cell_symbol;
                if (!checked[i][j])
                    cell_symbol = '?';
                else if (minefield[i][j])
                    cell_symbol = '*';
                else if (clueGrid[i][j] > 0)
                    cell_symbol = (char)('0' + clueGrid[i][j]);
                else
                    cell_symbol = ' ';
                System.out.print(" " + cell_symbol);
            }
            System.out.println();
            row_letter++;
        }
    }

    /**
     * start the Minesweeper game
     */

    //TODO Fix all the broken code

    public void start() {
        Scanner in = new Scanner(System.in);
        boolean win = false;
        while (!win) {
            printBoard();
            System.out.println("Check cell? ");
            String line = in.nextLine().toUpperCase();
            int row = line.charAt(0) - 'A';
            int column = line.charAt(1) - '1';
            if (minefield[row][column]){
                break;
            }
            else {
                checked[row][column] = true;
            }
            win = true;
            for (int i = 0 ; i < rows && win; i++)
                for (int j = 0; j < columns && win; j++)
                    if (!checked[i][j] && !minefield[i][j])
                        win = false;
        }
        in.close();
        for (int i = 0 ; i < rows; i++)
            for (int j = 0; j < columns; j++)
                checked[i][j] = true;
        printBoard();
        if (win) {
            System.out.println("Winner winner chicken dinner!");
        }
        else {
            System.out.println("You suck at this game");
        }
    }

    /*public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean win = false; // have not won yet
        while (!win) {
            // show board to user
            printBoard();
            // prompt user for cell to check
            System.out.print("Check cell? ");
            // get input from user
            String line = scanner.nextLine().toUpperCase();
            int row = line.charAt(0)-'A';
            int column = line.charAt(1)-'1';
            // check the cell
            if (minefield[row][column]) // game over, loss
                break;
            else // game continues
                // mark cell as checked
                checked[row][column] = true;
            // if all unchecked cells are mines, game is won
            win = true;
            for (int i = 0 ; i < rows && win; i++)
                for (int j = 0; j < columns && win; j++)
                    if (!checked[i][j] && !minefield[i][j])
                        win = false; // unchecked cell is not a mine
        }
        // game is over
        scanner.close(); // close the scanner
        // set all cells to checked
        for (int i = 0 ; i < rows; i++)
            for (int j = 0; j < columns; j++)
                checked[i][j] = true;
        // show board to user
        printBoard();
        if (win) // congratulate user on success
            System.out.println("\nYou win!");
        else // scold or console user for loss
            System.out.println("\nYou're not very good at this are you?");
    }*/

    public static void main(String[] args) {
        Minesweeper m = new Minesweeper(5,5,5);
        m.start();
    }

}
