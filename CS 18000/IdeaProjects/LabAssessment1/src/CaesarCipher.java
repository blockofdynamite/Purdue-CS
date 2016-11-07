/**
 * CS180 - Lab Assessment 1
 *
 * Ceaser Cipher
 *
 * @author hughe127
 *
 * @lab 802
 *
 * @date 10/10/2014
 */
public class CaesarCipher {

    /**
     * Does the actual "encrypting"
     *
     * @param toEncode - the string to encode
     * @param key - the amount to shift the letters by
     * @return the encrypted string
     */
    public static String encrypt(String toEncode, int key) {

        toEncode = justTheLetters(toEncode);

        int stringLength = toEncode.length();

        String encrypted = "";

        for (int i = 0; i < stringLength; i++) {

            if (toEncode.charAt(i) + key > 'Z') {
                int difference = toEncode.charAt(i) + key - 'Z' - 1;
                encrypted += (char) ('A' + difference);
            }
            else {
                encrypted += (char) (toEncode.charAt(i) + key);
            }
        }
        return encrypted;
    }

    public static String decrypt(String toDecrypt, int key) {

        String decrypted = "";

        toDecrypt = justTheLetters(toDecrypt);

        int stringLength = toDecrypt.length();

        for (int i = 0; i < stringLength; i++) {
            if (toDecrypt.charAt(i) - key < 'A') {
                int difference = 'A' - (toDecrypt.charAt(i) - key + 1);
                decrypted += (char) ('Z' - difference);
            }
            else {
                decrypted += (char) (toDecrypt.charAt(i) - key);
            }
        }
        return decrypted;
    }

    /**
     *Removes all non-letters and makes it uppercase
     *
     * @param toClean - the string to be made uppercase
     * @return - the string uppercase
     */
    public static String justTheLetters(String toClean) {
        toClean = toClean.toUpperCase();

        String justTheLetters = "";
        for (int i = 0; i < toClean.length(); i++) {
            if (toClean.charAt(i) == ' ') {
                justTheLetters += "";
            }
            else if ('A' > toClean.charAt(i) || toClean.charAt(i) > 'Z') {
                justTheLetters += "";
            }
            else {
                justTheLetters += toClean.toUpperCase().charAt(i);
            }
        }
        return justTheLetters;
    }

    /**
     * Takes a string to crack and returns the key
     * This is kinda expensive to run. Need to find a better way.
     * I know of one, but it's just as expensive.
     *
     * @param toCrack - String to figure out key for
     * @return key
     */
    public static int crack(String toCrack) {

        toCrack = justTheLetters(toCrack); //Convert the string to UPPERCASE while removing all non-letters
        int testNumber; //Used for counting E's in the current test string
        int major = 0; //The number of E's in the string w/ the most E's
        String decrypted; //The descrypted string used to parse for E's
        int key = 0;
        for (int i = 0; i < 25; i++) { //Testing for all possible shifts - this is the expensive part - <3 i7's
            decrypted = decrypt(toCrack, i); //Decrypting
            testNumber = 0;
            for (int j = 0; j < decrypted.length(); j++) { //Testing for E's, and adding +1 to testNumber for each
                if (decrypted.charAt(j) == 'E') {
                    testNumber++;
                }
            }
            if (testNumber > major) { //seeing if current string has more E's than the previous "winner"
                major = testNumber;
                key = i; //assigning the key
            }
        }
        return key;
    }

}
