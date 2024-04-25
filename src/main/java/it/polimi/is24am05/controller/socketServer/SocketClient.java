package it.polimi.is24am05.controller.socketServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SocketClient {
    // Server ip address
    private final String ip;
    // Server port
    private final int port;

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
        final SocketClient client = new SocketClient("127.0.0.1", 6969);
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
        final PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
        final Scanner stdin = new Scanner(System.in);

        // Start process to read input from server asynchronously
        new Thread(new ClientInReader(socket)).start();

        try {
            // Read stdin
            while (true) {
                final String inputLine = stdin.nextLine();
                if (inputLine.equals("quit")) {
                    socketOut.println(inputLine);
                    break;
                }

                socketOut.println(inputLine);
            }
        } catch (final NoSuchElementException e) {
            System.out.println("Connection closed");
        } finally {
            stdin.close();
            socketOut.close();
            socket.close();
        }
    }

    /**
     * Runnable used to read messages from the Socket asynchronously
     */
    public static class ClientInReader implements Runnable {
        /**
         * Socket to read input from
         */
        private final Socket socket;

        public ClientInReader(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            final Scanner socketIn;
            try {
                socketIn = new Scanner(socket.getInputStream());
                while (true) {
                    // Prints to the console the contents of the Socket Input
                    System.out.println(socketIn.nextLine());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NoSuchElementException | IllegalStateException ignored) {
            }
        }
    }
}
