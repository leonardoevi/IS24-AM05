package it.polimi.is24am05.client.model;

import it.polimi.is24am05.model.game.Game;
import it.polimi.is24am05.client.view.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientModel implements Observable {
    private Game game;
    private final List<String> log = new ArrayList<>();

    private final List<Observer> observers = new ArrayList<>();

    public void setGame(Game game) {
        this.game = game;
        notifyObserversGame();
    }

    public Optional<Game> getGame() {
        if (game == null)
            return Optional.empty();
        else
            return Optional.of(game);
    }

    public List<String> getLog() {
        return new ArrayList<>(log);
    }

    public void addLog(String log) {
        this.log.add(log);
        notifyObserversLogs();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserversGame() {
        for (Observer observer : observers) {
            observer.updateGame();
        }
    }

    public void notifyObserversLogs() {
        for (Observer observer : observers) {
            observer.updateLogs();
        }
    }
}
