import java.util.Scanner;

public class DerpTree {
    public static void main(String[] args){
        int count = 0;
        int sizeTreeInput;
        System.out.println("This program only looks good up to 9. If 2 or greater digits in number, " +
                "it works, but looks weird.");
        do {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter size of triangle (Integer > 0)");
            sizeTreeInput = in.nextInt();
            if (sizeTreeInput > 0) {
                count = 1;
            }
        } while (count <= 0);

        for (int x = 1; x < sizeTreeInput + 1; x++) {
            for (int y = 0; y < x; y++) {
                System.out.print(y+1);
            }

            System.out.println();
        }

        for (int x = sizeTreeInput - 1; x >= 0; x--){
            for (int y = 0; y < x; y++) {
                System.out.print(y+1);
            }

            System.out.println();
        }
    }
}
