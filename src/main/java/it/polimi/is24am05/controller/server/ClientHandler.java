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

        synchronized (server) {
            if (server.getNicknames().contains(nickname)) {
                addLog(new AlreadyUsedNicknameException(server.getNicknames()).getMessage());
                return;
            }
            this.nickname = nickname;
            server.subscribe(this);
        }
    }

    protected void joinGame() {
        if(nickname == null) {
            addLog("Identify yourself!");
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
}
