import java.util.Scanner;

public class Adder {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter first integer: ");
        int a = in.nextInt();
        System.out.println("Please enter second integer: ");
        int b = in.nextInt();
        int c = a + b;
        System.out.printf("Your sum is: \n%d", c);
    }
}
