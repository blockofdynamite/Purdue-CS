import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Project0 {
    static ArrayList<Token> tokens = new ArrayList<Token>();
    static String input;

    /**
     * Tokenizes the string inputted.
     *
     * @param input - The String to tokenize
     * @return ArrayList of tokens
     */
    public ArrayList<Token> get_tokens(String input) {
        /*TODO: Split the input into the seperate tokens */

        ArrayList<Token> tokens = new ArrayList<Token>();

        Scanner in = new Scanner(input);
        while (in.hasNext()) {

            //If float
            if (in.hasNextFloat()) {
                float next = in.nextFloat();

                if (next == Math.round(next)) {
                    int toAdd = Math.round(next);
                    Token tkn = new Token(11, toAdd);
                    tokens.add(tkn);
                } else {
                    Token tkn = new Token(12, next);
                    tokens.add(tkn);
                }
            }

            //If Integer
            else if (in.hasNextInt()) {
                int next = in.nextInt();
                Token tkn = new Token(11, next);
                tokens.add(tkn);
            }

            //If character
            else {
                in.useDelimiter("");
                char next = in.next().charAt(0);
                if (next == ' ') {
                    continue;
                }
                Token tkn = new Token(13, Character.toString(next));
                tokens.add(tkn);
                in.reset();
            }
        }
        return tokens;
    }

    /**
     * Returns a slightly modified string from what is given in input.
     * The modifications allow for easier tokenization.
     *
     * @param in - The Scanner we're going to be reading input from
     * @return
     */
    public String read_input(Scanner in) {
        //Allow to read char by char
        in.useDelimiter("");

        //base String
        String s = "";

        //Char we are gonna work with
        char toAdd = 0;

        //Booleans to keep track of decimal points
        boolean hasSeenFirstNumber = false;
        boolean hasSeenFirstDecimal = false;
        boolean hasSeenSecondNumber = false;

        while (toAdd != '?') {

            //Testing for EOF
            try {
                toAdd = in.next().charAt(0);
            } catch (NoSuchElementException e) {
                System.exit(1);
            }

            //Ignoring new lines and carriage returns
            if (toAdd == '\n' || toAdd == '\r') {
                continue;
            }

            //Adding digits to the string
            else if (Character.isDigit(toAdd)) {
                if (hasSeenFirstNumber && hasSeenFirstDecimal) {
                    hasSeenSecondNumber = true;
                }
                hasSeenFirstNumber = true;
                s += toAdd;
            }

            //Parsing for adding periods to the String
            else if (toAdd == '.') {
                //If we need to split the decimal
                if (hasSeenSecondNumber) {
                    s += " " + toAdd + " ";
                    hasSeenFirstNumber = false;
                    hasSeenSecondNumber = false;
                    hasSeenFirstDecimal = false;
                    continue;
                }

                //For adding a new number after a decimal point
                char next = in.next().charAt(0);

                //We've seen a decimal, so let's add numbers right after, if they're a number
                hasSeenFirstDecimal = true;
                if (Character.isDigit(next)) {
                    s += toAdd;
                    s += next;
                    hasSeenSecondNumber = true;
                } else {
                    s += " " + toAdd + " ";
                    s += " " + next + " ";
                }
            }

            //Not a number or '.'
            else {
                hasSeenFirstNumber = false;
                hasSeenSecondNumber = false;
                hasSeenFirstDecimal = false;
                s += " " + toAdd + " ";
            }
        }
        //System.out.println(s);
        return s;
    }

    public void print_tokens(ArrayList<Token> tokens) {
		/*TODO: Print all the tokens before and including the '?' token
		 *      Print tokens from list in the following way, "(token,tokenValue)"
		 * */
        for (Token token : tokens) {
            if (token.getType() == 'i') {
                System.out.print("(11," + token.getValue_i() + ")");
            } else if (token.getType() == 'f') {
                System.out.print("(12," + token.getValue_f() + ")");
            } else {
                switch (token.getValue_s()) {
                    case "?":
                        System.out.print("(20," + 0 + ")");
                        break;
                    case "(":
                        System.out.print("(21," + 0 + ")");
                        break;
                    case ")":
                        System.out.print("(22," + 0 + ")");
                        break;
                    case "+":
                        System.out.print("(23," + 0 + ")");
                        break;
                    case "-":
                        System.out.print("(24," + 0 + ")");
                        break;
                    case "*":
                        System.out.print("(25," + 0 + ")");
                        break;
                    case "/":
                        System.out.print("(26," + 0 + ")");
                        break;
                    case ".":
                        System.out.print("(27," + 0 + ")");
                        break;
                    case ";":
                        System.out.print("(28," + 0 + ")");
                        break;
                    case "=":
                        System.out.print("(29," + 0 + ")");
                        break;
                }
            }
        }
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        Project0 p0 = new Project0();
        input = p0.read_input(in);
        tokens = p0.get_tokens(input);
        p0.print_tokens(tokens);
    }
}
