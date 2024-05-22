package it.polimi.is24am05.client;

import it.polimi.is24am05.client.socket.SocketServerHandler;

import static java.lang.Thread.sleep;

public class Client {
    public static void main(String[] args) throws Exception {
        // TODO: currently acts as a test. Will instantiate the server handler specified by the first two args
        try {
            ServerHandler serverHandler1 = new SocketServerHandler("localhost", "6969", "tui");
            sleep(2000);
            ServerHandler serverHandler2 = new SocketServerHandler("localhost", "6969", "tui");
            sleep(2000);
            serverHandler1.setNickname("Leo");
            serverHandler2.setNickname("Andre");

            serverHandler1.joinServer();
            sleep(2000);
            serverHandler2.joinServer();
            sleep(2000);

            serverHandler1.setNumberOfPlayers(2);
            sleep(2000);
            serverHandler2.joinGame();
            sleep(2000);

            serverHandler1.placeStarterSide(false);
            sleep(2000);
            serverHandler2.placeStarterSide(false);
            sleep(2000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
