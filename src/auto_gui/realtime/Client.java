package auto_gui.realtime;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Client {
    private static final String SERVER_IP_ADDRESS = "127.0.0.1"; // set to drive computer static ip/network name
    private static Socket socket;
    private static ObjectOutputStream objectOutputStream;

    private int[] coordinates = new int[]{0, 0};

    public Client() throws IOException {
        socket = new Socket(SERVER_IP_ADDRESS, Server.PORT);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public boolean sendCoordinates() throws IOException {
        try {
            objectOutputStream.writeObject(coordinates);
            return true;
        } catch (SocketException e) {
            System.out.println("Robot disconnected from server, reconnecting...");
            socket.close();
            socket = new Socket(SERVER_IP_ADDRESS, Server.PORT);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            return false;
        }
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }
}
