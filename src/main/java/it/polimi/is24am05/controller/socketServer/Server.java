package it.polimi.is24am05.controller.socketServer;

import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.controller.GameServer;

import java.util.List;

/**
 * Wrapper of socket and RMI server.
 */
public class Server implements GameServer {
    /**
     * Controller.
     */
    private final Controller controller;
    /**
     * Socket server.
     */
    private SocketServer socketServer;

    /**
     * Constructor.
     * @param controller controller.
     */
    public Server(Controller controller) {
        this.controller = controller;
    }

    /**
     * Starts the server.
     */
    @Override
    public void start() {
        socketServer = new SocketServer(controller, this);
        socketServer.start();
        // TODO: add RMI
    }

    /**
     * Broadcasts a message to all clients.
     * @param message the message broadcasted.
     */
    @Override
    public void sendBroadcast(Message message) {
        socketServer.sendBroadcast(message);
        // TODO: add RMI
    }

    /**
     * Broadcasts a message to all clients, except the given one.
     * @param message the message broadcasted.
     * @param client the client to not send the message to.
     */
    @Override
    public void sendBroadcast(Message message, String client) {
        socketServer.sendBroadcast(message, client);
        // TODO: add RMI
    }

    /**
     * Sends a message to a client.
     * @param message the message broadcasted.
     * @param client the client to send the message to.
     */
    @Override
    public void send(Message message, String client) {
        socketServer.send(message, client);
        // TODO: add RMI
    }

    /**
     * Gets the list of joined clients.
     * @return the list of joined clients.
     */
    @Override
    public List<String> getJoinedClients() {
        return socketServer.getJoinedClients();
        // TODO: add RMI, merge lists
    }
}
