package it.polimi.is24am05.controller.server;

import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.deck.Deck;
import it.polimi.is24am05.model.game.Game;
import it.polimi.is24am05.model.playArea.PlayArea;

import java.net.Socket;
import java.util.List;

public class SocketClientHandler extends ClientHandler implements Runnable {
    private final Socket socket;

    public SocketClientHandler(Controller controller, Server server, Socket socket) {
        super(controller, server);
        this.socket = socket;
    }

    @Override
    public void run() {

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
    public void notifyAllGameCreated(Game pov) {

    }

    @Override
    public void notifyPlaceStarterSide(PlayArea playArea) {

    }

    @Override
    public void notifyOthersPlaceStarterSide(String nickname, PlayArea playArea) {

    }

    @Override
    public void notifyAllHandsAndObjectivesDealt(Game pov) {

    }

    @Override
    public void notifyChooseObjective() {

    }

    @Override
    public void notifyAllGameStarted() {

    }

    @Override
    public void notifyPlaceSide(PlayArea playArea, int points) {

    }

    @Override
    public void notifyOthersPlaceSide(String nickname, PlayArea playArea, int points) {

    }

    @Override
    public void notifyDrawVisible(Deck deck, List<Card> hand) {

    }

    @Override
    public void notifyOthersDrawVisible(String nickname, boolean isGold, Deck deck, List<Card> hand) {

    }

    @Override
    public void notifyDrawDeck(Deck deck, List<Card> hand) {

    }

    @Override
    public void notifyOthersDrawDeck(String nickname, boolean isGold, Deck deck, List<Card> hand) {

    }

    @Override
    public void notifyGameResumed(Game pov) {

    }

    @Override
    public void notifyOthersGameResumed(String nickname) {

    }

    @Override
    public void notifyException(Exception exception) {

    }

    private synchronized void send() {

    }


}
