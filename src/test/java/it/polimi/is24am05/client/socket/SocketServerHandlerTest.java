package it.polimi.is24am05.client.socket;

import it.polimi.is24am05.controller.server.socket.Message;
import it.polimi.is24am05.model.exceptions.game.*;
import it.polimi.is24am05.model.exceptions.player.InvalidStarterSideException;
import it.polimi.is24am05.model.game.Game;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

class SocketServerHandlerTest {
    // Test what is wrong with socket client server communication
    @Test
    void Server() throws InterruptedException, TooManyPlayersException, TooFewPlayersException, PlayerNamesMustBeDifferentException, NoSuchPlayerException, MoveNotAllowedException, InvalidStarterSideException {
        new DummyServer(1234).run();
    }

    @Test
    void Client() throws TooManyPlayersException, TooFewPlayersException, PlayerNamesMustBeDifferentException, NoSuchPlayerException, MoveNotAllowedException, InvalidStarterSideException {
        DummyClient dummyClient = new DummyClient(1234);
        dummyClient.start();
    }

    class DummyServer implements Runnable{
        private final int port;
        private ObjectOutputStream out;

        public DummyServer(int port) {
            this.port = port;
        }


        public void run(){
            try {
                ServerSocket serverSocket = new ServerSocket(port);

                Socket socket = serverSocket.accept();
                out = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("Client connected");


                Game game = new Game(List.of("Leo", "Andre"));
                game.placeStarterSide("Leo", game.getStarterCard("Leo").getBackSide());

                send(new Message("Game", Map.of("game", game)));

            } catch (IOException | TooManyPlayersException | NoSuchPlayerException | TooFewPlayersException |
                     MoveNotAllowedException | PlayerNamesMustBeDifferentException | InvalidStarterSideException e) {
                throw new RuntimeException(e);
            }
        }

        public void send(Message message){
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class DummyClient{
        private final int port;
        private ObjectInputStream in;

        public DummyClient(int port) {
            this.port = port;
        }

        public void start(){
            try {
                Socket socket = new Socket("localhost", port);
                in = new ObjectInputStream(socket.getInputStream());

                Message message = (Message) in.readObject();
                Game game = (Game) message.arguments().get("game");
                System.out.println(game);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}