public class ProblemTwoTwo {
    public static void main(String[] args) {
        String row = "";
        for (int i = 0; i <= 4; i++) {
            row = "";
            for (int j = 0; j <= 4;j++) {
                if (i + j == 4) {
                    row += i+i+1;
                }
                else {
                    row += '#';
                }
            }
            System.out.println(row);
        }
    }
}