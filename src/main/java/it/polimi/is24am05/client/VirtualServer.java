package it.polimi.is24am05.client;

/**
 * Methods to invoke needed to play the game
 */
public interface VirtualServer {
    /**
     * Allows the client to join the server with the nickname that was previously set.
     */
    public void joinServer();

    /**
     * Allows the client to join a game
     */
    public void joinGame();

    /**
     * Allows a client to start a new game and join the new game, with the specified number of players
     * @param numberOfPlayers
     */
    public void setNumberOfPlayers(int numberOfPlayers);


    public void placeStarterSide(boolean isFront);
    public void chooseObjective(String objectiveId);
    public void placeSide(String cardId, boolean isFront, int i, int j);
    public void drawVisible(String cardId);
    public void drawDeck(boolean isGold);
    public void disconnect();

    public void sendMessage(String message);
    public void sendDirectMessage(String message, String recipient);
}
