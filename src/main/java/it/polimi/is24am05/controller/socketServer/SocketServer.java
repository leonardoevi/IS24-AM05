package it.polimi.is24am05.controller.socketServer;

import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.controller.GameServer;

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
public class SocketServer implements GameServer {
    protected final Controller controller;
    protected final Server server;

    private final List<SocketClientHandler> clients = new ArrayList<>();

    public SocketServer(Controller controller, Server server) {
        this.controller = controller;
        this.server = server;
    }

    public void start() {
        ServerSocket serverSocket = null;

        // Define a fixed pool of threads to handle clients connections
        final ExecutorService threadPool = Executors.newFixedThreadPool(8);

        // Create the server socket to accept clients connections
        try {
            serverSocket = new ServerSocket(6969); // 0 means it looks for any open ports
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

    public void subscribe(SocketClientHandler handler) {
        synchronized (clients) {
            clients.add(handler);
        }
    }

    public void unsubscribe(SocketClientHandler handler) {
        synchronized (clients) {
            clients.remove(handler);
        }
    }

    @Override
    public void sendBroadcast(Message message) {
        for (String player : controller.getUsers())
            send(message, player);
    }

    @Override
    public void sendBroadcast(Message message, String player) {
        for (String user : controller.getUsers()) {
            if (user.equals(player)) continue;
            send(message, user);
        }
    }

    @Override
    public void send(Message message, String player) {
        List<SocketClientHandler> copy;
        synchronized (clients) {
            copy = new LinkedList<>(clients);
        }
        for (SocketClientHandler handler : copy) {
            if (handler.getClientNickname().equals(player))
                handler.send(message);
        }
    }

    @Override
    public List<String> getJoinedClients() {
        synchronized (clients) {
            return clients.stream().map(SocketClientHandler::getClientNickname).toList();
        }
    }
}
