package it.polimi.is24am05.controller;

public interface GameServer {
    public void start();

    public void sendBroadcast(String message);
}
