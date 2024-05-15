package it.polimi.is24am05.controller.server;

import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.deck.Deck;
import it.polimi.is24am05.model.game.Game;
import it.polimi.is24am05.model.objective.Objective;
import it.polimi.is24am05.model.playArea.PlayArea;

import java.util.List;
import java.util.Map;

/**
 * Implemented by Server, Socket and RMI.
 */
public interface Network {
    void notifyException(String client, Exception exception);

    void notifyJoinServer(String client, String[] joinedUsers);

    void notifyOthersJoinServer(String client, String joinedUser);

    void notifyJoinGame(String client);

    void notifyOthersJoinGame(String client, String joinedPlayer);

    void notifySetNumberOfPlayers(String client);

    void notifyAllGameCreated(Deck resourceDeck, Deck goldDeck, Map<String, Object> players);

    void notifyPlaceStarterSide(String client, PlayArea playArea);

    void notifyOthersPlaceStarterSide(String client, String nickname, PlayArea playArea);

    void notifyHandsAndObjectivesDealt(String client, List<Card> hand, List<Objective> objectives);

    void notifyHandsAllAndObjectivesDealt(String client, List<Card> hand, List<Objective> objectives, Deck resourceDeck, Deck goldDeck, List<Objective> commonObjectives, Map<String, Object> players);

    void notifyChooseObjective(String client);

    void notifyAllGameStarted();

    void notifyPlaceSide();
}
