package it.polimi.is24am05.controller.server.rmi;

import it.polimi.is24am05.client.model.DeckPov;
import it.polimi.is24am05.client.rmi.RmiVirtualClient;
import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.controller.server.ClientHandler;
import it.polimi.is24am05.controller.server.Server;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.side.PlacedSide;
import it.polimi.is24am05.model.card.starterCard.StarterCard;
import it.polimi.is24am05.model.deck.Deck;
import it.polimi.is24am05.model.enums.Color;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.enums.state.GameState;
import it.polimi.is24am05.model.enums.state.PlayerState;
import it.polimi.is24am05.model.game.Game;
import it.polimi.is24am05.model.objective.Objective;
import it.polimi.is24am05.model.playArea.PlayArea;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public class RmiClientHandler extends ClientHandler {
    private volatile String lastReceivedKey;
    private final RmiVirtualClient virtualClient;

    private final RmiVirtualController rmiFromClient;

    public RmiClientHandler(Controller controller, Server server, RmiVirtualClient virtualClient) throws RemoteException {
        super(controller, server);
        this.virtualClient = virtualClient;
        this.rmiFromClient = new RmiFromClient();
    }

    // This getter is needed to give the Client an object that implements the RmiVirtualController interface
    public RmiVirtualController getRmiVirtualController() {
        return rmiFromClient;
    }

    @Override
    public void notifyJoinServer() {

    }

    @Override
    public void notifyJoinGame(List<String> nicknames) {

    }

    @Override
    public void notifyOthersJoinGame(String nickname) {

    }

    @Override
    public void notifyGameCreated(DeckPov resourceDeck, DeckPov goldDeck, int playerTurn, Color color, StarterCard starterCard, List<Map<String, Object>> players) {

    }

    @Override
    public void notifyPlaceStarterSide(PlacedSide[][] playArea) {

    }

    @Override
    public void notifyOthersPlaceStarterSide(String nickname, PlacedSide[][] playArea) {

    }

    @Override
    public void notifyHandsAndObjectivesDealt(DeckPov resourceDeck, DeckPov goldDeck, List<Objective> objectives, List<Card> hand, List<Objective> playerObjectives, List<Map<String, Object>> players) {

    }

    @Override
    public void notifyChooseObjective(Objective objective) {

    }

    @Override
    public void notifyAllGameStarted() {

    }

    @Override
    public void notifyPlaceSide(PlacedSide[][] playArea, int points) {

    }

    @Override
    public void notifyOthersPlaceSide(String nickname, PlacedSide[][] playArea, int points) {

    }

    @Override
    public void notifyDrawVisible(boolean isGold, DeckPov deck, List<Card> hand) {

    }

    @Override
    public void notifyOthersDrawVisible(String nickname, boolean isGold, DeckPov deck, List<Resource> hand) {

    }

    @Override
    public void notifyDrawDeck(boolean isGold, DeckPov deck, List<Card> hand) {

    }

    @Override
    public void notifyOthersDrawDeck(String nickname, boolean isGold, DeckPov deck, List<Resource> hand) {

    }

    @Override
    public void notifyGameResumed(GameState state, int turn, DeckPov resourceDeck, DeckPov goldDeck, List<Objective> objectives, PlayerState playerState, int playerTurn, Color color, StarterCard starterCard, List<Objective> playerObjectives, List<Card> hand, PlacedSide[][] playArea, int points, List<Map<String, Object>> players) {

    }

    @Override
    public void notifyOthersGameResumed(String nickname) {

    }

    @Override
    public void notifyOthersQuitGame(String nickname) {

    }

    @Override
    public void notifyAllGamePaused() {

    }

    @Override
    public void notifyException(Exception exception) {

    }

    /*

    // Methods invoked by the Server, needed to notify the client of some events
    // FROM SERVER TO CLIENT

    @Override
    public void notifyJoinServer() {
        try {
            virtualClient.printMessageRMI("notifyJoinServer");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyJoinGame(List<String> nicknames) {
        try {
            virtualClient.printMessageRMI("notifyJoinGame");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyOthersJoinGame(String nickname) {
        try {
            virtualClient.printMessageRMI("notifyOthersJoinGame");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyGameCreated(Game pov) {
        try {
            virtualClient.printMessageRMI("notifyAllGameCreated");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyPlaceStarterSide(PlayArea playArea) {
        try {
            virtualClient.printMessageRMI("notifyPlaceStarterSide");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyOthersPlaceStarterSide(String nickname, PlayArea playArea) {
        try {
            virtualClient.printMessageRMI("notifyOthersPlaceStarterSide");
        } catch (RemoteException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyHandsAndObjectivesDealt(Game pov) {
        try {
            virtualClient.printMessageRMI("notifyAllHandsAndObjectivesDealt");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyChooseObjective() {
        try {
            virtualClient.printMessageRMI("notifyChooseObjective");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyAllGameStarted() {
        try {
            virtualClient.printMessageRMI("notifyAllGameStarted");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyPlaceSide(PlayArea playArea, int points) {
        try {
            virtualClient.printMessageRMI("notifyPlaceSide");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyOthersPlaceSide(String nickname, PlayArea playArea, int points) {
        try {
            virtualClient.printMessageRMI("notifyOthersPlaceSide");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyDrawVisible(Deck deck, List<Card> hand) {
        try {
            virtualClient.printMessageRMI("notifyDrawVisible");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyOthersDrawVisible(String nickname, boolean isGold, Deck deck, List<Card> hand) {
        try {
            virtualClient.printMessageRMI("notifyOthersDrawVisible");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyDrawDeck(Deck deck, List<Card> hand) {
        try {
            virtualClient.printMessageRMI("notifyDrawDeck");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyOthersDrawDeck(String nickname, boolean isGold, Deck deck, List<Card> hand) {
        try {
            virtualClient.printMessageRMI("notifyOthersDrawDeck");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyGameResumed(Game pov) {
        try {
            virtualClient.printMessageRMI("notifyGameResumed");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyOthersGameResumed(String nickname) {
        try {
            virtualClient.printMessageRMI("notifyOthersGameResumed");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyOthersQuitGame(String nickname) {
        try {
            virtualClient.printMessageRMI("notifyOthersQuitGame");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyAllGamePaused() {
        try {
            virtualClient.printMessageRMI("notifyAllGamePaused");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyException(Exception exception) {
        try {
            virtualClient.printMessageRMI(exception.getMessage());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    */

    // this class implements RMI methods, from the RmiVirtualController interface, invoked by the client
    // simply propagates RMI calls to the super class ClientHandler
    class RmiFromClient extends UnicastRemoteObject implements RmiVirtualController {

        protected RmiFromClient() throws RemoteException {
        }

        // FROM CLIENT TO SERVER

        @Override
        public void joinServerRMI(String nickname) throws RemoteException {
            joinServer(nickname);
        }

        @Override
        public void joinGameRMI() throws RemoteException {
            joinGame();
        }

        @Override
        public void setNumberOfPlayersRMI(int numberOfPlayers) throws RemoteException {
            setNumberOfPlayers(numberOfPlayers);
        }

        @Override
        public void placeStarterSideRMI(boolean isFront) throws RemoteException {
            placeStarterSide(isFront);
        }

        @Override
        public void chooseObjectiveRMI(String objectiveId) throws RemoteException {
            chooseObjective(objectiveId);
        }

        @Override
        public void placeSideRMI(String cardId, boolean isFront, int i, int j) throws RemoteException {
            placeSide(cardId, isFront, i, j);
        }

        @Override
        public void drawVisibleRMI(String cardId) throws RemoteException {
            drawVisible(cardId);
        }

        @Override
        public void drawDeckRMI(boolean isGold) throws RemoteException {
            drawDeck(isGold);
        }

        @Override
        public void disconnectRMI() throws RemoteException {
            disconnect();
        }

        @Override
        public void pong(String key) throws RemoteException {
            lastReceivedKey = key;
        }
    }
}


