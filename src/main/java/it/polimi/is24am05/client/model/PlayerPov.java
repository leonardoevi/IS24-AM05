package it.polimi.is24am05.client.model;

import it.polimi.is24am05.model.card.side.PlacedSide;
import it.polimi.is24am05.model.card.starterCard.StarterCard;
import it.polimi.is24am05.model.enums.Color;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.enums.state.PlayerState;

import java.util.List;

/**
 * Local model of another player, restricted to the player's point of view.
 */
public class PlayerPov {
    /**
     * Nickname of this player.
     */
    final String nickname;

    /**
     * State of this player.
     */
    PlayerState state;

    /**
     * Turn of this player.
     */
    final int turn;

    /**
     * Color of this player.
     */
    final Color color;

    /**
     * Starter card of this player.
     */
    final StarterCard starterCard;

    /**
     * Hand of this player.
     */
    List<Resource> hand;

    /**
     * Play area of this player.
     */
    PlacedSide[][] playArea;

    /**
     * Points of this player.
     */
    int points;

    /**
     * Constructor for a new game.
     * @param nickname the nickname.
     * @param turn the turn.
     * @param color the color.
     * @param starterCard the starter card.
     */
    public PlayerPov(String nickname, int turn, Color color, StarterCard starterCard) {
        this.nickname = nickname;
        this.state = PlayerState.PLACE_STARTER_CARD;
        this.turn = turn;
        this.color = color;
        this.starterCard = starterCard;
    }

    /**
     * Constructor for a resumed game.
     * @param nickname the nickname.
     * @param state the state.
     * @param turn the turn.
     * @param color the color.
     * @param starterCard the starter card.
     * @param hand the hand.
     * @param playArea the play area.
     * @param points the points.
     */
    public PlayerPov(String nickname, PlayerState state, int turn, Color color, StarterCard starterCard, List<Resource> hand, PlacedSide[][] playArea, int points) {
        this.nickname = nickname;
        this.state = state;
        this.turn = turn;
        this.color = color;
        this.starterCard = starterCard;
        this.hand = hand;
        this.playArea = playArea;
        this.points = points;
    }

    /**
     * Sets the state of this player.
     * @param state the state to set.
     */
    public void setState(PlayerState state) {
        this.state = state;
    }

    /**
     * Sets the hand of this player.
     * @param hand the hand to set.
     */
    public void setHand(List<Resource> hand) {
        this.hand = hand;
    }

    /**
     * Sets the play area of this player.
     * @param playArea the play area to set.
     */
    public void setPlayArea(PlacedSide[][] playArea) {
        this.playArea = playArea;
    }

    /**
     * Sets the points of this player.
     * @param points the points to set.
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Gets the nickname of this player.
     * @return the nickname.
     */
    public String getNickname() {
        return nickname;
    }
}
