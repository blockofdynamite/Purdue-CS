import java.util.Scanner;

/**
 * Customer class that has a method that compares two cameras. It also allows direct input of cameras.
 *
 * @author - hughe127
 * @date - 6/3/15
 * @class - CS Bridge UTA "Test"
 */
public class Customer {

    /**
     * A method that returns the value of which camera is better. It takes 2 cameras as input
     * and returns the better one (int) if they are different, or zero if they're equal
     *
     * @param c1 - The first camera to compare
     * @param c2 - The second camera to compare
     * @return - 0 if equal, 1 if Camera 1 is better, 2 if Camera 2 is better
     */
    public static int compare(Camera c1, Camera c2) {
        int valOne = c1.computeValue();
        int valTwo = c2.computeValue();

        //if everything is the same
        if (valOne == valTwo && c1.getUserRating() == c2.getUserRating()
                && (Math.abs(c1.getPrice() - c2.getPrice()) < 0.01)) {
            return 0;
        }

        //if rating and price are equal
        else if (c1.getUserRating() == c2.getUserRating() && (Math.abs(c1.getPrice() - c2.getPrice()) < 0.01)) {
            if (valOne > valTwo) {
                return 1;
            } else {
                return 2;
            }
        }

        //if value and price are equal
        else if (valOne == valTwo && (Math.abs(c1.getPrice() - c2.getPrice()) < 0.01)) {
            if (c1.getUserRating() > c2.getUserRating()) {
                return 1;
            } else {
                return 2;
            }
        }

        //value and rating are equal
        else if (valOne == valTwo && c1.getUserRating() == c2.getUserRating()) {
            if (c1.getPrice() < c2.getPrice()) {
                return 1;
            } else {
                return 2;
            }
        }

        //if no two values are the same
        else {
            double pv;
            double ratio;

            pv = c1.getPrice() / (double) valOne;
            ratio = c2.getPrice() / (double) valTwo;
            if (pv < ratio) {
                return 1;
            } else {
                return 2;
            }
        }
    }

    /**
     * Main method to run the program, get the value of the cameras, and to compare them in the end
     *
     * @param args - Program arguments - This code takes none
     */
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.println("Enter attributes of Camera 1: ");

        //Get WiFi attribute
        System.out.println("Is WiFi enabled? (true/false)");
        boolean hasWiFi = in.nextBoolean();

        //Get water resistance
        System.out.println("Is water resistant? (true/false)");
        boolean isWaterResistant = in.nextBoolean();

        //Get GPS attribute
        System.out.println("Is GPS enabled? (true/false)");
        boolean gps = in.nextBoolean();
        //to accommodate for a known "bug" in Java as nextBoolean doesn't always take the '\n' character
        //see http://stackoverflow.com/questions/23176392/simple-output-issue for more information on this issue
        in.nextLine();

        //Get condition
        System.out.println("Condition? (New/Refurbished/Used)");
        String condition = in.nextLine();

        //Get price
        System.out.println("Enter price in $ (0.00 to 1000.00)");
        double price = in.nextDouble();

        //Get rating
        System.out.println("Enter user rating (0 to 5)");
        int rating = in.nextInt();

        Camera c1 = new Camera(hasWiFi, isWaterResistant, gps, condition, price, rating);
        System.out.println("\n============================================================\n");

        //Gonna do the same stuff as we did with camera one
        System.out.println("Enter attributes of Camera 1: ");

        System.out.println("Is WiFi enabled? (true/false)");
        hasWiFi = in.nextBoolean();

        System.out.println("Is water resistant? (true/false)");
        isWaterResistant = in.nextBoolean();

        System.out.println("Is GPS enabled? (true/false)");
        gps = in.nextBoolean();
        in.nextLine();

        System.out.println("Condition? (New/Refurbished/Used)");
        condition = in.nextLine();

        System.out.println("Enter price in $ (0.00 to 1000.00)");
        price = in.nextDouble();

        System.out.println("Enter user rating (0 to 5)");
        rating = in.nextInt();

        Camera c2 = new Camera(hasWiFi, isWaterResistant, gps, condition, price, rating);
        System.out.println("\n============================================================\n");

        System.out.println("Result of Comparison:");
        if (compare(c1, c2) == 1) {
            System.out.println("Camera 1 is better than Camera 2!");
        } else if (compare(c1, c2) == 2) System.out.println("Camera 2 is better than Camera 1!");
        else System.out.println("Camera 1 is the same as Camera 2!");
    }
}
