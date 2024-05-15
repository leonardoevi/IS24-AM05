package it.polimi.is24am05.controller.server.socket;

import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.controller.server.Network;
import it.polimi.is24am05.controller.server.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server using sockets protocol
 */
public class SocketServer implements Network {
    /**
     * Controller.
     */
    protected final Controller controller;

    /**
     * Server wrapping socket and RMI.
     */
    protected final Server server;

    /**
     * List of socket handlers.
     */
    private final List<SocketClientHandler> clients = new ArrayList<>();

    /**
     * Constructor.
     * @param controller controller.
     * @param server server wrapping socket and RMI.
     */
    public SocketServer(Controller controller, Server server) {
        this.controller = controller;
        this.server = server;
    }

    /**
     * Starts the server.
     */
    public void start() {
        ServerSocket serverSocket = null;

        // Define a fixed pool of threads to handle clients connections
        final ExecutorService threadPool = Executors.newFixedThreadPool(8);

        // Create the server socket to accept clients connections
        try {
            serverSocket = new ServerSocket(6969);
        } catch (IOException e) {
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
     * Subscribes the given socket handler to the server.
     * @param handler the socket handler to subscribe to the server.
     */
    public void subscribe(SocketClientHandler handler) {
        synchronized (clients) {
            clients.add(handler);
        }
    }

    /**
     * Unsubscribes the given socket handler to the server.
     * @param handler the socket handler to unsubscribe to the server.
     */
    public void unsubscribe(SocketClientHandler handler) {
        synchronized (clients) {
            clients.remove(handler);
        }
    }

    /**
     * Broadcasts a message to all clients.
     * @param message the message broadcasted.
     */
    @Override
    public void sendBroadcast(Message message) {
        for (String user : controller.getUsers())
            send(message, user);
    }

    /**
     * Broadcasts a message to all clients, except the given one.
     * @param message the message broadcasted.
     * @param client the client to not send the message to.
     */
    @Override
    public void sendBroadcast(Message message, String client) {
        for (String user : controller.getUsers()) {
            if (user.equals(client)) continue;
            send(message, user);
        }
    }

    /**
     * Sends a message to a client.
     * @param message the message broadcasted.
     * @param client the client to send the message to.
     */
    @Override
    public void send(Message message, String client) {
        List<SocketClientHandler> copy;
        synchronized (clients) {
            copy = new LinkedList<>(clients);
        }
        for (SocketClientHandler handler : copy) {
            if (handler.getClientNickname().equals(client))
                handler.send(message);
        }
    }

    /**
     * Gets the list of joined clients.
     * @return the list of joined clients.
     */
    @Override
    public List<String> getJoinedClients() {
        synchronized (clients) {
            return clients.stream().map(SocketClientHandler::getClientNickname).toList();
        }
    }
}
