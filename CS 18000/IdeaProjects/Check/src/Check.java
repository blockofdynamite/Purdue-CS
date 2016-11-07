import java.util.Scanner;

public class Check {

    public static boolean isInteger(String s) {

        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
