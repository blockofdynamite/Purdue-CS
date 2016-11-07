import java.util.Scanner;

/**
 * Created by grayson on 2/10/15.
 */
public class Customer {
    public static int compare(Camera c1, Camera c2) {
        int c1v = 0;
        int c2v = 0;
        if (c1.computeValue() > c2.computeValue()) c1v++;
        if (c1.computeValue() < c2.computeValue()) c2v++;
        if (c1.getPrice() < c2.getPrice()) c1v++;
        if (c1.getPrice() > c2.getPrice()) c2v++;
        if (c1.getUserRating() > c2.getUserRating()) c1v++;
        if (c1.getUserRating() < c2.getUserRating()) c2v++;
        double c1pv = c1.getPrice() / c1.computeValue();
        double c2pv = c2.getPrice() / c1.computeValue();
        if ((c1v > c2v) || (c1pv < c2pv)) return 1;
        if (c2v > c1v || (c1pv > c2pv)) return 2;
        return 0;
    }

    public static void main(String[] args) {
        Scanner scanley = new Scanner(System.in);
        System.out.println("Enter attributes of Camera 1:\nIs WiFi enabled? (true/false)");
        boolean wifi = scanley.nextBoolean();
        System.out.println("Is water resistant? (true/false)");
        boolean water = scanley.nextBoolean();
        System.out.println("Is GPS enabled? (true/false)");
        boolean gps = scanley.nextBoolean();
        System.out.println("Condition? (New/Refurbished/Used)");
        String condition = scanley.nextLine();
        System.out.println("Enter price in $ (0.00 to 1000.00)");
        double price = scanley.nextDouble();
        System.out.println("Enter user rating (0 to 5)");
        int rating = scanley.nextInt();

        Camera c1 = new Camera(wifi, water, gps, condition, price, rating);
        System.out.println("\n============================================================\n");

        System.out.println("Enter attributes of Camera 2:\nIs WiFi enabled? (true/false)");
        wifi = scanley.nextBoolean();
        System.out.println("Is water resistant? (true/false)");
        water = scanley.nextBoolean();
        System.out.println("Is GPS enabled? (true/false)");
        gps = scanley.nextBoolean();
        System.out.println("Condition? (New/Refurbished/Used)");
        condition = scanley.nextLine();
        System.out.println("Enter price in $ (0.00 to 1000.00)");
        price = scanley.nextDouble();
        System.out.println("Enter user rating (0 to 5)");
        rating = scanley.nextInt();

        Camera c2 = new Camera(wifi, water, gps, condition, price, rating);
        System.out.println("\n============================================================\n");

        System.out.println("Result of Comparison:");
        if (compare(c1, c2) == 1) System.out.println("Camera 1 is better than Camera 2!");
        else if (compare(c1, c2) == 2) System.out.println("Camera 2 is better than Camera 1!");
        else System.out.println("Camera 1 is the same as Camera 2!");
    }
}
