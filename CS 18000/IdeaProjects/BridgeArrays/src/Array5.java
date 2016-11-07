import java.util.Scanner;

public class Array5 {
    public static void main(String[] args) {
        int countOverArray = 0;
        Scanner in = new Scanner(System.in);
        System.out.println("Number of players is: ");
        int numberOfPlayers = in.nextInt();
        System.out.println("Please enter every Kth player to be eliminated: ");
        int playerToBeEliminated = in.nextInt();
        System.out.println("Every " + playerToBeEliminated + " will be eliminated. ");
        int[] players = new int[numberOfPlayers];
        int numberOfTurns = -2;
        int numberOfPlayersLeft = numberOfPlayers;
        for (int i = numberOfPlayers - 1; i >= 0; i--) {
            players[i] = i + 1;
        }
        int j = 0;
        while (numberOfPlayers > numberOfTurns) {
            if (numberOfPlayers > 1) {
                numberOfPlayers--;
                players[j] = -1;
            }
            numberOfTurns++;
            j += playerToBeEliminated;
            if (j > players.length) {
                countOverArray = j - players.length;
                j = countOverArray - 1;
            }
        }
        int playerWhoWon = 0;
        for (int p = 0; p < numberOfPlayers; p++) {
            if (players[p] != -1) {
                playerWhoWon = players[p];
            }
        }
        System.out.println("The number of turns was: " + (numberOfPlayersLeft-1));
        System.out.println("The winning player is: " + playerWhoWon);
    }
}
