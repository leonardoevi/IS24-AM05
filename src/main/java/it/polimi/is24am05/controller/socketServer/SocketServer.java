package it.polimi.is24am05.controller.socketServer;

import it.polimi.is24am05.controller.Controller;

import java.io.IOException;
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
    protected final Controller controller;

    // Observer pattern?
    private final List<SocketClientHandler> clients = new ArrayList<>();

    public SocketServer(Controller controller) {
        this.controller = controller;
    }

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

                // Let the thread pool handle the communication with the client
                threadPool.submit(new SocketClientHandler(socket, this));

            } catch (final IOException e) {
                break;
            }
        }

        System.out.println("Shutting down thread pool");
        threadPool.shutdown();
    }

    /**
     * Removes a Client from the broadcast list
     * @param toRemove client Handler handler to remove
     */
    public void removeClient(SocketClientHandler toRemove){
        synchronized (clients) {
            clients.remove(toRemove);
        }

        System.out.println("Client disconnected: " + toRemove.getClientNickname());
    }

    /**
     * Adds a Client to the broadcast list
     * @param toAdd client Handler to add
     */
    public void addClient(SocketClientHandler toAdd){
        synchronized (clients) {
            clients.add(toAdd);
        }
        System.out.println("Client connected: " + toAdd.getClientNickname());
    }

    /**
     * Used to send a message to all connected clients
     * @param message to send
     */
    public void sendBroadcast(String message){
        // Create a copy of the socket list
        List<SocketClientHandler> clientsListCopy;
        synchronized (this.clients) {
            clientsListCopy = new ArrayList<>(this.clients);
        }

        for (SocketClientHandler client : clientsListCopy) {
            client.send(message);
        }
    }

    /**
     * Used to send a message to all connected clients except for one
     * @param message to send
     * @param toAvoid client not to send the message to
     */
    public void sendBroadcast(String message, SocketClientHandler toAvoid){
        // Create a copy of the socket list
        List<SocketClientHandler> clientsListCopy;
        synchronized (this.clients) {
            clientsListCopy = new ArrayList<>(this.clients);
        }

        clientsListCopy.remove(toAvoid);

        for (SocketClientHandler client : clientsListCopy) {
            client.send(message);
        }
    }

    /*
    private class ConnectionsChecker implements Runnable {
        private final SocketServer parent;

        private ConnectionsChecker(SocketServer parent) {
            this.parent = parent;
        }

        @Override
        public void run() {
            System.out.println("Checking connections routine...");
            List<SocketServerClientHandler> clientsListCopy;
            synchronized (parent.clients) {
                clientsListCopy = new ArrayList<>(parent.clients);
            }

            for (SocketServerClientHandler client : clientsListCopy) {
                if(!client.isConnected()){
                    System.out.println("Connection checker noticed that " +client.getClientNickname()+ " is not connected");
                    parent.removeClient(client);
                }
            }
        }
    }
     */
}
