import java.util.Scanner;

public class Problem3 {
    public static void main(String[] args) {
        int count = 0;
        int sizeTreeInput;
        String row = "";
        System.out.println("This program only looks good up to 9. If 2 or greater digits in number, " +
                "it works, but looks weird.");
        do {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter size of matrix (Integer > 0)");
            sizeTreeInput = in.nextInt();
            if (sizeTreeInput > 0) {
                count = 1;
            }
        } while (count <= 0);

        for (int x = 0; x < sizeTreeInput; x++){
            for (int y = 0; y < x; y++) {
                System.out.print("#");
            }
            System.out.print("0");
            for (int z = x; z <= sizeTreeInput - 2; z++) {
                System.out.print(z);
            }
            System.out.println();
        }

    }
}
