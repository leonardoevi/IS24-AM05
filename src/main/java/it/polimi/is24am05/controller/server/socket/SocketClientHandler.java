package it.polimi.is24am05.controller.server.socket;

import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.controller.server.ClientHandler;
import it.polimi.is24am05.controller.server.Server;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.deck.Deck;
import it.polimi.is24am05.model.exceptions.game.NoSuchPlayerException;
import it.polimi.is24am05.model.game.Game;
import it.polimi.is24am05.model.playArea.PlayArea;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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

            // this.clientChecker
            //        .scheduleAtFixedRate(new SocketClientChecker(this, checkingInterval / 2), 0, checkingInterval, TimeUnit.SECONDS);

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

                System.out.println("Reading message...");
                switch (message.title()) {
                    case "quitServer":
                        break;
                    case "pong":
                        pong = (String) message.arguments().get("id");
                        break;
                    default:
                        messageDecoder.submit(new Thread(()->{
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
                System.out.println("Joining server...");
                super.joinServer((String) message.arguments().get("nickname"));
                break;
            case "joinGame":
                super.joinGame();
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
    public void notifyGameCreated(Game pov) {
        send(new Message("gameCreated", Map.of("pov", pov)));
    }

    @Override
    public void notifyPlaceStarterSide(PlayArea playArea) {
        send(new Message("placedStarterSide", Map.of("playArea", playArea)));
    }

    @Override
    public void notifyOthersPlaceStarterSide(String nickname, PlayArea playArea) {
        send(new Message("otherPlacedStarterSide", Map.of(
                "nickname", nickname,
                "playArea", playArea
        )));
    }

    @Override
    public void notifyHandsAndObjectivesDealt(Game pov) {
        send(new Message("handAndObjectivesDealt", Map.of("pov", pov)));
    }

    @Override
    public void notifyChooseObjective() {
        send(new Message("chosenObjective", Map.of()));
    }

    @Override
    public void notifyAllGameStarted() {
        send(new Message("gameStarted", Map.of()));
    }

    @Override
    public void notifyPlaceSide(PlayArea playArea, int points) {
        send(new Message("placedSide", Map.of(
                "playArea", playArea,
                "points", points
        )));
    }

    @Override
    public void notifyOthersPlaceSide(String nickname, PlayArea playArea, int points) {
        send(new Message("otherPlacedSide", Map.of(
                "nickname", nickname,
                "playArea", playArea,
                "points", points
        )));
    }

    @Override
    public void notifyDrawVisible(Deck deck, List<Card> hand) {
        send(new Message("drawnVisible", Map.of(
                "deck", deck,
                "hand", hand
        )));
    }

    @Override
    public void notifyOthersDrawVisible(String nickname, boolean isGold, Deck deck, List<Card> hand) {
        send(new Message("otherDrawnVisible", Map.of(
                "nickname", nickname,
                "isGold", isGold,
                "deck", deck,
                "hand", hand
        )));
    }

    @Override
    public void notifyDrawDeck(Deck deck, List<Card> hand) {
        send(new Message("drawnDeck", Map.of(
                "deck", deck,
                "hand", hand
        )));
    }

    @Override
    public void notifyOthersDrawDeck(String nickname, boolean isGold, Deck deck, List<Card> hand) {
        send(new Message("otherDrawnDeck", Map.of(
                "nickname", nickname,
                "isGold", isGold,
                "deck", deck,
                "hand", hand
        )));
    }

    @Override
    public void notifyGameResumed(Game pov) {
        send(new Message("gameResumed", Map.of("pov", pov)));
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
