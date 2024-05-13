package it.polimi.is24am05.controller.socketServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

public class SocketClient {
    // Server ip address
    private final String ip;
    // Server port
    private final int port;

    //Out
    private ObjectOutputStream socketOut;

    /**
     * Constructor
     * @param ip Server IP in string format "255.255.255.255"
     * @param port Server Port [0 - 65535]
     */
    public SocketClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static void main(String[] args) {
        final SocketClient client = new SocketClient("localhost", 6969);
        try {
            client.startClient();
        } catch (final IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void startClient() throws IOException {
        // Connect to server
        final Socket socket = new Socket(ip, port);
        System.out.println("Connection to server established");

        // Create i/o to server
        this.socketOut = new ObjectOutputStream(socket.getOutputStream());
        final Scanner stdin = new Scanner(System.in);

        // Start process to read input from server asynchronously
        new Thread(new ClientInReader(socket, this)).start();

        try {
            // Read stdin
            while (true) {
                final String message = stdin.nextLine();
                send(new Message(message, null));
                if (message.equals("quitServer"))
                    break;
            }
        } catch (Exception e) {
            System.out.println("Connection closed");
        } finally {
            stdin.close();
            socketOut.close();
            socket.close();
        }
    }

    public synchronized void send(Message message){
        try {
            System.out.println("Sending message: " + message.title() + message.arguments());
            socketOut.writeObject(message);
            socketOut.flush();
        } catch (Exception ignored) {}
    }

    /**
     * Runnable used to read messages from the Client's Socket input channel asynchronously
     */
    public static class ClientInReader implements Runnable {
        /**
         * Socket to read input from
         */
        private final Socket socket;

        private final SocketClient socketClient;

        public ClientInReader(Socket socket, SocketClient socketClient) {
            this.socket = socket;
            this.socketClient = socketClient;
        }

        @Override
        public void run() {
            final ObjectInputStream socketIn;
            try {
                socketIn = new ObjectInputStream(socket.getInputStream());
                while (true) {
                    handleServerInput((Message) socketIn.readObject());
                }
            } catch (Exception ignored) {}
        }

        private void handleServerInput(Message message) {
            if (message.title().equals("ping"))
                socketClient.send(new Message("pong", Map.of(
                    "id", message.arguments().get("id")
                )));
            else System.out.println(message.title() + message.arguments());
        }
    }
}
