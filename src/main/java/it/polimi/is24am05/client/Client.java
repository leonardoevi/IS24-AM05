package it.polimi.is24am05.client;

import it.polimi.is24am05.client.rmi.RmiServerHandler;

public class Client {
    public static void main(String[] args) {
        ServerHandler serverHandler = new RmiServerHandler("localhost", "9696");
        serverHandler.joinServer();

    }
}
