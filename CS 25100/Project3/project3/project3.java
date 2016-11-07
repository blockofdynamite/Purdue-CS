import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;



public class project3 {
	private int graphSize;
	private int[] color;
	
	public Graph createGraph(){
		/*TODO: Task 1.1 */
		
		return new Graph(graphSize);
		
	}
	
	
	public boolean validateGraph(Graph g) {
		/*TODO: Task 1.2 */
		return true;
	}

		
	
	public boolean checkBipartiteBfs(Graph g) {
		/*TODO: Task 2 */			
		return true; 
	}
	
	public FlowNetwork createFlowNetwork(Graph g){
		/* TODO: Task 3*/
		FlowNetwork flowNet ;
		
		
		return flowNet;
	}		

	
	public static void main(String args[])  {
		/* TODO: Task 4 and 5: You may want to create helper 
		 * methods to reduce the size of main method*/
	}

	
}