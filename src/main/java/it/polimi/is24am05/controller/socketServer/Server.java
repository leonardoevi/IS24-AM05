package it.polimi.is24am05.controller.socketServer;

import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.controller.GameServer;

import java.util.List;

public class Server implements GameServer {
    private final Controller controller;
    private SocketServer socketServer;

    public Server(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void start() {
        socketServer = new SocketServer(controller, this);
        socketServer.start();
        // TODO: add RMI
    }

    @Override
    public void sendBroadcast(Message message) {
        socketServer.sendBroadcast(message);
        // TODO: add RMI
    }

    @Override
    public void sendBroadcast(Message message, String player) {
        socketServer.sendBroadcast(message, player);
        // TODO: add RMI
    }

    @Override
    public void send(Message message, String player) {
        socketServer.send(message, player);
        // TODO: add RMI
    }

    @Override
    public List<String> getJoinedClients() {
        return socketServer.getJoinedClients();
        // TODO: add RMI, merge lists
    }
}
