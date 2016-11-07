import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * CS180 - Project 5 - SafeWalk Server
 * <p/>
 * WORKED ALONE!
 *
 * @author hughe127
 * @date 19/11/14
 * @lab 802
 */
public class SafeWalkServer implements Runnable {

    /**
     * List of active connections
     */
    private ArrayList<Socket> connections = new ArrayList<Socket>();

    /**
     * List of current requests
     */
    private ArrayList<HashMap<String, String>> requests = new ArrayList<HashMap<String, String>>();

    ServerSocket safeWalk;

    BufferedReader br;

    static Socket tempStore;

    static int tempClient;

    /**
     * Construct the server, set up the socket.
     *
     * @throws SocketException if the socket or port cannot be obtained properly.
     * @throws IOException     if the port cannot be reused.
     */
    public SafeWalkServer(int port) throws SocketException, IOException {
        safeWalk = new ServerSocket(port);

    }

    /**
     * Construct the server and let the system allocate it a port.
     *
     * @throws SocketException if the socket or port cannot be obtained properly.
     * @throws IOException     if the port cannot be reused.
     */
    public SafeWalkServer() throws SocketException, IOException {
        safeWalk = new ServerSocket(0);

    }

    /**
     * Return the port number on which the server is listening.
     */
    public int getLocalPort() {
        return safeWalk.getLocalPort();
    }

