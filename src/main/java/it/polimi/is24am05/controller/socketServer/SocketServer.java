package it.polimi.is24am05.controller.socketServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server using sockets protocol
 */
public class SocketServer {
    // Observer pattern?
    private final List<Socket> clientSockets = new ArrayList<>();

    public void start() {
        ServerSocket serverSocket = null;

        // Define a fixed pool of threads to handle clients connections
        final ExecutorService threadPool = Executors.newFixedThreadPool(8);

        // Create the server socket to accept clients connections
        try {
            serverSocket = new ServerSocket(6969); // 0 means it looks for any open ports
        } catch (final IOException e) {
            System.err.println(e.getMessage());
            return;
        }

        System.out.println("Socket Server ready on port: " + serverSocket.getLocalPort());

        // Keep accepting connections
        while (true) {
            try {
                final Socket socket = serverSocket.accept();

                System.out.println("New client connected: " + socket.getRemoteSocketAddress());

                // Let the thread pool handle the communication with the client
                threadPool.submit(new SocketServerClientHandler(socket, this));

                // TODO: maybe delay this step after the connection has been approved by the controller
                // Add the client to the list of output stream for broadcast
                synchronized (clientSockets) {
                    clientSockets.add(socket);
                }

            } catch (final IOException e) {
                break;
            }
        }
        threadPool.shutdown();
        return;
    }

    /**
     * Removes a Client from the broadcast list
     * @param toRemove client socket to remove
     */
    public void removeClient(Socket toRemove){
        synchronized (clientSockets) {
            clientSockets.remove(toRemove);
        }
        System.out.println("Client disconnected: " + toRemove.getRemoteSocketAddress());
    }

    /**
     * Used to send a message to all connected clients
     * @param message to send
     */
    public void sendBroadcast(String message){
        // Create a copy of the socket list
        List<Socket> socketsListCopy;
        synchronized (clientSockets) {
            socketsListCopy = new ArrayList<>(clientSockets);
        }

        for (Socket socket : socketsListCopy) {
            // Send the message to each client
            // Sending messages must be sync to avoid interference with ClientHandler
            synchronized (socket){
                try {
                    new PrintWriter(socket.getOutputStream(), true).println(message);
                } catch (IOException ignored) {}
            }
        }

    }

    /**
     * Used to send a message to all connected clients except for one
     * @param message to send
     * @param toAvoid client not to send the message to
     */
    public void sendBroadcast(String message, Socket toAvoid){
        // Create a copy of the socket list
        List<Socket> socketsListCopy;
        synchronized (clientSockets) {
            socketsListCopy = new ArrayList<>(clientSockets);
        }

        for (Socket socket : socketsListCopy) {
            // Send the message to each client
            // Sending messages must be sync to avoid interference with ClientHandler
            synchronized (socket){
                if(socket.equals(toAvoid))
                    continue;

                try {
                    new PrintWriter(socket.getOutputStream(), true).println(message);
                } catch (IOException e) {
                    // If there is some errors remove the socket form the list
                    removeClient(socket);
                }
            }
        }

    }

    public static void main(String[] args) {
        new SocketServer().start();
    }
}
