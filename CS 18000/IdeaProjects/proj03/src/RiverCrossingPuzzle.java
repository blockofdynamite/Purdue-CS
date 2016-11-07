import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * CS 180 - Project 03 - RiverCrossingPuzzle
 *
 * Implements and lets the user play River Crossing
 *
 * @author hughe127
 *
 * @lab 802
 *
 * @date 15/10/2014
 */

public class RiverCrossingPuzzle {

    private int cannibalsOnLeft;

    private int cannibalsOnRight;

    private int missionariesOnLeft;

    private int missionariesOnRight;

    private boolean boatOnLeft;

    private boolean boatOnRight;

    private static int numEachOriginal;

    private static int gameWon;

    private int boatSize;

    private int totalMoves;

    public static boolean isInteger(String s) {
        Scanner derp = new Scanner(s);
        return derp.hasNextInt();
    }

    /**
     *  ***DO NOT CHANGE THIS FUNCTION.***
     * @return String containing the prompt for user input
     */
    public String prompt() {
        String str = "";
        str += "Available Actions\n";
        str += availableActions();
        str += "Action: ";
        return str;
    }

    /**
     * ***DO NOT CHANGE THIS FUNCTION.***
     * @return state of left (starting) bank as a String
     */
    private String leftBank() {
        String str = "";
        for (int i = 0; i < numCannibalsOnLeftBank(); i++)
            str += "C";
        str += " ";
        for (int i = 0; i < numMissionariesOnLeftBank(); i++)
            str += "M";
        str += " ";
        if (boatOnLeftBank())
            str += "B";
        return str;
    }

    /**
     *  ***DO NOT CHANGE THIS FUNCTION.***
     * @return state of right (ending) bank as a String
     */
    private String rightBank() {
        String str = "";
        if (boatOnRightBank())
            str += "B ";
        for (int i = 0; i < numCannibalsOnRightBank(); i++)
            str += "C";
        str += " ";
        for (int i = 0; i < numMissionariesOnRightBank(); i++)
            str += "M";
        return str;
    }

    public String toString() {
        return leftBank() + " | " + rightBank();
    }

    /**
     *  ***DO NOT CHANGE THIS FUNCTION.***
     * @return String representation of current state of puzzle
     */
    private String puzzleState() {
        String lb = leftBank();
        String rb = rightBank();
        String str = "\n";
        str += "Left Bank";
        for (int i = 9; i < lb.length(); i++)
            str += " ";
        str += " | ";
        for (int i = lb.length() + 3; i < lb.length() + rb.length() + 3 - 10; i++)
            str += " ";
        str += "Right Bank";
        str += "\n";
        str += lb;
        for (int i = lb.length(); i < 9; i++)
            str += " ";
        str += " | ";
        for (int i = rb.length() - 10; i < 0; i++)
            str += " ";
        str += rb;
        str += "\n";
        str += "\n";
        str += "   Cannibals on left,right banks: ";
        str += String.format("%3d,%3d", numCannibalsOnLeftBank(), numCannibalsOnRightBank());
        str += "\n";
        str += "Missionaries on left,right banks: ";
        str += String.format("%3d,%3d", numMissionariesOnLeftBank(), numMissionariesOnRightBank());
        str += "\n";
        str += "\n";
        str += "Number of moves: " + totalMoves();
        str += "\n";
        return str;
    }

    public RiverCrossingPuzzle(int numEach, int boatSize) {

        numEachOriginal = numEach;

        cannibalsOnRight = 0;

        cannibalsOnLeft = numEach;

        missionariesOnRight = 0;

        missionariesOnLeft = numEach;

        boatOnRight = false;

        boatOnLeft = true;

        gameWon = 0;

        totalMoves = 0;

        this.boatSize = boatSize;

    }

    public int numMissionariesOnLeftBank() {

        return missionariesOnLeft;
    }

    public int numMissionariesOnRightBank() {

        return missionariesOnRight;
    }

    public boolean boatOnLeftBank() {

        return boatOnLeft;
    }

    public int numCannibalsOnRightBank() {

        return cannibalsOnRight;
    }

    public int numCannibalsOnLeftBank() {

        return cannibalsOnLeft;
    }

    public boolean boatOnRightBank() {

        return boatOnRight;
    }

    public String availableActions() {
        int k = 0;
        String left = "";
        String right = "";
        if (boatOnLeftBank()) {
            for (int j = 0; j <= cannibalsOnLeft; j++) {
                for (int i = 0; i <= missionariesOnLeft; i++) {
                    if (i + j <= boatSize && i + j != 0 && i <= missionariesOnLeft && j <= cannibalsOnLeft) {
                        k++;
                        left += String.format("(%d) Can move %d cannibals and %d missionaries\n", k, j, i);
                    }
                }
            }
            return left;
        }
        else {
            for (int j = 0; j <= cannibalsOnRight; j++) {
                for (int i = 0; i <= missionariesOnRight; i++) {
                    if (i + j <= boatSize && i + j != 0 && i <= missionariesOnRight && j <= cannibalsOnRight) {
                        k++;
                        right += String.format("(%d) Can move %d cannibals and %d missionaries\n", k, j, i);
                    }
                }
            }
            return right;
        }
    }

