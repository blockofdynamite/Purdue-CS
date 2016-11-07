import java.util.Scanner;

/**
 * This class parses the command line arguments.
 *
 * @author hughe127
 * @version 6/10/14
 */

public class MyCLIParser {
    public static void main(String[] args) {
        // if no arguments, print help and return
        if ( args.length == 0 ) {
            help(args);
            return;
        }
        //-help
        else if (args[0].equals("-help"))  help(args);
        //-add
        else if (args[0].equals("-add"))   add(args);
        //-sub
        else if (args[0].equals("-sub"))   sub(args);
        //-mul
        else if (args[0].equals("-mul"))   mul(args);
        //-div
        else if (args[0].equals("-div"))   div(args);
        //-stats
        else if (args[0].equals("-stats")) stats(args);
        //-table
        else if (args[0].equals("-table")) table(args);
        //if nothing inputed, print help
        else help(args);
    }

    public static boolean isInteger(String s) {
        Scanner derp = new Scanner(s);
        if (derp.hasNextInt()) {
            return true;
        }

        else {
            return false;
        }
    }

    private static void help(String[] args) {
        System.out.println("-sum: takes 1 or more ints and adds them together");
        System.out.println("-sub: takes 2 ints and subtracts them, second from the first");
        System.out.println("-mul: takes 1 or more ints and multiplies them together");
        System.out.println("-div: takes 2 ints and divides them, second into the first");
        System.out.println("-stats: takes 1 or more ints and finds the total, " +
                "the lowest and highest value, and the average");
        System.out.println("-table: Prints out a table with each row adding by one more than the row above it" +
                " the first row adds zero: takes an int 0 <= x <= 9");
    }

    /**
     * Checks to make sure all numbers are ints
     * Adds all numbers together
     *
     * @param -add num1 num2 ... numN
     */

    private static void add(String[] args) {

        if (args.length < 2) {
            System.out.println("Argument count mismatch");

            return;
        }
        for (int i = 1; i < args.length; i++) {

            if (!isInteger(args[i].toString())) {
                System.out.println("Argument type mismatch");

                return;
            }
        }

        int sum = 0;

        for (int i = 1; i < args.length; i++) {
            sum += Integer.parseInt(args[i]);
        }
        System.out.println(sum);
    }

    /**
     * Checks to make sure all numbers are ints
     * Subtracts number 2 from number 1
     *
     * @param -sub num1 num2
     */

    private static void sub(String[] args) {

        if (args.length != 3) {
            System.out.println("Argument count mismatch");

            return;
        }

        for (int i = 1; i < args.length; i++) {

            if (!isInteger(args[i].toString())) {
                System.out.println("Argument type mismatch");

                return;
            }
        }

        System.out.println(Integer.parseInt(args[1]) - Integer.parseInt(args[2]));

    }

    /**
     *  Checks to make sure all numbers are ints
     *  Multiplies all numbers together
     *
     * @param -mul num1 num2 .... numN
     */

    private static void mul(String[] args) {

        if (args.length < 2) {
            System.out.println("Argument count mismatch");

            return;
        }
        for (int i = 1; i < args.length; i++) {

            if (!isInteger(args[i].toString())) {
                System.out.println("Argument type mismatch");

                return;
            }
        }

        int multiplicative = 1;

        for (int i = 1; i < args.length; i++) {
            multiplicative *= Integer.parseInt(args[i]);
        }

        System.out.println(multiplicative);
    }

    /**
     * Checks to make sure parameters are ints
     * Takes 2 numbers and divides the second into the first
     *
     * @param -div num1 num2
     */

    private static void div(String[] args) {

        if (args.length != 3) {
            System.out.println("Argument count mismatch");

            return;
        }
        for (int i = 1; i < args.length; i++) {

            if (!isInteger(args[i].toString())) {
                System.out.println("Argument type mismatch");

                return;
            }
        }

        if (args[2].equals("0")) {
            System.out.println("undefined");
            return;
        }

        System.out.printf("%.2f\n", Double.parseDouble(args[1]) / Double.parseDouble(args[2]));
    }

    /**
     * Checks to make sure parameters are ints
     * Calculates total, max, min, and average
     *
     * @param -stats num1 num2 ... numN
     */

    private static void stats(String[] args) {
        if (args.length < 2) {
            System.out.println("Argument count mismatch");

            return;
        }
        for (int i = 1; i < args.length; i++) {

            if (!isInteger(args[i].toString())) {
                System.out.println("Argument type mismatch");

                return;
            }
        }

        MyCLIParser.add(args);

        int largest = Integer.MIN_VALUE;

        int smallest = Integer.MAX_VALUE;

        for (int number = 1; number < args.length; number++) {

            if (Integer.parseInt(args[number]) > largest) {
                largest = Integer.parseInt(args[number]);
            }

            if (Integer.parseInt(args[number]) < smallest) {
                smallest = Integer.parseInt(args[number]);
            }

        }


        int sum = 0;

        for (int i = 1; i < args.length; i++) {
            sum += Integer.parseInt(args[i]);
        }

        System.out.println(largest);

        System.out.println(smallest);

        System.out.printf("%.2f\n", ((double) sum) / ((double) (args.length - 1)) );

    }

    /**
     * Makes sure all parameters are ints
     * Prints out a table with each row adding by one more than the row above it
     * the first row adds zero
     *
     * MAKE SURE PARAMETER IS LESS THAN 10 AND IS POSITIVE
     *
     * @param -table num
     */

    private static void table(String[] args) {

        if (args.length != 2) {
            System.out.println("Argument count mismatch");

            return;
        }
        for (int i = 1; i < args.length; i++) {

            if (!isInteger(args[i].toString())) {
                System.out.println("Argument type mismatch");

                return;
            }
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int toPrint = Integer.parseInt(args[1]) + j * i;
                if (j == 9 && toPrint > 9) {
                    System.out.printf("    %d\n", toPrint);
                }
                else if (j == 9 && toPrint < 10) {
                    System.out.printf("     %d\n", toPrint);
                }
                else if (j != 9 && toPrint < 10) {
                    System.out.printf("     %d", toPrint);
                }
                else {
                    System.out.printf("    %d", toPrint);
                }

            }
        }



    }
}
