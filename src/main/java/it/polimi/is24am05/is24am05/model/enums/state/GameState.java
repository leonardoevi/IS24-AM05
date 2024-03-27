package it.polimi.is24am05.is24am05.model.enums.state;

/**
 * States of the game.
 */
public enum GameState {
    /**
     * The game is initialized:
     * 1. Starter cards are dealt and placed;
     * 2. Objectives are dealt and chosen;
     * 3. Hands are dealt.
     */
    START,
    /**
     * The game is played: players place and draw cards according to their state.
     */
    GAME,
    /**
     * The last turn is played as either a player reached 20 points or both decks are empty.
     */
    LAST,
    /**
     * The game ended and winners are returned.
     */
    END;
}
