/**
 * Calculator (HW07)
 *
 * Takes a string of numbers and adds them together
 *
 * @author Jeffrey Hughes
 *
 * @lab 802
 *
 * @date 9/14/14
 *
 */

import java.util.Scanner;

public class Calculator {
    public static int sum(String s) {
        Scanner toAdd = new Scanner(s);
        int i = 0;
        while (toAdd.hasNextInt()) {
            i += toAdd.nextInt();
        }

        return i;
    }

    public static void main(String[] args) {
        String a = "3 42 75 48";
        System.out.println(sum(a));
    }
}
