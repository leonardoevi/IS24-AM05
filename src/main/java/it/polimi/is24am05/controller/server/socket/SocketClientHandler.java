package it.polimi.is24am05.controller.server.socket;

import it.polimi.is24am05.client.model.DeckPov;
import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.controller.server.ClientHandler;
import it.polimi.is24am05.controller.server.Server;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.side.PlacedSide;
import it.polimi.is24am05.model.card.starterCard.StarterCard;
import it.polimi.is24am05.model.enums.Color;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.enums.state.GameState;
import it.polimi.is24am05.model.enums.state.PlayerState;
import it.polimi.is24am05.model.exceptions.game.NoSuchPlayerException;
import it.polimi.is24am05.model.objective.Objective;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SocketClientHandler extends ClientHandler implements Runnable {
    private final Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    volatile boolean isConnected = true;

    private volatile String pong = "";
    private final int checkingInterval = 1;
    private final ScheduledExecutorService clientChecker = Executors.newSingleThreadScheduledExecutor();

    private final Object lock = new Object();

    private final ExecutorService messageDecoder = Executors.newFixedThreadPool(4);

    public SocketClientHandler(Controller controller, Server server, Socket socket) {
        super(controller, server);
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());

            // this.clientChecker.scheduleAtFixedRate(new SocketClientChecker(this, checkingInterval / 2), 0, checkingInterval, TimeUnit.SECONDS);

            while (isConnected) {
                Message message;
                if(socket.getInputStream().available() > 0)
                    message = (Message) inputStream.readObject();
                else try {
                    synchronized (lock) {
                        lock.wait(100);
                    }
                    continue;
                } catch (InterruptedException e) {
                    // Should never happen
                    System.out.println(getNickname() + " handler crashed");
                    break;
                }

                switch (message.title()) {
                    case "quitServer":
                        break;
                    case "pong":
                        pong = (String) message.arguments().get("id");
                        break;
                    default:
                        messageDecoder.submit(new Thread(() -> {
                            handleClientInput(message);
                        }));
                }
            }
            clientDisconnected();
        } catch (Exception ignored) {}
    }

    public String getPong() {
        return pong;
    }

    public ScheduledExecutorService getClientChecker() {
        return clientChecker;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    private void handleClientInput(Message message) {
        switch (message.title()) {
            case "joinServer":
                super.joinServer((String) message.arguments().get("nickname"));
                break;
            case "joinGame":
                super.joinGame();
                break;
            case "setNumberOfPlayers":
                super.setNumberOfPlayers((int) message.arguments().get("numberOfPlayers"));
                break;
            case "placeStarterSide":
                super.placeStarterSide((boolean) message.arguments().get("isFront"));
                break;
            case "chooseObjective":
                super.chooseObjective((String) message.arguments().get("objectiveId"));
                break;
            case "placeSide":
                super.placeSide(
                        (String) message.arguments().get("cardId"),
                        (boolean) message.arguments().get("isFront"),
                        (int) message.arguments().get("i"),
                        (int) message.arguments().get("j")
                );
                break;
            case "drawVisible":
                super.drawVisible((String) message.arguments().get("cardId"));
                break;
            case "drawDeck":
                super.drawDeck((boolean) message.arguments().get("isGold"));
                break;
            case "quitServer":
                super.disconnect();
                break;
        }
    }

    @Override
    public void notifyJoinServer() {
        System.out.println("joinedServer being sent");
        send(new Message("joinedServer", Map.of()));
    }

    @Override
    public void notifyJoinGame(List<String> nicknames) {
        send(new Message("joinedGame", Map.of("nickname", nicknames)));
    }

    @Override
    public void notifyOthersJoinGame(String nickname) {
        send(new Message("otherJoinedGame", Map.of("nickname", nickname)));
    }

    @Override
    public void notifyGameCreated(
            DeckPov resourceDeck, DeckPov goldDeck,
            int playerTurn, Color color, StarterCard starterCard,
            List<Map<String, Object>> players)
    {
        send(new Message("gameCreated", Map.of(
                "resourceDeck", resourceDeck,
                "goldDeck", goldDeck,
                "playerTurn", playerTurn,
                "color", color,
                "starterCard", starterCard,
                "players", players
        )));
    }

    @Override
    public void notifyPlaceStarterSide(PlacedSide[][] playArea) {
        send(new Message("placedStarterSide", Map.of("playArea", playArea)));
    }

    @Override
    public void notifyOthersPlaceStarterSide(String nickname, PlacedSide[][] playArea) {
        send(new Message("otherPlacedStarterSide", Map.of(
                "nickname", nickname,
                "playArea", playArea
        )));
    }

    @Override
    public void notifyHandsAndObjectivesDealt(DeckPov resourceDeck, DeckPov goldDeck, List<Objective> objectives, List<Card> hand, List<Objective> playerObjectives, List<Map<String, Object>> players) {
        send(new Message("handsAndObjectivesDealt", Map.of(
                "resourceDeck", resourceDeck,
                "goldDeck", goldDeck,
                "objectives", objectives,
                "hand", hand,
                "playerObjectives", playerObjectives,
                "players", players
        )));
    }

    @Override
    public void notifyChooseObjective(Objective objective) {
        send(new Message("chosenObjective", Map.of("objective", objective)));
    }

    @Override
    public void notifyAllGameStarted() {
        send(new Message("gameStarted", Map.of()));
    }

    @Override
    public void notifyPlaceSide(PlacedSide[][] playArea, int points) {
        send(new Message("placedSide", Map.of(
                "playArea", playArea,
                "points", points
        )));
    }

    @Override
    public void notifyOthersPlaceSide(String nickname, PlacedSide[][] playArea, int points) {
        send(new Message("otherPlacedSide", Map.of(
                "nickname", nickname,
                "playArea", playArea,
                "points", points
        )));
    }

    @Override
    public void notifyDrawVisible(boolean isGold, DeckPov deck, List<Card> hand) {
        send(new Message("drawnVisible", Map.of(
                "isGold", isGold,
                "deck", deck,
                "hand", hand
        )));
    }

    @Override
    public void notifyOthersDrawVisible(String nickname, boolean isGold, DeckPov deck, List<Resource> hand) {
        send(new Message("otherDrawnVisible", Map.of(
                "nickname", nickname,
                "isGold", isGold,
                "deck", deck,
                "hand", hand
        )));
    }

    @Override
    public void notifyDrawDeck(boolean isGold, DeckPov deck, List<Card> hand) {
        send(new Message("drawnDeck", Map.of(
                "isGold", isGold,
                "deck", deck,
                "hand", hand
        )));
    }

    @Override
    public void notifyOthersDrawDeck(String nickname, boolean isGold, DeckPov deck, List<Resource> hand) {
        send(new Message("otherDrawnDeck", Map.of(
                "nickname", nickname,
                "isGold", isGold,
                "deck", deck,
                "hand", hand
        )));
    }

    @Override
    public void notifyGameResumed(
            GameState state, int turn, DeckPov resourceDeck, DeckPov goldDeck, List<Objective> objectives,
            PlayerState playerState, int playerTurn, Color color, StarterCard starterCard, List<Objective> playerObjectives, List<Card> hand, PlacedSide[][] playArea, int points,
            List<Map<String, Object>> players
    ) {
        Map<String, Object> arguments = Map.of(
                "state", state,
                "turn", turn,
                "resourceDeck", resourceDeck,
                "goldDeck", goldDeck,
                "objectives", objectives,
                "playerState", playerState,
                "playerTurn", playerTurn,
                "color", color,
                "starterCard", starterCard,
                "playerObjectives", playerObjectives
        );
        // As Map.of can take up to 10 keys, this is the best work around
        arguments.put("hand", hand);
        arguments.put("playArea", playArea);
        arguments.put("points", points);
        arguments.put("players", players);
        send(new Message("gameResumed", arguments));
    }

    @Override
    public void notifyOthersGameResumed(String nickname) {
        send(new Message("otherGameResumed", Map.of("nickname", nickname)));
    }

    @Override
    public void notifyOthersQuitGame(String nickname) {
        send(new Message("otherQuitGame", Map.of("nickname", nickname)));
    }

    @Override
    public void notifyAllGamePaused() {
        send(new Message("gamePaused", Map.of()));
    }

    @Override
    public void notifyException(Exception exception) {
        send(new Message("ko", Map.of(
                "exception", exception.toString(),
                "reason", exception.getMessage()
        )));
    }

    public synchronized void send(Message message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException ignored) {}
    }

    private void clientDisconnected() {
        if(!getNickname().isEmpty()) {
            try {
                getServer().unsubscribe(this);
                getController().disconnect(getNickname());
            } catch (NoSuchPlayerException ignored) {}
        }
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException ignored) {}
        this.messageDecoder.shutdown();
        try {
            socket.close();
        } catch (IOException ignored) {}
        clientChecker.shutdownNow();
    }
}
