package it.polimi.is24am05.client.socket;

import it.polimi.is24am05.client.model.DeckPov;
import it.polimi.is24am05.controller.server.socket.Message;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.card.starterCard.StarterCard;
import it.polimi.is24am05.model.enums.Color;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.enums.state.GameState;
import it.polimi.is24am05.model.objective.Objective;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServerReader implements Runnable {

    private final SocketServerHandler socketServerHandler;
    private final Socket socket;

    private final ExecutorService messageDecoder = Executors.newFixedThreadPool(4);
    private final Object lock = new Object();

    public SocketServerReader(SocketServerHandler socketServerHandler, Socket socket) {
        this.socketServerHandler = socketServerHandler;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            while (true) {
                Message message;
                if (inputStream.available() > 0)
                    message = (Message) inputStream.readObject();
                else try {
                    synchronized (lock) {
                        lock.wait(100);
                    }
                    continue;
                } catch (InterruptedException ignored) {
                    break;
                }

                if (message.title().equals("ping")) {
                    socketServerHandler.send(
                            new Message("pong", Map.of("id", message.arguments().get("id")))
                    );
                } else {
                    messageDecoder.submit(new Thread(() -> {
                        handleServerInput(message);
                    }));
                }
            }
        } catch (Exception ignored) {}
    }

    private void handleServerInput(Message message) {
        System.out.println("Handling server input...");
        socketServerHandler.printText(message.toString());

        switch (message.title()) {
            case "joinedServer":
                socketServerHandler.setConnected(true);
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
        }
    }
}
