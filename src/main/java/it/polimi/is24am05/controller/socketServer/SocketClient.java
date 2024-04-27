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

    //Out
    private PrintWriter socketOut;

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
        //final SocketClient client = new SocketClient("192.168.1.5", 6969);
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
        this.socketOut = new PrintWriter(socket.getOutputStream(), true);
        final Scanner stdin = new Scanner(System.in);

        // Start process to read input from server asynchronously
        new Thread(new ClientInReader(socket, this)).start();

        try {
            // Read stdin
            while (true) {
                final String inputLine = stdin.nextLine();
                if (inputLine.equals("quit")) {
                    send(inputLine);
                    break;
                }

                send(inputLine);
            }
        } catch (final NoSuchElementException e) {
            System.out.println("Connection closed");
        } finally {
            stdin.close();
            socketOut.close();
            socket.close();
        }
    }

    public synchronized void send(String line){
        socketOut.println(line);
    }

    /**
     * Runnable used to read messages from the Client's Socket input channel asynchronously
     */
    public class ClientInReader implements Runnable {
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
            final Scanner socketIn;
            try {
                socketIn = new Scanner(socket.getInputStream());
                while (true) {
                    handleServerInput(socketIn.nextLine());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NoSuchElementException | IllegalStateException ignored) {
            }
        }

        private void handleServerInput(String inputLine) {
            // TODO: fill this function according to the protocol
            if (inputLine.startsWith("ping,")) {
                String code = inputLine.substring(5);
                socketClient.send("pong," + code);
            } else {
                System.out.println(inputLine);
            }
        }
    }
}
