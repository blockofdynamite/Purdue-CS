/**
 * Tree
 *
 * Takes in the circumference, species, and serial of a tree and spits is back out
 *
 * @author Jeffrey Hughes
 *
 * @lab 802
 *
 * @date 9/10/14
 *
 */

import java.util.Scanner;

public class Tree {
    public int serial;
    public double circumference;
    public String species;
    Tree(int serial, double circumference, String species) {
        this.circumference = circumference;
        this.serial = serial;
        this.species = species;
    }
    public String describe() {
        return String.format("Tree number %d has a circumference of %.2f and is of species %s.",
                serial, circumference, species);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        double circumference;
        int serial;
        String species;
        System.out.println("Please enter the next tree.");
        System.out.println("Species: ");
        species = in.next();
        System.out.println("Serial: ");
        serial = in.nextInt();
        System.out.println("Circumference: ");
        circumference = in.nextDouble();
        Tree newTree = new Tree(serial, circumference, species);
        String info = newTree.describe();
        System.out.println(info);
    }
}
