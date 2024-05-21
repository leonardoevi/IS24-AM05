package it.polimi.is24am05.client.socket;

import it.polimi.is24am05.client.ServerHandler;
import it.polimi.is24am05.controller.server.socket.Message;
import it.polimi.is24am05.model.game.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServerHandler extends ServerHandler {
    private final Socket socket;
    private final ObjectOutputStream outputStream;

    public static void main(String[] args) throws Exception {
        new SocketServerHandler("localhost", "6969");
    }

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
        } catch (IOException ignored) {
            System.out.println("Failed sending message");
            System.out.println(ignored.getMessage());
        }
    }

    public class SocketServerReader implements Runnable {

        private final SocketServerHandler socketServerHandler;
        private final Socket socket;

        private final ExecutorService messageDecoder = Executors.newFixedThreadPool(4);

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

                    if (message.title().equals("ping")) {
                        socketServerHandler.send(
                                new Message("pong", Map.of("id", message.arguments().get("id")))
                        );
                    } else {
                        handleServerInput(message);
                    }
                }
            } catch (Exception e) {
                System.out.println("Socket Reader exiting");
                e.printStackTrace();
            }
        }

        private void handleServerInput(Message message) {

            switch (message.title()) {
                case "Game":
                    // TODO : fix game message not returing what expected
                    setGame((Game) message.arguments().get("game"));
                    break;

                case "Log":
                    addLog((String) message.arguments().get("log"));
                    break;

                default:
                    System.out.println("Unknown message: " + message.title() + message.arguments());
                    break;

                /*
                case "joinedServer":
                    socketServerHandler.addLog("Connected to server");
                    break;
                case "joinedGame":
                    socketServerHandler.addLog((String) message.arguments().get("nicknames"));
                    break;
                case "otherJoinedGame":
                    socketServerHandler.addLog((String) message.arguments().get("nickname"));
                    break;
                case "setNumberOfPlayers":
                    socketServerHandler.addLog("Created game with specified number of players.");
                case "gameCreated":
                    socketServerHandler.setGameCreated(
                            (DeckPov) message.arguments().get("resourceDeck"),
                            (DeckPov) message.arguments().get("goldDeck"),
                            (int) message.arguments().get("turn"),
                            (Color) message.arguments().get("color"),
                            (StarterCard) message.arguments().get("starterCard"),
                            (List<Map<String, Object>>) message.arguments().get("players")
                    );
                    break;
                case "placedStarterSide":
                    socketServerHandler.setPlacedStarterSide(
                            (Side[][]) message.arguments().get("playArea")
                    );
                    break;
                case "otherPlacedStarterSide":
                    socketServerHandler.setOtherPlacedStarterSide(
                            (String) message.arguments().get("nickname"),
                            (Side[][]) message.arguments().get("playArea")
                    );
                    break;
                case "handsAndObjectivesDealt":
                    socketServerHandler.setHandsAndObjectivesDealt(
                            (DeckPov) message.arguments().get("resourceDeck"),
                            (DeckPov) message.arguments().get("goldDeck"),
                            (List<Objective>) message.arguments().get("commonObjectives"),
                            (List<Card>) message.arguments().get("hand"),
                            (List<Objective>) message.arguments().get("objectives"),
                            (List<Map<String, Object>>) message.arguments().get("players")
                    );
                    break;
                case "chosenObjective":
                    socketServerHandler.setChosenObjective(
                            (Objective) message.arguments().get("objective")
                    );
                    break;
                case "gameStarted":
                    socketServerHandler.setGameStarted();
                    break;
                case "placedSide":
                    socketServerHandler.setPlacedSide(
                            (Side[][]) message.arguments().get("playArea"),
                            (int) message.arguments().get("points")
                    );
                    break;
                case "otherPlacedSide":
                    socketServerHandler.setOtherPlacedSide(
                            (String) message.arguments().get("nickname"),
                            (Side[][]) message.arguments().get("playArea"),
                            (int) message.arguments().get("points")
                    );
                    break;
                case "drawnVisible":
                case "drawnDeck":
                    socketServerHandler.setDrawn(
                            (boolean) message.arguments().get("isGold"),
                            (DeckPov) message.arguments().get("deck"),
                            (List<Card>) message.arguments().get("hand")
                    );
                    break;
                case "otherDrawnVisible":
                case "otherDrawnDeck":
                    socketServerHandler.setOtherDrawn(
                            (String) message.arguments().get("nickname"),
                            (boolean) message.arguments().get("isGold"),
                            (DeckPov) message.arguments().get("deck"),
                            (List<Resource>) message.arguments().get("hand")
                    );
                    break;
                case "gameResumed":
                    socketServerHandler.setGameResumed(
                            (GameState) message.arguments().get("state"),
                            (int) message.arguments().get("turn"),
                            (DeckPov) message.arguments().get("resourceDeck"),
                            (DeckPov) message.arguments().get("goldDeck"),
                            (List<Objective>) message.arguments().get("objectives"),
                            (int) message.arguments().get("playerTurn"),
                            (Color) message.arguments().get("color"),
                            (StarterCard) message.arguments().get("starterCard"),
                            (List<Objective>) message.arguments().get("playerObjectives"),
                            (List<Card>) message.arguments().get("hand"),
                            (Side[][]) message.arguments().get("playArea"),
                            (int) message.arguments().get("points"),
                            (List<Map<String, Object>>) message.arguments().get("players")
                    );
                    break;
                case "otherGameResumed":
                    socketServerHandler.setOtherGameResumed(
                            (String) message.arguments().get("nickname")
                    );
                    break;
                case "otherQuitGame":
                    socketServerHandler.setOtherQuitGame(
                            (String) message.arguments().get("nickname")
                    );
                    break;
                case "gamePaused":
                    socketServerHandler.setGamePaused();
                    break;
                case "ko":
                    socketServerHandler.addLog((String) message.arguments().get("reason"));
                    break;

                 */
            }
        }
    }
}
