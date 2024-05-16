package it.polimi.is24am05.controller.server.rmi;

import it.polimi.is24am05.client.rmi.RmiVirtualClient;
import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.controller.server.ClientHandler;
import it.polimi.is24am05.controller.server.Server;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.deck.Deck;
import it.polimi.is24am05.model.game.Game;
import it.polimi.is24am05.model.playArea.PlayArea;

import java.rmi.RemoteException;
import java.util.List;

public class RmiClientHandler extends ClientHandler implements RmiVirtualController {
    private volatile String lastReceivedKey;
    private final RmiVirtualClient virtualClient;

    public RmiClientHandler(Controller controller, Server server, RmiVirtualClient virtualClient) {
        super(controller, server);
        this.virtualClient = virtualClient;
    }

    // Methods invoked by the Server, needed to notify the client of some events

    @Override
    public void notifyJoinServer() {
        try {
            virtualClient.printMessage("notifyJoinServer");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyJoinGame(List<String> nicknames) {
        try {
            virtualClient.printMessage("notifyJoinGame");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyOthersJoinGame(String nickname) {
        try {
            virtualClient.printMessage("notifyOthersJoinGame");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyAllGameCreated(Game pov) {
        try {
            virtualClient.printMessage("notifyAllGameCreated");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyPlaceStarterSide(PlayArea playArea) {
        try {
            virtualClient.printMessage("notifyPlaceStarterSide");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyOthersPlaceStarterSide(String nickname, PlayArea playArea) {
        try {
            virtualClient.printMessage("notifyOthersPlaceStarterSide");
        } catch (RemoteException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyAllHandsAndObjectivesDealt(Game pov) {
        try {
            virtualClient.printMessage("notifyAllHandsAndObjectivesDealt");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyChooseObjective() {
        try {
            virtualClient.printMessage("notifyChooseObjective");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyAllGameStarted() {
        try {
            virtualClient.printMessage("notifyAllGameStarted");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyPlaceSide(PlayArea playArea, int points) {
        try {
            virtualClient.printMessage("notifyPlaceSide");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyOthersPlaceSide(String nickname, PlayArea playArea, int points) {
        try {
            virtualClient.printMessage("notifyOthersPlaceSide");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyDrawVisible(Deck deck, List<Card> hand) {
        try {
            virtualClient.printMessage("notifyDrawVisible");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyOthersDrawVisible(String nickname, boolean isGold, Deck deck, List<Card> hand) {
        try {
            virtualClient.printMessage("notifyOthersDrawVisible");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyDrawDeck(Deck deck, List<Card> hand) {
        try {
            virtualClient.printMessage("notifyDrawDeck");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyOthersDrawDeck(String nickname, boolean isGold, Deck deck, List<Card> hand) {
        try {
            virtualClient.printMessage("notifyOthersDrawDeck");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyGameResumed(Game pov) {
        try {
            virtualClient.printMessage("notifyGameResumed");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyOthersGameResumed(String nickname) {
        try {
            virtualClient.printMessage("notifyOthersGameResumed");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyException(Exception exception) {
        try {
            virtualClient.printMessage(exception.getMessage());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    // RMI methods, implemented from the RmiVirtualController interface, invoked by the client

    @Override
    public void joinServerRMI(String nickname) throws RemoteException {
        super.joinServer(nickname);
    }

    @Override
    public void joinGameRMI() throws RemoteException {
        super.joinGame();
    }

    @Override
    public void setNumberOfPlayersRMI(int numberOfPlayers) throws RemoteException {
        super.setNumberOfPlayers(numberOfPlayers);
    }

    @Override
    public void placeStarterSideRMI(boolean isFront) throws RemoteException {
        super.placeStarterSide(isFront);
    }

    @Override
    public void chooseObjectiveRMI(String objectiveId) throws RemoteException {
        super.chooseObjective(objectiveId);
    }

    @Override
    public void placeSideRMI(String cardId, boolean isFront, int i, int j) throws RemoteException {
        super.placeSide(cardId, isFront, i, j);
    }

    @Override
    public void drawVisibleRMI(String cardId) throws RemoteException {
        super.drawVisible(cardId);
    }

    @Override
    public void drawDeckRMI(boolean isGold) throws RemoteException {
        super.drawDeck(isGold);
    }

    @Override
    public void disconnectRMI() throws RemoteException {
        super.disconnect();
    }

    @Override
    public void pong(String key) throws RemoteException {
        this.lastReceivedKey = key;
    }
}


