package it.polimi.is24am05.client.model;

import it.polimi.is24am05.client.view.Observer;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);

    /**
     * Notify all observer the game was modified
     */
    void notifyObserversGame();

    /**
     * Notify observer a log was received
     */
    void notifyObserversLogs();
}
