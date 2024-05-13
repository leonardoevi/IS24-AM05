package it.polimi.is24am05.controller;

import it.polimi.is24am05.controller.socketServer.Message;

import java.util.List;

public interface GameServer {
    void start();

    void sendBroadcast(Message message);

    void sendBroadcast(Message message, String player);

    void send(Message message, String player);

    List<String> getJoinedClients();
}
