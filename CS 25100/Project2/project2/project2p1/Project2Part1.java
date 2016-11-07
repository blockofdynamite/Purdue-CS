import java.util.ArrayList;
import java.util.Scanner;

public class Project2Part1 {
	public static void main(String args[]) {
		Project2 project2 = new Project2(); //Initialize assignment
		ArrayList<Token> tokens = project2.read_input(); //Where to store all our tokens
		ArrayList<Token> toEval = new ArrayList<Token>(); //the current tree tokens to evaluate
		for (Token token : tokens) {
			if (token.getToken() == 28 || token.getToken() == 20) { // At the end of an expression
				if (toEval.size() == 0) { // nothing in expression
					continue;
				}
				toEval.add(token); //adding ';'
				toEval.add(new Token(20, "?")); //adding '?'
				Parser parser = new Parser(toEval); //parsing current expression
				Tree t = parser.build_expression_tree(toEval); //Build tree
				t.print(); //Print tree
				System.out.println(); //New line to satisfy test cases
				try {
					Tree toPrint = project2.evaluate(t); //Evaluate
					toPrint.get_info().print(); //Print
					System.out.println(); //Satisfy test cases
					toEval.clear(); //Ready for next expression
				} catch (ArithmeticException e) { //Check for divide by zero
					System.out.println("Divide by zero!");
					toEval.clear(); //Clear
				}
				continue; //Begin again!
			}
			toEval.add(token); //Token for current tree
		}
	}
}