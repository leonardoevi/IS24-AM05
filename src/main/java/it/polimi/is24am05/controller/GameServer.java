package it.polimi.is24am05.controller;

public interface GameServer {
    void start();

    void sendBroadcast(String message);

    void sendBroadcast(String message, String player);

    void send(String message, String player);
}
