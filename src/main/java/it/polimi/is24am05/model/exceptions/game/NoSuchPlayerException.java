package it.polimi.is24am05.model.exceptions.game;

public class NoSuchPlayerException extends Exception {
    @Override
    public String getMessage() {
        return "No such player";
    }
}
