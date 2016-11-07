/**
 * CS 180 - Lab 04 - Repetition
 * 
 * This class is class that helps with our knowledge of loops
 * 
 * @author hughe127
 * 
 * @lab 802
 *
 * @date 26/09/14
 */

import java.util.Scanner;

public class Repetition {
	
	/**
	* Generate and print out all even numbers from 0 to 100 (inclusively) on the same line.
	* 
	* Output: 0 2 4 6 8 10 12 14 16 18 20 22 24 26 28  ... 98 100
	*/
	public static void even() {
		
		for (int i = 0; i < 101; i +=2) {
			System.out.printf("%d ", i);
		}
	}   
	
	/**
	  *  1. Declare an integer named exp (short for exponent). Initialize it with value 0.
	  *  2. Iterate over increased values of exp for every 2^(exp) less than 1000 
	  *     (note that this is the loop exit condition).
	  *  3. To get the value of 2^(exp), use the expression Math.pow(2, exp).
	  * 
	  * Output: 1.0 2.0 4.0 8.0 16.0 32.0 64.0 128.0 256.0 512.0
	  */
	public static void powers() {
		
		double power = 1;
	    
	    while (power < 1000) {
	    	System.out.printf("%.1f ", power);
	    	power *= 2;
	    }
	}
	
	/**
	  * Print out the alphabet using Characters and a 'for loop'.
	  * 
	  * Make use of the following fact about type char to be able to iterate,
	  * printing each letter of the alphabet in small case.
	  * 
	  * Type char (like byte and short) is an integral type, which means:
	  * - it is internally represented as a 16-bit integer,
	  *   and hence can accept integer arithmetic and logical operations.
	  * - A variable of type char initialized to value 'a' has the integer value 97 
	  *   (you can verify that by printing a variable of type char as an integer). 
	  * - Adding 1 to the char 'a' will yield the char 'b' that has integer value 98.
	  * - The expression ('a' < 'z') is a valid boolean expression that evaluates to true. 
	  * 
	  * Output: abcdefghijklmnopqrstuvwxyz
	  */
	public static void alphabet() {
		
	    for (char a = 'a'; a <= 'z'; a++) {
	    	
	    	System.out.print(a);
	    }
	}
	
	/**
	  * Print out every letter of the string s, each on its own line.
	  * Sample Output for argument "Hello":
	  * ===================================
	  * H
	  * e
	  * l
	  * l
	  * o
	  * 
	  *  Hints
	  *  =====
	  *  - To access each character of a string use the charAt method.
	  *  - To test this method, write the line vertical(“Hello”) in your main method 
	  *    (“Hello” can be replaced by any string)
	  * 
	  * @param s : input string.
	  * @see http://docs.oracle.com/javase/8/docs/api/java/lang/String.html#charAt-int-
	  */
	public static void vertical ( String s) {
		
	    for (int i = 0; i < s.length(); i++) {
	    	
	    	System.out.println(s.charAt(i));
	    }
	}
	
	/**
	  * Print test result summary.
	  * Requirements
	  * 
	  *  - Print out “Enter scores now:”
	  *  - Accept scores (integers) from the user until they enter a non-integer
	  *  - Print out the lowest score, the highest score, and the average 
	  *    (rounded to the floor, or greatest integer not exceeding the average).
	  * 
	  * Hints:
	  *  - You can calculate the average by maintaining the sum of all the grades
	  *    and the count of all the grades and only calculating the average when
	  *    the user is done supplying the grades.
	  *  - Maintain one variable for each statistic (count, sum, lowest, highest), 
	  *    updating each of them for each grade entered.
	  *  - When printing the output; note that the output statistics are right justified.
	  */
	public static void testResults() {
		
	    Scanner in = new Scanner(System.in);
	    
	    int sum = 0;
	    
	    int count = 0;
	    
	    int lowest = Integer.MAX_VALUE;
	    
	    int highest = Integer.MIN_VALUE;
	    
	    System.out.println("Enter scores now: ");
	    
	    while(in.hasNextInt()) {
		    int score = in.nextInt();
		    sum += score;
		    count++;
		    if (score < lowest) {
		    	lowest = score;
		    }
		    
		    if (score > highest) {
		    	highest = score;
		    }
	    } 
	    
	    System.out.println("=====-----=====-----=====-----=====");
	    System.out.println("=            Test Results         =");
	    System.out.printf("= Average:                     %d =\n", sum / count);
	    System.out.printf("= Lowest:                      %d =\n", lowest);
	    System.out.printf("= Highest:                     %d =\n", highest);
	    System.out.println("=====-----=====-----=====-----=====");
	    
	    in.close();
	}
	
	public static void main(String[] args) {
		
        Repetition.even();
        
        System.out.println();
        
        Repetition.powers();
        
        System.out.println();
        
        Repetition.alphabet();
        
        System.out.println();   
        
        Repetition.vertical("hello");
        
        System.out.println();
        
        Repetition.testResults();
        
        System.out.println();
        
	}
}
