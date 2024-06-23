package it.polimi.is24am05.controller.server;

import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.controller.exceptions.AlreadyUsedNicknameException;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.goldCard.GoldCard;
import it.polimi.is24am05.model.card.resourceCard.ResourceCard;
import it.polimi.is24am05.model.exceptions.game.NoSuchPlayerException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles communication with a client
 */
public abstract class ClientHandler implements VirtualClient {
    private String nickname;
    private final Controller controller;
    private final Server server;

    public ClientHandler(Controller controller, Server server) {
        this.nickname = null;
        this.controller = controller;
        this.server = server;
    }

    public String getNickname() {
        return nickname;
    }

    public Controller getController() {
        return controller;
    }

    public Server getServer() {
        return server;
    }

    protected void joinServer(String nickname) {
        if(nickname == null) {
            addLog("Identify yourself!");
            return;
        }

        if(this.nickname != null) {
            addLog("Already logged in as " + this.nickname);
            return;
        }

        synchronized (server) {
            if (server.getNicknames().contains(nickname)) {
                addLog("Already connected users: " + new AlreadyUsedNicknameException(server.getNicknames()).getMessage());
                return;
            }
            this.nickname = nickname;
            server.subscribe(this);
        }
    }

    protected void leaveServer() {
        if(nickname == null) {
            addLog("At least join before leaving... UwU");
            return;
        }

        synchronized (server) {
            if (!server.getNicknames().contains(nickname)) {
                addLog("At least join before leaving... T^T");
                return;
            }

            addLog("Bye " + nickname);
            server.unsubscribe(this);
            try {
                controller.disconnect(nickname);
            } catch (NoSuchPlayerException e) {
                System.out.println("Controller was not able to disconnect " + nickname);
            }

            this.nickname = null;
        }
    }

    protected void joinGame() {
        if(nickname == null) {
            addLog("Identify yourself!");
            return;
        }

        if(!server.isSubscribed(this)){
            addLog("You are not subscribed to this game!");
            return;
        }

        try {
            controller.newConnection(nickname);
        } catch (Exception e) {
            addLog(e.getMessage());
        }
    }

    protected void setNumberOfPlayers(int numberOfPlayers) {
        if(nickname == null) {
            addLog("Identify yourself!");
            return;
        }

        if(!server.isSubscribed(this)){
            addLog("You are not subscribed to this game!");
            return;
        }

        try {
            controller.newConnection(nickname, numberOfPlayers);
        } catch (Exception e) {
            addLog(e.getMessage());
        }
    }

    protected void placeStarterSide(boolean isFront) {
        if(nickname == null) {
            addLog("Identify yourself!");
            return;
        }

        if(!server.isSubscribed(this)){
            addLog("You are not subscribed to this game!");
            return;
        }

        try {
            controller.playStarterCard(nickname, isFront);
        } catch (Exception e) {
            addLog(e.getMessage());
        }
    }

    protected void chooseObjective(String objectiveId) {
        if(nickname == null) {
            addLog("Identify yourself!");
            return;
        }

        if(!server.isSubscribed(this)){
            addLog("You are not subscribed to this game!");
            return;
        }

        try {
            controller.chooseObjective(nickname, objectiveId);
        } catch (Exception e) {
            addLog(e.getMessage());
        }
    }

    protected void placeSide(String cardId, boolean isFront, int i, int j) {
        if(nickname == null) {
            addLog("Identify yourself!");
            return;
        }

        if(!server.isSubscribed(this)){
            addLog("You are not subscribed to this game!");
            return;
        }

        try {
            Card card;
            try {
                card = ResourceCard.valueOf(cardId);
            } catch (IllegalArgumentException e) {
                card = GoldCard.valueOf(cardId);
            }
            controller.placeSide(nickname, card, isFront, i, j);
        } catch (Exception e) {
            addLog(e.getMessage());
        }
    }

    protected void drawVisible(String cardId) {
        if(nickname == null) {
            addLog("Identify yourself!");
            return;
        }

        if(!server.isSubscribed(this)){
            addLog("You are not subscribed to this game!");
            return;
        }

        try {
            Card card;
            try {
                card = ResourceCard.valueOf(cardId);
            } catch (IllegalArgumentException e) {
                card = GoldCard.valueOf(cardId);
            }
            controller.drawVisible(nickname, card);
        } catch (Exception e) {
            addLog(e.getMessage());
        }
    }

    protected void drawDeck(boolean isGold) {
        if(nickname == null) {
            addLog("Identify yourself!");
            return;
        }

        if(!server.isSubscribed(this)){
            addLog("You are not subscribed to this game!");
            return;
        }

        try {
            controller.drawDeck(nickname, isGold);
        } catch (Exception e) {
            addLog(e.getMessage());
        }
    }

    protected void disconnect() {
        try {
            if(nickname != null)
                controller.disconnect(nickname);

            server.unsubscribe(this);
        } catch (Exception e) {
            addLog(e.getMessage());
        }
    }

    protected void sendMessage(String message) {
        if(nickname == null) {
            addLog("Identify yourself!");
            return;
        }

        if (controller.game == null) {
            addLog("No game => no players to send the message to!");
            return;
        }

        if(!controller.game.getNicknames().contains(nickname)) {
            addLog("You are not part of the game!");
            return;
        }

        List<String> recipients = new ArrayList<>(controller.game.getNicknames());
        recipients.remove(this.nickname);
        server.broadcastLog(recipients, "[" + this.nickname + "] " + message);
    }

    protected void sendMessage(String message, String recipientNickname) {
        if(nickname == null) {
            addLog("Identify yourself!");
            return;
        }

        if(this.nickname.equals(recipientNickname)) {
            addLog("Soliloquy non permitted!");
            return;
        }

        if(!server.getNicknames().contains(recipientNickname)) {
            addLog(recipientNickname + " is not connected");
            return;
        }

        server.broadcastLog(List.of(recipientNickname), "{" + this.nickname + "} " + message);
    }
}
