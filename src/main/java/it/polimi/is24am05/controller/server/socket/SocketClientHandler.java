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

            //this.clientChecker
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

                switch (message.title()) {
                    case "quitServer": break;
                    case "pong": pong = (String) message.arguments().get("id");
                    default: messageDecoder.submit(new Thread(()->{
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
            case "disconnect":
                super.disconnect();
                break;
        }
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

    @Override
    public void setGame(Game game) {
        System.out.println("Sending via socket the following game");
        System.out.println(game.toString());
        send(new Message("Game", Map.of("game", game)));
    }

    @Override
    public void addLog(String log) {
        send(new Message("Log", Map.of("log", log)));
    }
}
