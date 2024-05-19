package it.polimi.is24am05.controller.server;

import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.controller.exceptions.AlreadyUsedNicknameException;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.goldCard.GoldCard;
import it.polimi.is24am05.model.card.resourceCard.ResourceCard;

public abstract class ClientHandler implements VirtualClient {
    private String nickname;
    private final Controller controller;
    private final Server server;

    public ClientHandler(Controller controller, Server server) {
        this.nickname = null;
        this.controller = controller;
        this.server = server;
    }

    protected void joinServer(String nickname) {
        synchronized (server) {
            if (server.getNicknames().contains(nickname)) {
                notifyException(new AlreadyUsedNicknameException(server.getNicknames()));
                return;
            }
            this.nickname = nickname;
            server.subscribe(this);
            notifyJoinServer();
        }
    }

    protected void joinGame() {
        try {
            controller.newConnection(nickname);
        } catch (Exception e) {
            notifyException(e);
        }
    }

    protected void setNumberOfPlayers(int numberOfPlayers) {
        try {
            controller.newConnection(nickname, numberOfPlayers);
        } catch (Exception e) {
            notifyException(e);
        }
    }

    protected void placeStarterSide(boolean isFront) {
        try {
            controller.playStarterCard(nickname, isFront);
        } catch (Exception e) {
            notifyException(e);
        }
    }

    protected void chooseObjective(String objectiveId) {
        try {
            controller.chooseObjective(nickname, objectiveId);
        } catch (Exception e) {
            notifyException(e);
        }
    }

    protected void placeSide(String cardId, boolean isFront, int i, int j) {
        try {
            Card card;
            try {
                card = ResourceCard.valueOf(cardId);
            } catch (IllegalArgumentException e) {
                card = GoldCard.valueOf(cardId);
            }
            controller.placeSide(nickname, card, isFront, i, j);
        } catch (Exception e) {
            notifyException(e);
        }
    }

    protected void drawVisible(String cardId) {
        try {
            Card card;
            try {
                card = ResourceCard.valueOf(cardId);
            } catch (IllegalArgumentException e) {
                card = GoldCard.valueOf(cardId);
            }
            controller.drawVisible(nickname, card);
        } catch (Exception e) {
            notifyException(e);
        }
    }

    protected void drawDeck(boolean isGold) {
        try {
            controller.drawDeck(nickname, isGold);
        } catch (Exception e) {
            notifyException(e);
        }
    }

    protected void disconnect() {
        try {
            controller.disconnect(nickname);
            server.unsubscribe(this);
        } catch (Exception e) {
            notifyException(e);
        }
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
}
