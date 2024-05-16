package it.polimi.is24am05.client;

/**
 * Methods to invoke needed to play the game
 */
public interface VirtualServer {
    public void joinServer();
    public void joinGame();
    public void setNumberOfPlayers(int numberOfPlayers);
    public void placeStarterSide(boolean isFront);
    public void chooseObjective(String objectiveId);
    public void placeSide(String cardId, boolean isFront, int i, int j);
    public void drawVisible(String cardId);
    public void drawDeck(boolean isGold);
    public void disconnect();
}
