import java.util.Scanner;

public class ModeOfArrays {
    public static void main(String[] args) {
        int count2 = 0;
        int count1 = 0;
        int popular1 =0;
        int popular2 =0;
        Scanner in = new Scanner(System.in);
        System.out.println("How many numbers do you have?");
        int numbers = in.nextInt();
        int[] arrayOfInts = new int[numbers];

        for (int x = 0; x < numbers; x++) {
            int y = 0;
            System.out.println("Enter integer");
            y = in.nextInt();
            arrayOfInts[x] = y;
        }



        for (int i = 0; i < arrayOfInts.length; i++) {
            popular1 = arrayOfInts[i];
            count1 = 1;

            for (int j = i + 1; j < arrayOfInts.length; j++) {
                if (popular1 == arrayOfInts[j]) {
                    count1++;
                }
            }

            if (count1 > count2) {
                popular2 = popular1;
                count2 = count1;
            }

            else if(count1 == count2) {
                popular2 = Math.max(popular2, popular1);
            }
        }
        System.out.println("The mode is: " + popular2);
    }
}
