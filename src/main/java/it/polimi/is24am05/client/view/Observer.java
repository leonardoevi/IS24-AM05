package it.polimi.is24am05.client.view;

public interface Observer {
    /**
     * The game is updated
     */
    void updateGame();

    /**
     * A new log form the server was received
     */
    void updateLogs();
}
