package it.polimi.is24am05.client.view;

import it.polimi.is24am05.client.ServerHandler;
import it.polimi.is24am05.client.model.GamePov;

/**
 * Text user interface.
 */
public class TUI extends View {

    /**
     * Constructor.
     * @param serverHandler the server handler.
     * @param gamePov the local model of the game.
     */
    public TUI(ServerHandler serverHandler, GamePov gamePov) {
        super(serverHandler, gamePov);
    }

    /**
     * Prints the view after joining the server.
     */
    @Override
    public void joinedServer() {
        // TODO
    }

    /**
     * Prints the view after adding a log.
     */
    @Override
    public void addedLog() {
        // TODO
    }

    /**
     * Prints the view after creating the game.
     */
    @Override
    public void gameCreated() {
        // TODO
    }

    /**
     * Prints the view after placing the starter side.
     */
    @Override
    public void placedStarterSide() {
        // TODO
    }

    /**
     * Prints the view after another player placed the starter side.
     */
    @Override
    public void otherPlacedStarterSide() {
        // TODO
    }

    /**
     * Prints the view after dealing the hands and objectives.
     */
    @Override
    public void handsAndObjectivesDealt() {
        // TODO
    }

    /**
     * Prints the view after choosing the objective.
     */
    @Override
    public void chosenObjective() {
        // TODO
    }

    /**
     * Prints the view after starting the game.
     */
    @Override
    public void gameStarted() {
        // TODO
    }

    /**
     * Prints the view after placing a side.
     */
    @Override
    public void placedSide() {

    }

    /**
     * Prints the view after another player placed a side.
     */
    @Override
    public void otherPlacedSide() {
        // TODO
    }

    /**
     * Prints the view after drawing a visible card.
     */
    @Override
    public void drawnVisible() {
        // TODO
    }

    /**
     * Prints the view after another player draws a visible card.
     */
    @Override
    public void otherDrawnVisible() {
        // TODO
    }

    /**
     * Prints the view after drawing from a deck.
     */
    @Override
    public void drawnDeck() {
        // TODO
    }

    /**
     * Prints the view after another player draws from a deck.
     */
    @Override
    public void otherDrawnDeck() {
        // TODO
    }

    /**
     * Prints the view after resuming the game.
     */
    @Override
    public void gameResumed() {
        // TODO
    }

    /**
     * Prints the view after another player reconnected to the game.
     */
    @Override
    public void otherGameResumed() {
        // TODO
    }

    /**
     * Prints the view after another player quit the game.
     */
    @Override
    public void otherQuitGame() {
        // TODO
    }

    /**
     * Prints the view after pausing the game.
     */
    @Override
    public void gamePaused() {
        // TODO
    }
}
