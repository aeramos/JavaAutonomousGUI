package auto_gui.realtime;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {
    public static final int PORT = 7777;

    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectInputStream inputStream;
    private int[] coordinates = new int[]{0, 0};

    private ScheduledExecutorService scheduledExecutorService;

    private boolean closed = false;

    public Server() throws IOException {
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        serverSocket = new ServerSocket(PORT);

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // check data sent to server
            try {
                coordinates = (int[])inputStream.readObject();
            } catch (SocketException | NullPointerException e) {
                if (!closed) {
                    System.out.println("Socket connection closed, reopening...");
                    connect();
                }
            } catch (EOFException e) {
                System.out.println("No more data to read from socket");
            } catch (ClassCastException e) {
                System.out.println("Error - the data is not of type: int[]");
            } catch (Exception | Error e) {
                System.out.println("Unexpected error, shutting down");
                e.printStackTrace();
                close();
            }
        }, 0, 1, TimeUnit.NANOSECONDS); // poll constantly
    }

    private void connect() {
        try {
            try {
                socket.close();
                inputStream.close();
            } catch (NullPointerException ignored) {
            }
            try {
                socket = serverSocket.accept();
            } catch (SocketException ignored) { // thrown if close() is called while waiting here
            }
            if (socket != null && !socket.isClosed()) {
                inputStream = new ObjectInputStream(socket.getInputStream());
                String address = socket.getRemoteSocketAddress().toString();
                address = address.substring(1, address.indexOf(":") + 1);
                address = address + socket.getLocalPort();
                System.out.println("New connection established with " + address);
            }
        } catch (IOException | NullPointerException e) {
            System.out.println("Error reopening socket, shutting down");
            e.printStackTrace();
            close();
        }
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public boolean isClosed() {
        return closed;
    }

    public void close() {
        closed = true;
        scheduledExecutorService.shutdown();
        try {
            if (socket != null) {
                socket.close();
            }
            serverSocket.close();
            System.out.println("Server shutdown");
        } catch (IOException e) {
            System.out.println("Unexpected error closing server socket");
            e.printStackTrace();
        }
    }
}
