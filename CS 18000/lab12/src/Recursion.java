import java.io.File;

/**
 * Created by hughe127 on 12/12/14.
 */
public class Recursion {

    /**
     *
     * @param a - integer to apply a power to
     * @param b - power to apply
     * @return
     */
    public static int power(int a, int b) {
        if (b == 0) {
            return 1;
        } else if (b == 1) {
            return a;
        } else {
          return a * power(a, b - 1);
        }
    }

    public static int power2(int a, int b) {
        int toReturn = a;
        if (b == 1) {
            return a;
        }
        for (int i = 0; i < b; i++) {
            toReturn *= a;
        }
        return toReturn;
    }

    public static int fileCount(String file) {
        int count = 0;
        File f = new File(file);
        if (f.isFile()) {
            return ++count;
        }
        File[] children = f.listFiles();
        for (File child : children) {
            count += fileCount(child.toString());
        }
        return count;
    }

    public static void hanoi() {

    }
}
