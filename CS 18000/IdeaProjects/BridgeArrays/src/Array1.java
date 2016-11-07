public class Array1 {
    public static void main(String[] args) {
        int[] array = new int[20];
        for (int i = 0; i < 20; i++) {
            array[i] = i + 20;
        }
        for (int c = 19; c >= 0; c--) {
            if(c % 2 == 1) {
                System.out.println(array[c]);
            }
        }
    }
}
