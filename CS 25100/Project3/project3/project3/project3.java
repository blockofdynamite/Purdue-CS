import java.awt.*;
import java.util.*;

public class project3 {

    private int graphSize;

    //We use this in our BFS. We use a separate array for our DFS
    private int[] color;

    /**
     * This method initializes the graph, while checking for:</p>
     * Empty graph</p>
     * Illegal adjacency</p>
     * Range</p>
     * Backwards Adjacency</p>
     *
     * @return - The Graph that is inputted
     */
    public Graph createGraph() {
        Scanner in = new Scanner(System.in);

        //Get the graph size from the user
        System.out.print("Input the size of the graph: ");
        graphSize = Integer.parseInt(in.nextLine()); //Get size of graph

        Graph g;
        try { //Check if the size entered is negative
            g = new Graph(graphSize);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }

        if (graphSize == 0) { //check if graph size is 0
            System.out.println("Warning: empty graph.");
            return null;
        }

        System.out.println("Input lines of adjacencies: (V0: V1, V2, V3...)");
        for (int i = 1; i <= graphSize; i++) {
            Scanner line = new Scanner(in.nextLine()); //We process line by line as the user enters it
            line.useDelimiter(":|,| "); //Go number by number separated by ',' ':' or ' '
            int v1 = line.nextInt(); //Get out initial vertex
            v1--; //Make it compatible w/ the graph
            ArrayList<Integer> nodesAdded = new ArrayList<>();
            while (line.hasNext()) { //Get all nodes and add them to the adjacency
                int v2 = line.nextInt();
                v2--;
                if (!nodesAdded.contains(v2) && v2 != v1) {
                    nodesAdded.add(v2);
                } else { //Check to make sure the user didn't put it in twice
                    System.out.println("Error: illegal adjacency.");
                    return null;
                }
                try {
                    g.addEdge(v1, v2); //Add the inputted adjacency
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Error: Vertex number out of range!");
                    return null;
                }
            }
        }
        return g;
    }


    //Variables we use in our DFS for verifying that the graph is connected
    private boolean marked[];
    private int count;

    /**
     * This method takes care of printing and running DFS to verify that the graph is connected.
     *
     * @param g - The Graph to validate
     * @return - whether it passes or not
     */
    public boolean validateGraph(Graph g) {
        boolean over10 = false;

        //Run our DFS to verify it's connected
        marked = new boolean[g.V()];
        dfs(g, 1);
        if (count != g.V()) {
            System.out.println("Error: Graph not connected.");
            return false;
        }

        System.out.println("Number of vertices: " + g.V());
        for (int i = 0; i < g.V(); i++) {
            if (g.V() >= 10) {
                over10 = true;
            }

            //Adding only unique adjacencies for our calculations
            ArrayList<Integer> adjacency = new ArrayList<>();
            for (int j : g.adj(i)) {
                if (!adjacency.contains(j)) {
                    adjacency.add(j);
                }
            }

            //This turns the ArrayList into an int[] and then sorts it
            int[] adjArray = adjacency.stream().mapToInt(Integer::intValue).toArray();
            Arrays.sort(adjArray);

            //if number of vertices is less than 10, print it.
            boolean first = true;
            int k = i + 1;
            if (!over10) {
                System.out.print(k + ":");
                for (int j : adjArray) {
                    if (!first) {
                        System.out.print(",");
                    }
                    System.out.print(++j);
                    first = false;
                }
                System.out.println();
            }
        }

        //If the graph is over 10 vertices
        if (over10) {
            System.out.println("Graph passes.");
        }

        return true;
    }

    /**
     * Runs a DFS on the graph to check for connectivity
     *
     * @param g - The Graph to run a DFS on
     * @param v - The vertex to start at
     */
    public void dfs(Graph g, int v) {
        count++;
        marked[v] = true;
        for (int w : g.adj(v)) {
            if (!marked[w]) {
                dfs(g, w);
            }
        }
    }

    /**
     * Runs a BFS on a graph starting at the given vertex and verifies that it is bipartite
     *
     * @param g   - The graph to run a BFS on
     * @param src - The vertex to start at
     * @return - whether it's bipartite or not
     */
    public boolean isBipartite(Graph g, int src) {
        //Initialize color array
        color = new int[g.V()];
        for (int i = 0; i < color.length; i++) {
            color[i] = -1;
        }
        color[src] = 1;

        //We'll use this as our queue
        LinkedList<Integer> q = new LinkedList<>();
        q.add(src);

        //While it's not empty
        while (q.size() != 0) {
            int u = q.poll();

            //Getting unique adjacency
            ArrayList<Integer> adjacency = new ArrayList<>();
            for (int j : g.adj(u)) {
                if (!adjacency.contains(j)) {
                    adjacency.add(j);
                }
            }

            //Run the actual BFS, iteratively
            for (int v = 0; v < g.V(); ++v) {
                if (adjacency.contains(v) && color[v] == -1) {
                    color[v] = 1 - color[u];
                    q.add(v);
                } else if (adjacency.contains(v) && color[v] == color[u]) {
                    return false;
                }
            }
        }

        //And we print out the partitions assuming it is bipartite
        if (g.V() < 10) {
            ArrayList<Integer> x = new ArrayList<>();
            ArrayList<Integer> y = new ArrayList<>();

            for (int i = 0; i < color.length; i++) {
                if (color[i] == 1) {
                    x.add(i + 1);
                } else if (color[i] == 0) {
                    y.add(i + 1);
                }
            }

            System.out.print("Partition X:");
            for (int i = 0; i < x.size(); i++) {
                if (i == x.size() - 1) {
                    System.out.println(x.get(i));
                } else {
                    System.out.print(x.get(i) + ", ");
                }
            }
            System.out.print("Partition Y:");
            for (int i = 0; i < y.size(); i++) {
                if (i == y.size() - 1) {
                    System.out.println(y.get(i));
                } else {
                    System.out.print(y.get(i) + ", ");
                }
            }
        } else { //If it's over 10 vertices
            System.out.println("Graph is bipartite.");
        }


        return true;
    }

