package it.polimi.is24am05.client.model;

import it.polimi.is24am05.model.card.side.PlacedSide;
import it.polimi.is24am05.model.card.starterCard.StarterCard;
import it.polimi.is24am05.model.enums.Color;
import it.polimi.is24am05.model.enums.state.GameState;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.enums.state.PlayerState;
import it.polimi.is24am05.model.objective.Objective;

import java.util.ArrayList;
import java.util.List;

/**
 * Local model of the game, restricted to the player's point of view.
 */
public class GamePov {
    /**
     * True if this game is paused, false otherwise.
     */
    boolean isPaused = false;

    /**
     * State of this game.
     */
    GameState state;

    /**
     * Current turn of this game.
     */
    int turn = 0;

    /**
     * Resource deck of this game.
     */
    DeckPov resourceDeck;

    /**
     * Gold deck of this game.
     */
    DeckPov goldDeck;

    /**
     * Objectives of this game.
     */
    List<Objective> objectives;

    /**
     * State of the player.
     */
    PlayerState playerState;

    /**
     * Turn of the player.
     */
    int playerTurn;

    /**
     * Color of the player.
     */
    Color color;

    /**
     * Starter card of the player.
     */
    StarterCard starterCard;

    /**
     * Objectives of the player.
     */
    List<Objective> playerObjectives;

    /**
     * Hand of the player.
     */
    List<Card> hand;

    /**
     * Play area of the player.
     */
    PlacedSide[][] playArea;

    /**
     * Points of the player.
     */
    int points;

    /**
     * List of other players, restricted to the player's point of view.
     */
    List<PlayerPov> playerPovs = new ArrayList<>();

    /**
     * List of disconnected players, identified by their turns.
     */
    List<Integer> disconnected = new ArrayList<>();

    /**
     * Sets if this game is paused.
     * @param paused true if this game is paused, false otherwise.
     */
    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    /**
     * Sets the state.
     * @param state the state.
     */
    public void setState(GameState state) {
        this.state = state;
    }

    /**
     * Sets the turn.
     * @param turn the turn.
     */
    public void setTurn(int turn) {
        this.turn = turn;
    }

    /**
     * Increments the turn to the next connected player.
     */
    public void incrementTurn() {
        do {
            turn += 1;
        } while (disconnected.contains(turn));
    }

    /**
     * Sets the resource deck.
     * @param resourceDeck the resource deck.
     */
    public void setResourceDeck(DeckPov resourceDeck) {
        this.resourceDeck = resourceDeck;
    }

    /**
     * Sets the gold deck.
     * @param goldDeck the gold deck.
     */
    public void setGoldDeck(DeckPov goldDeck) {
        this.goldDeck = goldDeck;
    }

    /**
     * Sets the objectives.
     * @param objectives the objectives.
     */
    public void setObjectives(List<Objective> objectives) {
        this.objectives = objectives;
    }

    /**
     * Sets the state of the player.
     * @param playerState the state of the player.
     */
    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    /**
     * Sets the turn of the player.
     * @param playerTurn the turn of the player.
     */
    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    /**
     * Sets the color of the player.
     * @param color the color of the player.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Sets the starter card of the player.
     * @param starterCard the starter card of the player.
     */
    public void setStarterCard(StarterCard starterCard) {
        this.starterCard = starterCard;
    }

    /**
     * Sets the objectives of the player.
     * @param playerObjectives the objectives of the player.
     */
    public void setPlayerObjectives(List<Objective> playerObjectives) {
        this.playerObjectives = playerObjectives;
    }

    /**
     * Sets the hand of the player.
     * @param hand the hand of the player.
     */
    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    /**
     * Sets the play area of the player.
     * @param playArea the play area of the player.
     */
    public void setPlayArea(PlacedSide[][] playArea) {
        this.playArea = playArea;
    }

    /**
     * Sets the points of the player.
     * @param points the points of the player.
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Adds another player to the list of other players' points of view.
     * @param playerPov the point of view of the player to add.
     */
    public void addPlayerPov(PlayerPov playerPov) {
        playerPovs.add(playerPov);
    }

    /**
     * Adds a disconnected player to the disconnected list.
     * @param nickname the nickname of the disconnected player.
     */
    public void addDisconnected(String nickname) {
        PlayerPov playerPov = playerPovs.stream().filter(p -> p.getNickname().equals(nickname)).findFirst().orElseThrow();
        disconnected.add(playerPov.turn);
    }

    /**
     * Removes a connected player from the disconnected list.
     * @param nickname the nickname of the connected player.
     */
    public void removeDisconnected(String nickname) {
        PlayerPov playerPov = playerPovs.stream().filter(p -> p.getNickname().equals(nickname)).findFirst().orElseThrow();
        disconnected.remove(playerPov.turn);
    }

    /**
     * Gets the point of view of the given player.
     * @param nickname the nickname of the player.
     * @return the points of view of the player.
     */
    public PlayerPov getPlayerPov(String nickname) {
        return playerPovs.stream().filter(p -> p.getNickname().equals(nickname)).findFirst().orElseThrow();
    }
}
