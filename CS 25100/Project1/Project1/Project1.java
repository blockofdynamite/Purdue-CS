import java.awt.Point;
import java.util.ArrayList;
import java.util.Scanner;

public class Project1 {
	private int m;
	private int n;
	private WeightedQuickUnionUF qu;
	private int[][] grid;
	private ArrayList<Point> connections;

	/**
	 * initializes UnionFind structure, grid and connection list
	 * @param m
	 * @param n
	 */
	public Project1(int m, int n){
		grid = new int[m][n];
        
	}

	/**
	 * Reads input from user (pair of connections presented as points), store the input in a list  
	 */
	public void read_input() {

		
	}

	/**
	 * converts point into an integer
	 * @param p
	 * @return
	 */

	public int map(Point p) {
		
	}

	/***
	 * converts integer into a point
	 * @param i
	 * @return
	 */
	public  Point unmap(int i) {
		
	}

	/***
	 * scans connections and populates UnionFind structure
	 */
	public void process_connections(){
		

	}

	/**
	 * retrieve the connected sets from the UnionFind structure
	 * @return connected sets
	 */
	public ArrayList<Point> retrieve_connected_sets() {
		
	}

	/**
	 * Tests whether two Cells are connected in the grid
	 * @param p1
	 * @param p2
	 * @return
	 */
	public boolean is_adjacent(Point p1, Point p2) {
		
	}

	/**
	 * outputs the boundaries and size of each connected set
	 * @param sets
	 */
	public void output_boundaries_size(ArrayList<Point> sets) {
		
	}

	public static void main(String args[]) {
		int m, n;
		Scanner input = new Scanner(System.in);
		System.out.print("Enter size of grid(m n): ");
		m = input.nextInt();
		n = input.nextInt();

		Project1 project1 = new Project1(m,n);
		project1.read_input();
		project1.process_connections();
		ArrayList<Point> sets = project1.retrieve_connected_sets();
		project1.output_boundaries_size(sets);
	}
}