    /**
     * Runs a BFS on the graph to verify that it is bipartite
     *
     * @param g - The graph to check for bipartite
     * @return - The result of isBipartite(), which starts with vertex 0.
     */
    public boolean checkBipartiteBfs(Graph g) {
        return isBipartite(g, 0);
    }

    /**
     * @param g - The Graph to create a FlowNetwork from
     * @return - The FlowNetwork
     */
    public FlowNetwork createFlowNetwork(Graph g) {
        //Initialize FlowNetwork with a tap and sink
        FlowNetwork flowNet = new FlowNetwork(g.V() + 2);

        //Two partitions
        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Integer> y = new ArrayList<>();

        //Get the partitions
        for (int i = 0; i < color.length; i++) {
            if (color[i] == 1) {
                x.add(i + 1);
            } else if (color[i] == 0) {
                y.add(i + 1);
            }
        }

        //Initialize the blue partition
        for (Integer aX : x) {
            ArrayList<Integer> added = new ArrayList<>();
            flowNet.addEdge(new FlowEdge(0, aX, 1));
            for (int j : g.adj(aX - 1)) {
                if (!added.contains(j)) {
                    added.add(j);
                    flowNet.addEdge(new FlowEdge(Integer.parseInt(aX.toString()), j + 1, 1));
                }
            }
        }

        //Initialize the red partition
        for (Integer aY : y) {
            flowNet.addEdge(new FlowEdge(aY, g.V() + 1, 1));
        }

        //Print
        if (g.V() < 20) {
            System.out.println(flowNet.toString());
        } else {
            System.out.println("Flow network built.");
        }

        return flowNet;
    }

    /**
     * This method is based off of answer @607 on Piazza.
     * It verifies that an output is a correct output.
     *
     * @param connections
     * @return
     */
    public boolean verifyOutput(ArrayList<Point> connections) {
        //Two partitions
        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Integer> y = new ArrayList<>();

        //Get the partitions
        for (int i = 0; i < color.length; i++) {
            if (color[i] == 1) {
                x.add(i + 1);
            } else if (color[i] == 0) {
                y.add(i + 1);
            }
        }

        ArrayList<Integer> verifyX = new ArrayList<>();
        ArrayList<Integer> verifyY = new ArrayList<>();

        for (Point p : connections) {
            if (!x.contains((int) p.getX())) {
                return false;
            }
            if (!y.contains((int) p.getY())) {
                return false;
            }
            if (!verifyX.contains((int) p.getX())) {
                verifyX.add((int) p.getX());
            } else {
                return false;
            }
            if (!verifyY.contains((int) p.getY())) {
                verifyY.add((int) p.getY());
            } else {
                return false;
            }
        }

        return true;
    }

    public static void main(String args[]) {

        project3 project3 = new project3();
        Graph g = project3.createGraph();

        if (g == null) {
            return;
        }

        try  {
            if (!project3.validateGraph(g)) {
                return;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Graph not connected.");
            return;
        }

        if (!project3.checkBipartiteBfs(g)) {
            System.out.println("Error: Graph not bipartite!");
            return;
        }

        FlowNetwork flowNetwork = project3.createFlowNetwork(g);

        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, g.V() + 1);

        ArrayList<Point> toPrint = new ArrayList<>();
        if (fordFulkerson.value() > 0) {
            System.out.println("Matching found");

            for (int i = 1; i < g.V(); i++) {
                for (FlowEdge j : flowNetwork.adj(i)) {
                    if (j.from() != 0 && j.to() != g.V() + 1 && j.flow() >= 1.0) {
                        if (!toPrint.contains(new Point(j.from(), j.to()))) {
                            toPrint.add(new Point(j.from(), j.to()));
                        }
                        //System.out.println(j.flow() + " " + j.from() + " " + j.to());
                    }
                }
            }

            Collections.sort(toPrint, (point, t1) -> {
                return Integer.compare((int) point.getX(), (int) t1.getX());
            });

            for (int i = 0; i < toPrint.size(); i++) {
                Point p = toPrint.get(i);
                if (i == toPrint.size() - 1) {
                    System.out.print("(" + (int) p.getX() + "," + (int) p.getY() + ") ");
                } else {
                    System.out.print("(" + (int) p.getX() + "," + (int) p.getY() + "),");
                }
            }
            System.out.println("Number of edges " + Math.round(fordFulkerson.value()));
        }

        //System.out.println(project3.verifyOutput(toPrint));
    }
}