    public void move(int numCannibalsToMove, int numMissionariesToMove) {

        totalMoves++;

        if (boatOnRight) {
            if (numCannibalsToMove + numMissionariesToMove > boatSize) {
                System.out.println("Tried to move too many people!");
                return;
            }

            if (numCannibalsToMove + numMissionariesToMove == 0) {
                System.out.println("Tried to move nobody!");
                return;
            }

            if (numCannibalsToMove > cannibalsOnRight) {
                System.out.println("Tried to move too many cannibals!");
                return;
            }

            if (numMissionariesToMove > missionariesOnRight) {
                System.out.println("Tried to move too many missionaries!");
                return;
            }

            cannibalsOnRight -= numCannibalsToMove;

            missionariesOnRight -= numMissionariesToMove;

            cannibalsOnLeft += numCannibalsToMove;

            missionariesOnLeft += numMissionariesToMove;

            boatOnRight = false;

            boatOnLeft = true;
        }

        else if (boatOnLeft) {
            if (numCannibalsToMove + numMissionariesToMove > boatSize) {
                System.out.println("Tried to move too many people!");
                return;
            }

            if (numCannibalsToMove + numMissionariesToMove == 0) {
                System.out.println("Tried to move nobody!");
                return;
            }

            if (numCannibalsToMove > cannibalsOnLeft) {
                System.out.println("Tried to move too many cannibals!");
                return;
            }

            if (numMissionariesToMove > missionariesOnLeft) {
                System.out.println("Tried to move too many missionaries!");
                return;
            }

            cannibalsOnLeft -= numCannibalsToMove;

            missionariesOnLeft -= numMissionariesToMove;

            cannibalsOnRight += numCannibalsToMove;

            missionariesOnRight += numMissionariesToMove;

            boatOnLeft = false;

            boatOnRight = true;
        }

    }

    public int totalMoves() {

        return totalMoves;
    }

    public void reset() {

        cannibalsOnRight = 0;

        cannibalsOnLeft = numEachOriginal;

        missionariesOnRight = 0;

        missionariesOnLeft = numEachOriginal;

        boatOnRight = false;

        boatOnLeft = true;

        gameWon = 0;

        totalMoves = 0;

    }

    public int status() {

        return gameWon;
    }

    public void play() {
        Scanner in = new Scanner(System.in);
        while (gameWon == 0) {

            System.out.println(puzzleState());
            System.out.println(prompt());
            String doWhat = in.nextLine();
            Scanner characterScanner = new Scanner(doWhat);
            int cans = characterScanner.nextInt();
            int miss = characterScanner.nextInt();
            move(cans, miss);

            if (cannibalsOnLeft > missionariesOnLeft && !(missionariesOnLeft == 0)) {
                gameWon = -1;
            }

            if (cannibalsOnRight > missionariesOnRight && !(missionariesOnRight == 0)) {
                gameWon = -1;
            }

            if ((cannibalsOnRight + missionariesOnRight) == (numEachOriginal * 2)) {
                gameWon = 1;
            }
        }
    }

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        int numEach = 3;

        int boatSize = 2;

        if (args.length < 2 || args.length == 3 || args.length > 4) {
            System.out.println("Argument count mismatch");

            return;
        }

        if (args.length == 4) {

            if (!isInteger(args[1])) {
                System.out.println("Argument type mismatch");

                return;
            }

            if (!isInteger(args[3])) {
                System.out.println("Argument type mismatch");

                return;
            }

            if (args[0].equals("-n")) {
                numEach = Integer.parseInt(args[1]);
            }

            if (args[2].equals("-n")) {
                numEach = Integer.parseInt(args[3]);
            }

            if (args[0].equals("-b")) {
                boatSize = Integer.parseInt(args[1]);
            }

            if (args[2].equals("-b")) {
                boatSize = Integer.parseInt(args[3]);
            }
        }

        if (args.length == 2) {

            if (!isInteger(args[1])) {
                System.out.println("Argument type mismatch");

                return;
            }

            if (args[0].equals("-n")) {
                numEach = Integer.parseInt(args[1]);
            }

            if (args[0].equals("-b")) {
                boatSize = Integer.parseInt(args[1]);
            }
        }

        boolean wantToKeepPlaying = true;

        RiverCrossingPuzzle a = new RiverCrossingPuzzle(numEach, boatSize);

        try {
            while (wantToKeepPlaying) {

                a.play();

                if (a.status() == 1) {
                    System.out.println("You won! Continue playing? (y or n)");
                    String yesOrNo = in.nextLine();

                    if (yesOrNo.toLowerCase().matches("y")) {
                        wantToKeepPlaying = true;
                        gameWon = 0;
                        a.reset();
                    }

                    if (yesOrNo.toLowerCase().matches("n")) {
                        wantToKeepPlaying = false;
                        gameWon = 0;
                    }
                } else if (a.status() == -1) {
                    System.out.println("You lost! :( Continue playing? (y or n)");
                    String yesOrNo = in.nextLine();

                    if (yesOrNo.toLowerCase().matches("y")) {
                        wantToKeepPlaying = true;
                        gameWon = 0;
                        a.reset();
                    }

                    if (yesOrNo.toLowerCase().matches("n")) {
                        wantToKeepPlaying = false;
                        gameWon = 0;
                    }
                }
            }
        } catch (NoSuchElementException test) {
            wantToKeepPlaying = true;
        }
    }
}
