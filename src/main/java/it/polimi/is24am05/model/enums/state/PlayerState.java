package it.polimi.is24am05.model.enums.state;

/**
 * States of the player's turn.
 */
public enum PlayerState {
    /**
     * Player must place the Starter Card
     */
    PLACE_STARTER_CARD,
    /**
     * Player must choose an objective
     */
    CHOOSE_OBJECTIVE,
    /**
     * Player must draw a card.
     */
    DRAW,
    /**
     * Player must place a card.
     */
    PLACE;
}
