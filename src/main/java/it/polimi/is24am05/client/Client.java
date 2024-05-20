package it.polimi.is24am05.client;

import it.polimi.is24am05.client.rmi.RmiServerHandler;
import it.polimi.is24am05.client.socket.SocketServerHandler;

import static java.lang.Thread.sleep;

public class Client {
    public static void main(String[] args) throws Exception {
        /*
        ServerHandler serverHandler1 = new RmiServerHandler("localhost", "9696");
        sleep(2000);

        ServerHandler serverHandler2 = new RmiServerHandler("localhost", "9696");
        sleep(2000);

        serverHandler1.setNickname("Leo");
        serverHandler2.setNickname("Andre");


        serverHandler1.joinServer();            sleep(2000);
        serverHandler2.joinServer();            sleep(2000);

        serverHandler1.setNumberOfPlayers(2);   sleep(2000);
        serverHandler2.joinGame();              sleep(2000);

        serverHandler1.placeStarterSide(false); sleep(2000);
        serverHandler2.placeStarterSide(false); sleep(2000);
         */


        new SocketServerHandler("localhost", "6969");
    }
}
