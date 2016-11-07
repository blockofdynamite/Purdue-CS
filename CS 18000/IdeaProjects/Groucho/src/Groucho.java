import java.util.Scanner;

public class Groucho {

    String secret;

    Groucho(String secret) {
        this.secret = secret;
    }

    public boolean saysSecret(String line) {
        if (line.indexOf(secret) >= 0) {
            System.out.println("The secret word was " + secret);
            return true;
        }

        else {
            return false;
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter the secret word");
        String secretWord = in.nextLine();
        Groucho g = new Groucho(secretWord);
        System.out.println("Please enter your line of text!");
        while (in.hasNextLine()) {
            String secretWordNow = in.nextLine();
            if (g.saysSecret(secretWordNow)) {
                System.out.println("You have won $100");
            }
            System.out.println("Please enter your line of text!");
        }
    }
}
