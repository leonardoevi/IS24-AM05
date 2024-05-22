package it.polimi.is24am05.controller.server;

import it.polimi.is24am05.client.model.DeckPov;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.side.PlacedSide;
import it.polimi.is24am05.model.card.starterCard.StarterCard;
import it.polimi.is24am05.model.enums.Color;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.enums.state.GameState;
import it.polimi.is24am05.model.enums.state.PlayerState;
import it.polimi.is24am05.model.objective.Objective;

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

    void notifyPlaceStarterSide(PlacedSide[][] playArea);

    void notifyOthersPlaceStarterSide(String nickname, PlacedSide[][] playArea);

    void notifyHandsAndObjectivesDealt(DeckPov resourceDeck, DeckPov goldDeck, List<Objective> objectives, List<Card> hand, List<Objective> playerObjectives, List<Map<String, Object>> players);

    void notifyChooseObjective(Objective objective);

    void notifyAllGameStarted();

    void notifyPlaceSide(PlacedSide[][] playArea, int points);

    void notifyOthersPlaceSide(String nickname, PlacedSide[][] playArea, int points);

    void notifyDrawVisible(boolean isGold, DeckPov deck, List<Card> hand);

    void notifyOthersDrawVisible(String nickname, boolean isGold, DeckPov deck, List<Resource> hand);

    void notifyDrawDeck(boolean isGold, DeckPov deck, List<Card> hand);

    void notifyOthersDrawDeck(String nickname, boolean isGold, DeckPov deck, List<Resource> hand);

    void notifyGameResumed(
            GameState state, int turn, DeckPov resourceDeck, DeckPov goldDeck, List<Objective> objectives,
            PlayerState playerState, int playerTurn, Color color, StarterCard starterCard, List<Objective> playerObjectives, List<Card> hand, PlacedSide[][] playArea, int points,
            List<Map<String, Object>> players
    );

    void notifyOthersGameResumed(String nickname);

    void notifyOthersQuitGame(String nickname);

    void notifyAllGamePaused();

    void notifyException(Exception exception);
}
