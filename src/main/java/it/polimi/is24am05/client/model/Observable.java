package it.polimi.is24am05.client.model;

import it.polimi.is24am05.client.view.Observer;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObserversGame();
    void notifyObserversLogs();
}
