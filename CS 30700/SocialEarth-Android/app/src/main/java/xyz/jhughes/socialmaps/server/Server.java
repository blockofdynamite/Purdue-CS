package xyz.jhughes.socialmaps.server;

import android.content.Context;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import objects.Command;
import objects.Response;
import xyz.jhughes.socialmaps.R;

public class Server {
    public static Response sendCommandToServer(Command command, Context context) {
        try {
            InetSocketAddress sa = new InetSocketAddress(
                    context.getResources().getString(R.string.server_host),
                    context.getResources().getInteger(R.integer.server_port));
            Socket socket = new Socket();
            socket.connect(sa, 5000);
            if (!socket.isConnected()) {
                return null;
            }
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(command);
            oos.flush();
            Response response =  (Response) ois.readObject();
            ois.close();
            oos.close();
            socket.close();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Crash and burn.
        }
    }
}
