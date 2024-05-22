package it.polimi.is24am05.client;

/**
 * Lists methods needed to play the game. Implemented by serverHandler.
 */
public interface VirtualServer {
    /**
     * Joins the server with the previously set nickname.
     */
    void joinServer();

    /**
     * Joins a game.
     */
    void joinGame();

    /**
     * Creates a new game with the given number of players and joins it.
     * @param numberOfPlayers the number of the players of the game.
     */
    void setNumberOfPlayers(int numberOfPlayers);


    /**
     * Places the starter card on the given side.
     * @param isFront true for front side, false for back side.
     */
    void placeStarterSide(boolean isFront);


    /**
     * Chooses the given objective.
     * @param objectiveId the chosen objective.
     */
    void chooseObjective(String objectiveId);


    /**
     * Places the given card on the given side and the given coordinates.
     * @param cardId the card to be placed.
     * @param isFront true for front side, false for back side.
     * @param i the row at which to place the side.
     * @param j the column at which to place the side.
     */
    void placeSide(String cardId, boolean isFront, int i, int j);


    /**
     * Draws the given visible card.
     * @param cardId the visible card to be drawn.
     */
    void drawVisible(String cardId);

    /**
     * Draws a card from the given deck.
     * @param isGold true for gold deck, false for resource deck.
     */
    void drawDeck(boolean isGold);

    /**
     * Disconnects from the server.
     */
    void quitServer();
}
