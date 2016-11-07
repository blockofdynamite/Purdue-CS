public class Matrix {
    int[][] matrix ;

    public Matrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int sum() {
        int sum = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                sum += matrix[i][j];
            }
        }
        return sum;
    }
}
