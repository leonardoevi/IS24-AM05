package it.polimi.is24am05.controller.server;

import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.deck.Deck;
import it.polimi.is24am05.model.game.Game;
import it.polimi.is24am05.model.playArea.PlayArea;

import java.util.List;

/**
 * Output from the server to the client
 */
public interface VirtualClient {
    void setGame(Game game);
    void addLog(String log);
}