    /**
     * Start a loop to accept incoming connections
     */
    public void run() {
        try {
            while (true) {
                Socket socket = safeWalk.accept();
                tempStore = socket;
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String requestOrCommand = br.readLine();
                System.out.println(requestOrCommand);
                if (requestOrCommand.charAt(0) == ':') {
                    if (validateCommand(requestOrCommand)) {
                        if (requestOrCommand.equals(":LIST_PENDING_REQUESTS")) {
                            String list = commandList();
                            PrintWriter pw = new PrintWriter(socket.getOutputStream());
                            pw.println(list);
                            pw.flush();
                            socket.close();
                        } else if (requestOrCommand.equals(":RESET")) {
                            System.out.println("test");
                            commandReset();
                            PrintWriter pw = new PrintWriter(socket.getOutputStream());
                            pw.println("RESPONSE: success");
                            pw.flush();
                            socket.close();
                        } else {
                            PrintWriter pw = new PrintWriter(socket.getOutputStream());
                            commandReset();
                            pw.println("RESPONSE: success");
                            pw.flush();
                            socket.close();
                            return;
                        }
                    } else {
                        PrintWriter pw = new PrintWriter(socket.getOutputStream());
                        pw.println("ERROR: invalid request");
                        pw.flush();
                        socket.close();
                    }
                } else {
                    if (validateRequest(requestOrCommand)) {

                        int commaOne = requestOrCommand.indexOf(',') + 1;
                        int commaTwo = requestOrCommand.indexOf(',', commaOne) + 1;
                        int commaThree = requestOrCommand.indexOf(',', commaTwo) + 1;

                        String one = requestOrCommand.substring(0, commaOne - 1);
                        String two = requestOrCommand.substring(commaOne, commaTwo - 1);
                        String three = requestOrCommand.substring(commaTwo, commaThree - 1);
                        String four = requestOrCommand.substring(commaThree, requestOrCommand.length());

                        HashMap<String, String> request = new HashMap<String, String>();
                        request.put("person", one);
                        request.put("from", two);
                        request.put("to", three);
                        request.put("priority", four);

                        if (!isCandidate(two, three)) {
                            connections.add(socket);
                            requests.add(request);
                        } else {
                            PrintWriter pw = new PrintWriter(connections.get(tempClient).getOutputStream());
                            pw.printf("RESPONSE: %s,%s,%s,%s", one, two, three, four);
                            pw.flush();
                            pw.close();
                            connections.get(tempClient).close();
                            connections.remove(tempClient);
                        }
                    } else {
                        PrintWriter pw = new PrintWriter(socket.getOutputStream());
                        pw.println("ERROR: invalid request");
                        pw.flush();
                        socket.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks to make sure that the entered string is valid
     *
     * @param request - String from user
     * @return true if the command is valid
     */
    private boolean validateRequest(String request) {

        int commaOne = request.indexOf(',') + 1;
        int commaTwo = request.indexOf(',', commaOne) + 1;
        int commaThree = request.indexOf(',', commaTwo) + 1;

        String one = null;
        String two = null;
        String three = null;
        String four = null;

        try {
            one = request.substring(0, commaOne - 1);
            two = request.substring(commaOne, commaTwo - 1);
            three = request.substring(commaTwo, commaThree - 1);
            four = request.substring(commaThree, request.length());
        } catch (StringIndexOutOfBoundsException e) {
            return false;
        }

        String[] locations = {"CL50", "EE", "LWSN", "PMU", "PUSH", "*"};

        boolean goodToGo1 = false;
        boolean goodToGo2 = false;
        boolean goodToGo = false;

        if (two.equals(three)) {
            return false;
        }

        if (two.equals("*")) {
            return false;
        }

        for (String matches : locations) {

            if (matches.equals(two)) {
                goodToGo1 = true;
                break;
            }
        }

        for (String matches : locations) {
            if (matches.equals(three)) {
                goodToGo2 = true;
                break;
            }
        }

        if (goodToGo1 && goodToGo2) {
            goodToGo = true;
        }

        return goodToGo;
    }

    /**
     * Tests to make sure command is one of the valid three commands
     *
     * @param command - Entered string from user
     * @return true if a good command
     */
    private boolean validateCommand(String command) {
        String[] validCommands = {":LIST_PENDING_REQUESTS",
                ":RESET", ":SHUTDOWN"};
        for (String commandTest : validCommands) {
            if (commandTest.equals(command)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Prints out requests
     *
     * @return string of requests
     */
    private String commandList() {
        String toList = "";
        for (int i = 0; i < requests.size(); i++) {
            toList += "[";
            toList += requests.get(i).get("person");
            toList += ",";
            toList += requests.get(i).get("from");
            toList += ",";
            toList += requests.get(i).get("to");
            toList += ",";
            toList += requests.get(i).get("priority");
            if (i == requests.size() - 1) {
                toList += "]";
            } else {
                toList += "],";
            }
        }
        return toList;
    }

    /**
     * Closes all active connections and removes them from the list of requests and connections
     *
     * @throws IOException
     */
    private void commandReset() throws IOException {
        for (int i = 0; i < connections.size(); i++) {
            PrintWriter pw = new PrintWriter(connections.get(i).getOutputStream());
            pw.println("ERROR: connection reset");
            pw.flush();
            connections.get(i).close();
        }
        requests.clear();
        connections.clear();
    }

    /**
     * Assigns people to a suitable "partner"
     * Will disconnect users and remove from lists if match was found
     *
     * @param fromOrig - originating location of most recent request
     * @param toDest   - destination of most recent request
     * @return true if a candidate was found
     * @throws IOException
     */
    public boolean isCandidate(String fromOrig, String toDest) throws IOException {
        for (int i = 0; i < requests.size(); i++) {
            if (requests.get(i).get("from").equals(fromOrig)) {
                if (toDest.equals("*") && requests.get(i).get("to").equals("*")) {
                    return false;
                } else if ((requests.get(i).get("to").equals(toDest) || (requests.get(i).get("to").equals("*")))
                        || (toDest.equals("*") && !requests.get(i).get("to").equals("*"))) {
                    PrintWriter pw = new PrintWriter(tempStore.getOutputStream());
                    String one = requests.get(i).get("person");
                    String two = requests.get(i).get("from");
                    String three = requests.get(i).get("to");
                    String four = requests.get(i).get("priority");
                    pw.printf("RESPONSE: %s,%s,%s,%s", one, two, three, four);
                    pw.flush();
                    pw.close();
                    tempClient = i;
                    requests.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Reclaims the port
     *
     * @throws SocketException
     */
    public void reclaim() throws SocketException {
        safeWalk.setReuseAddress(true);
    }

    /**
     * Takes user-defined port
     *
     * @param args - the port
     * @throws SocketException
     * @throws IOException
     */
    public static void main(String[] args) throws SocketException, IOException {
        SafeWalkServer safeWalkServer;
        int port;
        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Port is not a valid number!");
                return;
            }
            if (port > 65535 || port < 1025) {
                System.out.println("Port is not a valid port!");
                return;
            }
            try {
                safeWalkServer = new SafeWalkServer(port);
            } catch (BindException e) {
                System.out.println("Port is already bound by another program!");
                return;
            }
        } else if (args.length == 0) {
            safeWalkServer = new SafeWalkServer();
        } else {
            System.out.println("Argument count mismatch");
            return;
        }

        System.out.printf("Server is running on port: %d%n", safeWalkServer.getLocalPort());

        safeWalkServer.run();

        safeWalkServer.commandReset();

        safeWalkServer.reclaim();
    }
}
