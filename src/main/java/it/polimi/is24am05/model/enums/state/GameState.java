package it.polimi.is24am05.model.enums.state;

/**
 * States of the game.
 */
public enum GameState {
    /**
     * The game is initialized:
     * 1. Starter cards are dealt and need to be placed;
     */
    PLACE_STARTER_CARDS,
    /**
     * 2. Objectives are dealt and chosen;
     */
    CHOOSE_OBJECTIVE,
    /**
     * The game is played: players place and draw cards according to their state.
     */
    GAME,
    /**
     * The game is about to end, last few turns are being played.
     */
    GAME_ENDING,
    /**
     * The game ended and winners are returned.
     */
    END,

    /**
     * The game is paused because there are too few players
     */
    PAUSE;


}
