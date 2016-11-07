/**
 * Write a Java program to print all the numbers between 1 and 25000 (inclusive) that are both
 * perfect squares and cubes
 *
 * Hint: There are only five such numbers and one of them is 1
 *
 * @author hughe127 - Jeff Hughes
 * @date 19 February 2015
 * @class CS Bridge UTA "Test"
 */

public class PerfectSquareCube {
    public static void main(String[] args) {
        for (int i = 1; i <= 25; i++) {
            if (Math.round(Math.sqrt(i)) == Math.sqrt(i)) {
                System.out.println((int) Math.pow(i, 3));
            }
        }
    }
}