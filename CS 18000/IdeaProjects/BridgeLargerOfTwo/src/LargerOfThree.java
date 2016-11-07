import java.util.Scanner;

public class LargerOfThree {

    public double median(double x, double y, double z) {
        if (x <= y && y <= z || z <= y && y <= x)
            return y;
        else if (y <= x && x <= z || z <= x && x <= y)
            return x;
        else if (x <= z && z <= y || y <= z && z <= x)
            return z;
        else
            return 0;
    }

    public static void main(String[] args){
        double medianValue;
        double one, two, three;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter number one: ");
        one = in.nextDouble();
        System.out.println("Enter number two: ");
        two = in.nextDouble();
        System.out.println("Enter number three: ");
        three = in.nextDouble();
        LargerOfThree lot = new LargerOfThree();
        medianValue = lot.median(one, two, three);

        System.out.println(medianValue);
    }
}
