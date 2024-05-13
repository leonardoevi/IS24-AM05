package it.polimi.is24am05.controller;

import it.polimi.is24am05.controller.socketServer.Message;

import java.util.List;

/**
 * Implemented by Server, SocketServer and RMIServer.
 */
public interface GameServer {
    /**
     * Starts the server.
     */
    void start();

    /**
     * Broadcasts a message to all clients.
     * @param message the message broadcasted.
     */
    void sendBroadcast(Message message);

    /**
     * Broadcasts a message to all clients, except the given one.
     * @param message the message broadcasted.
     * @param client the client to not send the message to.
     */
    void sendBroadcast(Message message, String client);

    /**
     * Sends a message to a client.
     * @param message the message broadcasted.
     * @param client the client to send the message to.
     */
    void send(Message message, String client);

    /**
     * Gets the list of joined clients.
     * @return the list of joined clients.
     */
    List<String> getJoinedClients();
}
