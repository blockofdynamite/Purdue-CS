import java.util.Scanner;

public class Array2 {
    boolean isDuplicate = false;
    String string1;
    public boolean duplicate(String[] value) {
        for(int i = 0; i < value.length; i++) {
            string1 = value[i];
            for (int j = i + 1; j < value.length; j++) {
                if (string1.equals(value[j])) {
                    isDuplicate = true;
                }
            }
        }
        return isDuplicate;
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter how many words you have: ");
        int sizeOfArray = in.nextInt();
        String[] array = new String[sizeOfArray];
        for (int x = 0; x < sizeOfArray; x++) {
            String y;
            System.out.println("Enter word: ");
            y = in.next();
            array[x] = y;
        }
        Array2 isADuplicate = new Array2();
        if (isADuplicate.duplicate(array) == true) {
            System.out.println("There is a duplicate!");
        }
        else {
            System.out.println("There is no duplicate!");
        }
    }
}
