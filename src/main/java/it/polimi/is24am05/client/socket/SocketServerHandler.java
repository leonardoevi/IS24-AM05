package it.polimi.is24am05.client.socket;

import it.polimi.is24am05.client.ServerHandler;
import it.polimi.is24am05.controller.server.socket.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class SocketServerHandler extends ServerHandler {
    private final Socket socket;
    private final ObjectOutputStream outputStream;

    public SocketServerHandler(String serverIP, String serverPort) throws IOException {
        super(serverIP, serverPort);

        socket = new Socket(serverIP, Integer.parseInt(serverPort));
        outputStream = new ObjectOutputStream(socket.getOutputStream());

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
    }

    public synchronized void send(Message message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException ignored) {
            System.out.println("Failed sending message");
        }
    }
}
