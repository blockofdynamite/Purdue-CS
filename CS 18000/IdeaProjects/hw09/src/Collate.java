import java.util.Scanner;

public class Collate {
    public static String collate(String a, String b) {
        int lengthOfString = a.length();
        String collatedString = "";
        for (int i = 0; i < lengthOfString; i++) {
            collatedString += a.charAt(i);
            collatedString += b.charAt(i);
        }

        return collatedString;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter string 1: ");
        String a = in.nextLine();
        System.out.println("Please ensure that string 2 is of equal length, then enter String 2: ");
        String b = in.nextLine();
        System.out.println(collate(a, b));
    }
}
