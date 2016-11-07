/**
 * CS18000 - HW04
 * This program takes in the circumference of a tree and prints out the diameter
 * @author:
 */
import java.util.Scanner;

public class NFS {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter circumference of the tree: ");
        double circumferenceOfTree = in.nextDouble();
        double diameter = circumferenceOfTree / Math.PI;
        System.out.printf("The diameter is: \n%.2f", diameter);
    }
}
