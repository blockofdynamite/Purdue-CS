import java.util.Scanner;

public class LargerOfTwo {

    public static void main(String[] args){
        double medianValue;
        double x, y, z;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter number one: ");
        x = in.nextDouble();
        System.out.println("Enter number two: ");
        y = in.nextDouble();
        System.out.println("Enter number three: ");
        z = in.nextDouble();
        if (x <= y && y <= z || z <= y && y <= x)
            System.out.println(y);
        else if (y <= x && x <= z || z <= x && x <= y)
            System.out.println(y);
        else if (x <= z && z <= y || y <= z && z <= x)
            System.out.println(x);
        else
            System.out.println(z);

    }
}
