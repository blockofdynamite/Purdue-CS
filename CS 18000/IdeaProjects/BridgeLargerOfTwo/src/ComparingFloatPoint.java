public class ComparingFloatPoint {
    public static void main(String[] args){
        double root = Math.sqrt(3.5278);
        if (root * root == 3.578) {
            System.out.println("Math.sqrt(3.5278) squared is 3.5278");
        }
        else {
            System.out.printf("Math.sqrt(3.5278) squared is not 3.5278. It is: %.4f", root * root);
        }
    }
}