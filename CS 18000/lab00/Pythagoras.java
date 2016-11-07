/**
 * CS180 - Lab 00
 * Explain briefly the functionality of the program.
 * @author Jeffrey Hughes, hughe127@purdue.edu
 */

import java.util.Scanner;

public class Pythagoras {
	public double getHypotenuse(int a, int b) {
		double result;
		result = Math.sqrt(a * a + b * b);
		return result;
	}
	
	public static void main(String[] args) {
		Pythagoras p = new Pythagoras(); //create a new Pythag. object
		Scanner in = new Scanner(System.in); 
		System.out.println("Please enter side 'a': "); //prompt user for a side
		int a = in.nextInt();
		System.out.println("Please enter side 'b': "); //prompt user for b side
		int b = in.nextInt();
		double h = p.getHypotenuse(a, b); //calculate hypotenuse
		System.out.println("Hypotenuse: " + h);
	}
}
