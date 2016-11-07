package clientCommunication;

import objects.Command;
import objects.Response;
import org.mindrot.jbcrypt.BCrypt;
import security.Security;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final int port = 2323;

    public static boolean isRunning = false;

    public static void main(String[] args) {
        try {
            isRunning = true;
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                Command command = (Command) ois.readObject();
                Response response = null;

                try {
                    System.out.println(command.getCommandType().toString());
                    response = (new Execute()).executeCommand(command);
                } catch (Exception e) {
                    System.out.println("Internal server error");
                    e.printStackTrace();
                }

                if (response == null) {
                    System.out.println("null");
                }

                try {
                    Security.removePasswords(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                oos.writeObject(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            isRunning = false;
        }
    }
}
