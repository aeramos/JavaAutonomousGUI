package auto_gui.realtime;

import auto_gui.api.Position;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Client {
    private static final String SERVER_IP_ADDRESS = "server3"; // set to drive computer static ip/network name
    private static final int SERVER_PORT = 7777;
    private static Socket socket;
    private static ObjectOutputStream objectOutputStream;

    private Position position = new Position();

    public Client() throws IOException {
        socket = new Socket(SERVER_IP_ADDRESS, SERVER_PORT);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public boolean sendPosition() throws IOException {
        try {
            objectOutputStream.writeObject(position);
            return true;
        } catch (SocketException e) {
            System.out.println("Robot disconnected from server, reconnecting...");
            socket.close();
            socket = new Socket(SERVER_IP_ADDRESS, SERVER_PORT);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            return false;
        }
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
