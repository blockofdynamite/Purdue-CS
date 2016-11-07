/**
 * LeapYear (HW06)
 *
 * Takes a year as input and determines if it is a leap year
 *
 * @author Jeffrey Hughes
 *
 * @lab 802
 *
 * @date 9/12/14
 *
 */

import java.util.Scanner;

public class LeapYear {
    public static boolean isLeapYear(int year) {
        boolean test = false;
        if (year % 4 == 0) {
            test = true;
            if (year % 400 == 0) {
                test = true;
            }
            else if (year % 100 == 0) {
                test = false;
            }
        }
        return test;
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter a year: ");
        int year = in.nextInt();
        LeapYear isLeapYear = new LeapYear();
        boolean test = isLeapYear(year);
        if (test) {
            System.out.printf("The year %d is a leap year!\n", year);
        }
        else {
            System.out.printf("The year %d is not a leap year!\n", year);
        }

    }
}
