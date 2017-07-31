import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class RSA {

    public static BigInteger[] encrypt(String word, BigInteger p, BigInteger q, BigInteger e) {
        BigInteger n = p.multiply(q);
        BigInteger[] toReturn = new BigInteger[word.length()];
        int length = word.length();
        for (int i = 0; i < length; i++) {
            char a = word.charAt(i);
            BigInteger charRep = BigInteger.valueOf(a);
            BigInteger toAppend = charRep.modPow(e, n);
            toReturn[i] = toAppend;
        }
        return toReturn;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("This program does *ZERO* checking to make sure your number is actually prime.");
        System.out.println("Do not use this in production!!!");

        System.out.println("Enter the word you want to encrypt: ");
        String toEncrypt = in.nextLine();

        System.out.println("Enter a large prime number: ");
        BigInteger p = null;
        try {
            p = in.nextBigInteger();
        } catch (NumberFormatException e) {
            System.out.println("Please try again with a whole number next time!");
            System.exit(1);
        }

        System.out.println("Enter another large prime number: ");
        BigInteger q = null;
        try {
            q = in.nextBigInteger();
        } catch (NumberFormatException e) {
            System.out.println("Please try again with a whole number next time!");
            System.exit(1);
        }

        System.out.println("Enter your public key: ");
        BigInteger e = null;
        try {
            e = in.nextBigInteger();
        } catch (NumberFormatException ex) {
            System.out.println("Please try again with a whole number next time!");
            System.exit(1);
        }

        BigInteger[] encrypted = RSA.encrypt(toEncrypt, p, q, e);

        System.out.printf("The encrypted output is:\n");
        for (BigInteger anEncrypted : encrypted)
            System.out.printf("%s", anEncrypted);
        System.out.println();
    }

}
