package it.polimi.is24am05.controller.server;

import it.polimi.is24am05.client.model.DeckPov;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.card.starterCard.StarterCard;
import it.polimi.is24am05.model.deck.Deck;
import it.polimi.is24am05.model.enums.Color;
import it.polimi.is24am05.model.game.Game;
import it.polimi.is24am05.model.playArea.PlayArea;

import java.util.List;
import java.util.Map;

/**
 * Output from the server to the client
 */
public interface VirtualClient {
    void notifyJoinServer();

    void notifyJoinGame(List<String> nicknames);

    void notifyOthersJoinGame(String nickname);

    void notifyGameCreated(DeckPov resourceDeck, DeckPov goldDeck, int playerTurn, Color color, StarterCard starterCard, List<Map<String, Object>> players);

    void notifyPlaceStarterSide(Side[][] playArea);

    void notifyOthersPlaceStarterSide(String nickname, Side[][] playArea);

    void notifyHandsAndObjectivesDealt(Game pov);

    void notifyChooseObjective();

    void notifyAllGameStarted();

    void notifyPlaceSide(PlayArea playArea, int points);

    void notifyOthersPlaceSide(String nickname, PlayArea playArea, int points);

    void notifyDrawVisible(Deck deck, List<Card> hand);

    void notifyOthersDrawVisible(String nickname, boolean isGold, Deck deck, List<Card> hand);

    void notifyDrawDeck(Deck deck, List<Card> hand);

    void notifyOthersDrawDeck(String nickname, boolean isGold, Deck deck, List<Card> hand);

    void notifyGameResumed(Game pov);

    void notifyOthersGameResumed(String nickname);

    void notifyOthersQuitGame(String nickname);

    void notifyAllGamePaused();

    void notifyException(Exception exception);
}
