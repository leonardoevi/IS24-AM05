package it.polimi.is24am05.client.view;

import it.polimi.is24am05.client.ServerHandler;
import it.polimi.is24am05.client.model.GamePov;

/**
 * Abstracts the TUI and GUI.
 */
public abstract class View {
    /**
     * Server handler.
     */
    ServerHandler serverHandler;

    /**
     * Local model of the game.
     */
    GamePov gamePov;

    /**
     * Constructor.
     * @param serverHandler the server handler.
     * @param gamePov the local model of the game.
     */
    public View(ServerHandler serverHandler, GamePov gamePov) {
        this.serverHandler = serverHandler;
        this.gamePov = gamePov;
    }

    /**
     * Updates the view after joining the server.
     */
    public abstract void joinedServer();

    /**
     * Updates the view after adding a log.
     */
    public abstract void addedLog();

    /**
     * Updates the view after creating the game.
     */
    public abstract void gameCreated();

    /**
     * Updates the view after placing the starter side.
     */
    public abstract void placedStarterSide();

    /**
     * Updates the view after another player placed the starter side.
     */
    public abstract void otherPlacedStarterSide();

    /**
     * Updates the view after dealing the hands and objectives.
     */
    public abstract void handsAndObjectivesDealt();

    /**
     * Updates the view after choosing the objective.
     */
    public abstract void chosenObjective();

    /**
     * Updates the view after starting the game.
     */
    public abstract void gameStarted();

    /**
     * Updates the view after placing a side.
     */
    public abstract void placedSide();

    /**
     * Updates the view after another player placed a side.
     */
    public abstract void otherPlacedSide();

    /**
     * Updates the view after drawing a visible card.
     */
    public abstract void drawnVisible();

    /**
     * Updates the view after another player draws a visible card.
     */
    public abstract void otherDrawnVisible();

    /**
     * Updates the view after drawing from a deck.
     */
    public abstract void drawnDeck();

    /**
     * Updates the view after another player draws from a deck.
     */
    public abstract void otherDrawnDeck();

    /**
     * Updates the view after resuming the game.
     */
    public abstract void gameResumed();

    /**
     * Updates the view after another player reconnected to the game.
     */
    public abstract void otherGameResumed();

    /**
     * Updates the view after another player quit the game.
     */
    public abstract void otherQuitGame();

    /**
     * Updates the view after pausing the game.
     */
    public abstract void gamePaused();
}
