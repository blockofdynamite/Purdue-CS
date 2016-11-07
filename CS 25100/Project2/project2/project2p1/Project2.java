import java.lang.String;
import java.util.ArrayList;
import java.util.Scanner;

public class Project2 {

    /**
     * add class members as needed TODO: PART1 PART2
     */

    //List of defined token constants
    final int Tk_ADDITION = 23;
    final int Tk_SUBTRACTION = 24;
    final int Tk_MULTIPLY = 25;
    final int Tk_DIVIDE = 26;
    final int Tk_INT = 11;
    final int Tk_FLOAT = 12;

    public Project2() {
        /**
         * TODO: PART1 PART2
         */
    }

    public ArrayList<Token> get_tokens(String input) {
        ArrayList<Token> tokens = new ArrayList<Token>();
        Token token;
        int start = 0;
        while (start < input.length()) {
            token = make_token(next_token(input, start));
            start += next_token(input, start).length();
            tokens.add(token);
        }
        return tokens;
    }

    public String next_token(String input, int start) {
        int i = start;
        char c = input.charAt(i);

        if (Character.isDigit(input.charAt(i))) {
            while (i < input.length() && (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.')) {
                if (input.charAt(i) == '.' && !Character.isDigit(input.charAt(i + 1))) {
                    break;
                } else if (input.charAt(i) == '.' && Character.isDigit(input.charAt(i + 1))) {
                    i++;
                    while (Character.isDigit(input.charAt(i))) {
                        i++;
                    }
                    break;
                }
                i++;
            }
            return input.substring(start, i);
        }
        if (c == '?' || c == '(' || c == ')' || c == '+' || c == '-' || c == '*' || c == '/' || c == '.' || c == ';' || c == '=') {
            return String.valueOf(c);
        } else {
            while (Character.isAlphabetic(input.charAt(i))) {
                i++;
            }
            return input.substring(start, i);
        }
    }

    public Token make_token(String token) {
        if (Character.isDigit(token.charAt(0))) {
            if (token.contains(".")) {
                return new Token(12, Float.valueOf(token));
            } else {
                return new Token(11, Integer.valueOf(token));
            }
        } else if (token.equals("?")) {
            return new Token(20, 0);
        } else if (token.equals("(")) {
            return new Token(21, 0);
        } else if (token.equals(")")) {
            return new Token(22, 0);
        } else if (token.equals("+")) {
            return new Token(23, 0);
        } else if (token.equals("-")) {
            return new Token(24, 0);
        } else if (token.equals("*")) {
            return new Token(25, 0);
        } else if (token.equals("/")) {
            return new Token(26, 0);
        } else if (token.equals(".")) {
            return new Token(27, 0);
        } else if (token.equals(";")) {
            return new Token(28, 0);
        } else if (token.equals("=")) {
            return new Token(29, 0);
        } else {
            return new Token(13, token);
        }
    }

    public String clean_text(String input) {
        String clean_string = "";
        clean_string = input.replaceAll("\t", "");
        clean_string = clean_string.replaceAll("\n", "");
        clean_string = clean_string.replaceAll(" ", "");
        return clean_string;
    }

    /**
     * Simple scanner function that reads input and appends it to the local String variable up until, and including, a '?'.
     *
     * @return
     */
    public ArrayList<Token> read_input() {
        Scanner input = new Scanner(System.in);
        ArrayList<Token> tokens = new ArrayList<Token>();
        String temp = "";
        while (true) {
            temp = input.nextLine();
            tokens.addAll(get_tokens(clean_text(temp)));
            for (int i = 0; i < tokens.size(); i++) {
                if (tokens.get(i).getToken() == 20) {
                    input.close();
                    return tokens;
                }
            }
        }
    }

    /**
     * Evaluation function: The evaluation function first checks both the left and right subtrees and evaluates them before the root of the expression tree.
     *
     * @param tree
     * @return
     */
    public Tree evaluate(Tree tree) {
        return new Tree(recursiveEvaluation(tree));
    }

    /**
     * Recursively goes through the tree and solves.
     * Switch statement has a case for each of the four main operations
     * Has four cases inside each:
     * a is an int and b is an int
     * a is a float and b is an int
     * a is an int and b is a float
     * a and b are both floats
     *
     * @param tree - the tree to evaluate
     * @return - a token that has the final value
     */
    public Token recursiveEvaluation(Tree tree) {
        Token a, b;
        switch (tree.get_info().getToken()) {
            case Tk_ADDITION:
                a = recursiveEvaluation(tree.get_left());
                b = recursiveEvaluation(tree.get_right());
                if (a.getToken() == Tk_INT && b.getToken() == Tk_INT) {
                    return new Token(Tk_INT, a.getValue_i() + b.getValue_i());
                } else if (a.getToken() == Tk_FLOAT && b.getToken() == Tk_INT) {
                    return new Token(Tk_FLOAT, a.getValue_f() + b.getValue_i());
                } else if (a.getToken() == Tk_INT && b.getToken() == Tk_FLOAT) {
                    return new Token(Tk_FLOAT, a.getValue_i() + b.getValue_f());
                } else {
                    return new Token(Tk_FLOAT, a.getValue_f() + b.getValue_f());
                }
            case Tk_SUBTRACTION:
                a = recursiveEvaluation(tree.get_left());
                b = recursiveEvaluation(tree.get_right());
                if (a.getToken() == Tk_INT && b.getToken() == Tk_INT) {
                    return new Token(Tk_INT, a.getValue_i() - b.getValue_i());
                } else if (a.getToken() == Tk_FLOAT && b.getToken() == Tk_INT) {
                    return new Token(Tk_FLOAT, a.getValue_f() - b.getValue_i());
                } else if (a.getToken() == Tk_INT && b.getToken() == Tk_FLOAT) {
                    return new Token(Tk_FLOAT, a.getValue_i() - b.getValue_f());
                } else {
                    return new Token(Tk_FLOAT, a.getValue_f() - b.getValue_f());
                }
            case Tk_MULTIPLY:
                a = recursiveEvaluation(tree.get_left());
                b = recursiveEvaluation(tree.get_right());
                if (a.getToken() == Tk_INT && b.getToken() == Tk_INT) {
                    return new Token(Tk_INT, a.getValue_i() * b.getValue_i());
                } else if (a.getToken() == Tk_FLOAT && b.getToken() == Tk_INT) {
                    return new Token(Tk_FLOAT, a.getValue_f() * b.getValue_i());
                } else if (a.getToken() == Tk_INT && b.getToken() == Tk_FLOAT) {
                    return new Token(Tk_FLOAT, a.getValue_i() * b.getValue_f());
                } else {
                    return new Token(Tk_FLOAT, a.getValue_f() * b.getValue_f());
                }
            case Tk_DIVIDE:
                a = recursiveEvaluation(tree.get_left());
                b = recursiveEvaluation(tree.get_right());
                if (a.getToken() == Tk_INT && b.getToken() == Tk_INT) {
                    return new Token(Tk_FLOAT, a.getValue_i() / b.getValue_i());
                } else if (a.getToken() == Tk_FLOAT && b.getToken() == Tk_INT) {
                    return new Token(Tk_FLOAT, a.getValue_f() / b.getValue_i());
                } else if (a.getToken() == Tk_INT && b.getToken() == Tk_FLOAT) {
                    return new Token(Tk_FLOAT, a.getValue_i() / b.getValue_f());
                } else {
                    return new Token(Tk_FLOAT, a.getValue_f() / b.getValue_f());
                }
            default:
                return tree.get_info();
        }
    }

    /**
     * This function processes expression with equal assignment e.g., A = B + 1 it  and
     * substitutes the value of the right hand side variable.
     *
     * @param list
     * @return list that contains the simplified expression
     */
    public ArrayList<Token> simplify_update_expression(ArrayList<Token> list) {
        ArrayList<Token> new_list = new ArrayList<Token>();
        /**
         * TODO: PART2
         */

        return new_list;
    }

    /**
     * creates an expression tree on the fly then inserts/ updates the value of the left hand side variable into symbol table
     *
     * @param list
     */
    public void update_symbol_trees(ArrayList<Token> list) {
        /**
         * TODO: PART2
         */
    }


}