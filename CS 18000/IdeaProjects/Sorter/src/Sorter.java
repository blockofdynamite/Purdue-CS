import java.util.*;

public class Sorter {
    static double[] sortArray(double[] input) {
        double[] toReturn = Arrays.copyOf(input, input.length);
        Arrays.sort(toReturn);
        return toReturn;
    }
}
