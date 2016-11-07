public class Array {

    int[] array;

    Array(int[] array) {
        this.array = array;
    }

    public int sum() {

        int j = 0;

        for (int i = 0; i < this.array.length; i++) {
            j += this.array[i];
        }

        return j;
    }

    public double average() {

        int j = 0;

        for (int i = 0; i < this.array.length; i++) {
            j += this.array[i];
        }

        return j / this.array.length;
    }
}
