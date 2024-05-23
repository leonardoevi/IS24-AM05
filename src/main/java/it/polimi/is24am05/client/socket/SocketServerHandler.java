package it.polimi.is24am05.client.socket;

import it.polimi.is24am05.client.ServerHandler;
import it.polimi.is24am05.controller.server.socket.Message;
import it.polimi.is24am05.model.game.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class SocketServerHandler extends ServerHandler {
    private final Socket socket;
    private final ObjectOutputStream outputStream;

    public static void main(String[] args) {
        try {
            new SocketServerHandler("localhost", "6969");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public SocketServerHandler(String serverIP, String serverPort) throws IOException {
        super(serverIP, serverPort);

        try {
            this.socket = new Socket(serverIP, Integer.parseInt(serverPort));
        } catch (IOException e) {
            notifyViewServerUnreachable();
            throw new IOException("Socket connection to server failed");
        }
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());

        new Thread(new SocketServerReader(this, socket)).start();
    }

    @Override
    public void joinServer() {
        send(new Message("joinServer", Map.of("nickname", this.getNickname())));
    }

    @Override
    public void joinGame() {
        send(new Message("joinGame", Map.of()));
    }

    @Override
    public void setNumberOfPlayers(int numberOfPlayers) {
        send(new Message("setNumberOfPlayers", Map.of("numberOfPlayers", numberOfPlayers)));
    }

    @Override
    public void placeStarterSide(boolean isFront) {
        send(new Message("placeStarterSide", Map.of("isFront", isFront)));
    }

    @Override
    public void chooseObjective(String objectiveId) {
        send(new Message("chooseObjective", Map.of("objectiveId", objectiveId)));
    }

    @Override
    public void placeSide(String cardId, boolean isFront, int i, int j) {
        send(new Message("placeSide", Map.of(
                "cardId", cardId,
                "isFront", isFront,
                "i", i,
                "j", j
        )));
    }

    @Override
    public void drawVisible(String cardId) {
        send(new Message("drawVisible", Map.of("cardId", cardId)));
    }

    @Override
    public void drawDeck(boolean isGold) {
        send(new Message("drawDeck", Map.of("isGold", isGold)));
    }

    @Override
    public void disconnect() {
        send(new Message("disconnect", Map.of()));
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Error closing Socket");
        }
    }

    public synchronized void send(Message message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Failed sending message");
            System.out.println(e.getMessage());
        }
    }

    public static class SocketServerReader implements Runnable {

        private final SocketServerHandler socketServerHandler;
        private final Socket socket;

        public SocketServerReader(SocketServerHandler socketServerHandler, Socket socket) {
            this.socketServerHandler = socketServerHandler;
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                while (true) {
                    Message message = (Message) inputStream.readObject();

                    handleServerInput(message);
                }
            } catch (Exception e) {
                //System.out.println("Socket Reader exiting");
                socketServerHandler.notifyViewServerUnreachable();
            }
        }

        private void handleServerInput(Message message) {

            switch (message.title()) {
                case "Game":
                    socketServerHandler.setGame((Game) message.arguments().get("game"));
                    break;

                case "Log":
                    socketServerHandler.addLog((String) message.arguments().get("log"));
                    break;

                default:
                    System.out.println("Unknown message: " + message.title() + message.arguments());
                    break;
            }
        }
    }
}
