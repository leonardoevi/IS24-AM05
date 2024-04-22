package it.polimi.is24am05.model.exceptions.game;

public class NotYourTurnException extends Exception{
    @Override
    public String getMessage() {
        return "Not your turn!";
    }
}
