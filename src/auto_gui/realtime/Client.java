package auto_gui.realtime;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    public static final String IP_ADDRESS = "127.0.0.1";
    private static Socket socket;
    private static ObjectOutputStream objectOutputStream;

    private int[] coordinates = new int[]{0, 0};

    public Client() throws IOException {
        socket = new Socket(IP_ADDRESS, Server.PORT);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public void sendCoordinates() throws IOException {
        objectOutputStream.writeObject(coordinates);
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }
}
