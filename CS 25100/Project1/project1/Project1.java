import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Project1 {
    private int m;
    private int n;
    private WeightedQuickUnionUF qu;
    private int[][] grid;
    private ArrayList<Point> connections;

    /**
     * initializes UnionFind structure, grid and connection list
     *
     * @param m - rows
     * @param n - columns
     */
    public Project1(int m, int n) {
        this.m = m;
        this.n = n;
        grid = new int[m][n];
        qu = new WeightedQuickUnionUF(n * m);
        connections = new ArrayList<>();
    }

    /**
     * Reads input from user (pair of connections presented as points), store the input in a list
     */
    public void read_input() {
        //System.out.print("Enter number of pairs of connections: ");
        Scanner s = new Scanner(System.in);
        int numberOfConnections = StdIn.readInt();
        for (int i = 0; i < numberOfConnections; i++) {
            Point p = new Point(StdIn.readInt(), StdIn.readInt());
            Point q = new Point(StdIn.readInt(), StdIn.readInt());
            connections.add(p);
            connections.add(q);
        }
    }

    /**
     * converts point into an integer
     *
     * @param p - point to convert
     * @return - the Point in integer form
     */
    public int map(Point p) {
        return p.x + p.y * m;
    }

    /**
     * converts integer into a point
     *
     * @param i - the int to convert to a Point
     * @return - the Point made from the int
     */
    public Point unmap(int i) {
        return new Point(i / m, i % m);
    }

    /**
     * scans connections and populates UnionFind structure
     */
    public void process_connections() {
        for (int i = 0; i < connections.size(); i++) {
            //System.out.println(connections.get(i) + " + " + connections.get(i + 1));
            qu.union(map(connections.get(i)), map(connections.get(++i)));
        }
    }

    //Used for sorting the array so that the test cases are happy!
    public class PointCompare implements Comparator<Point> {
        public int compare(final Point a, final Point b) {
            if (a.y < b.y) {
                return -1;
            } else if (a.y > b.y) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     * retrieve the connected sets from the UnionFind structure
     *
     * @return connected sets
     */
    public ArrayList<Point> retrieve_connected_sets() {
        ArrayList<Point> toReturn = new ArrayList<>();
        int[] tempSizes = qu.getSize();

        //Storing sizes for easier use later.
        ArrayList<Integer> sizes = new ArrayList<>();

        for (int i = 0; i < (n * m); i++) {
            int k = qu.find(i);
            Point toAdd = unmap(k);

            grid[i % grid.length][i / grid.length] = k;

            if (!toReturn.contains(toAdd)) {
                toReturn.add(toAdd);
                sizes.add(tempSizes[k]);
            }
        }

        toReturn.sort(new PointCompare());

        for (Point p : toReturn) {
            System.out.printf("Set (%d,%d) with size %d\n", p.y, p.x, tempSizes[map(new Point(p.y, p.x))]);
        }

        return toReturn;
    }

    /**
     * Tests whether two Cells are connected in the grid
     *
     * @param p1 - first point to compare
     * @param p2 - second point to compare
     * @return - if they're adjacent
     */
    public boolean is_adjacent(Point p1, Point p2) {
        int x = Math.abs(p1.x - p2.x);
        int y = Math.abs(p1.y - p2.y);
        return !(x > 1 || y > 1) && !(x == 1 && y == 1);
    }

    /**
     * outputs the boundaries and size of each connected set
     *
     * @param sets - the set of points to find the boundaries and size of
     */
    public void output_boundaries_size(ArrayList<Point> sets) {
        for (Point p : sets) {
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (grid[i][j] == qu.find(map(new Point(p.y, p.x)))) {
                        if (i > maxX) {
                            maxX = i;
                        }
                        if (i < minX) {
                            minX = i;
                        }
                        if (j > maxY) {
                            maxY = j;
                        }
                        if (j < minY) {
                            minY = j;
                        }
                    }
                }
            }
            System.out.printf("Boundaries for (%d,%d) are %d<=x<=%d and %d<=y<=%d\n", p.y, p.x, minY, maxY, minX, maxX);
        }
    }

    public static void main(String args[]) {
        int m, n;
        //System.out.print("Enter size of grid(m n): ");
        m = StdIn.readInt();
        n = StdIn.readInt();

        Project1 project1 = new Project1(m, n);
        project1.read_input();
        project1.process_connections();
        ArrayList<Point> sets = project1.retrieve_connected_sets();
        project1.output_boundaries_size(sets);
    }
}