import java.util.Stack;

/**
 * Created by hughe127 on 12/5/14.
 */
public class Evaluator {

    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<Integer>();
        for (int i = 0; i < args.length; i++) {
            if ((args[i]).equals("+")) {
                stack.push(stack.pop() + stack.pop());
            } else if (args[i].equals("-")) {
                stack.push(stack.pop() - stack.pop());
            } else if (args[i].equals("*")) {
                stack.push(stack.pop() * stack.pop());
            } else if (args[i].equals("/")) {
                stack.push(stack.pop() / stack.pop());
            } else {
                stack.push(Integer.parseInt(args[i]));
            }
        }
        System.out.println(Math.abs(stack.pop()));
    }
}
