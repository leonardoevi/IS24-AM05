package it.polimi.is24am05.client.socket;

import it.polimi.is24am05.client.ServerHandler;
import it.polimi.is24am05.controller.server.socket.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * Socket implementation of the server handler.
 */
public class SocketServerHandler extends ServerHandler {
    /**
     * Socket of this client.
     */
    private final Socket socket;

    /**
     * Output stream of this client.
     */
    private final ObjectOutputStream outputStream;

    /**
     * Constructor.
     * @param serverIP the IP of the server.
     * @param serverPort the port of the server.
     * @param view "cli" for the CLI, "gui" for the GUI.
     * @throws IOException if an input or output error occurs.
     */
    public SocketServerHandler(String serverIP, String serverPort, String view) throws IOException {
        super(serverIP, serverPort, view);

        socket = new Socket(serverIP, Integer.parseInt(serverPort));
        outputStream = new ObjectOutputStream(socket.getOutputStream());

        // Starts a thread to read messages from the server
        new Thread(new SocketServerReader(this, socket)).start();
    }

    /**
     * Sends the message to join the server.
     */
    @Override
    public void joinServer() {
        System.out.println("sending join server");
        send(new Message("joinServer", Map.of("nickname", this.getNickname())));
    }

    /**
     * Sends the message to join the game.
     */
    @Override
    public void joinGame() {
        send(new Message("joinGame", Map.of()));
    }

    /**
     * Sends the message to create a new game with the given number of players and join it.
     * @param numberOfPlayers the number of the players of the game.
     */
    @Override
    public void setNumberOfPlayers(int numberOfPlayers) {
        send(new Message("setNumberOfPlayers", Map.of("numberOfPlayers", numberOfPlayers)));
    }

    /**
     * Sends the message to place the starter card on the given side.
     * @param isFront true for front side, false for back side.
     */
    @Override
    public void placeStarterSide(boolean isFront) {
        send(new Message("placeStarterSide", Map.of("isFront", isFront)));
    }

    /**
     * Sends the message to choose the given objective.
     * @param objectiveId the chosen objective.
     */
    @Override
    public void chooseObjective(String objectiveId) {
        send(new Message("chooseObjective", Map.of("objectiveId", objectiveId)));
    }

    /**
     * Sends the message to place the given card on the given side and the given coordinates.
     * @param cardId  the card to be placed.
     * @param isFront true for front side, false for back side.
     * @param i       the row at which to place the side.
     * @param j       the column at which to place the side.
     */
    @Override
    public void placeSide(String cardId, boolean isFront, int i, int j) {
        send(new Message("placeSide", Map.of(
                "cardId", cardId,
                "isFront", isFront,
                "i", i,
                "j", j
        )));
    }

    /**
     * Sends a message to draw the given visible card.
     * @param cardId the visible card to be drawn.
     */
    @Override
    public void drawVisible(String cardId) {
        send(new Message("drawVisible", Map.of("cardId", cardId)));
    }

    /**
     * Sends a message to draw from the given deck.
     * @param isGold true for gold deck, false for resource deck.
     */
    @Override
    public void drawDeck(boolean isGold) {
        send(new Message("drawDeck", Map.of("isGold", isGold)));
    }

    /**
     * Sends a message to disconnect from the server.
     */
    @Override
    public void quitServer() {
        send(new Message("quitServer", Map.of()));
    }

    /**
     * Sends the given message.
     * @param message the message to send.
     */
    public synchronized void send(Message message) {
        System.out.println("sending message");
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException ignored) {
            System.out.println("Failed sending message");
        }
    }
}
