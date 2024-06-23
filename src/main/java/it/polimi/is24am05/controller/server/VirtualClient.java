package it.polimi.is24am05.controller.server;

import it.polimi.is24am05.model.game.Game;

/**
 * Output from the server to the client
 */
public interface VirtualClient {
    /**
     * Update client game state
     * @param game new game state
     */
    void setGame(Game game);

    /**
     * Send a log message to the client
     * @param log to send
     */
    void addLog(String log);
}
