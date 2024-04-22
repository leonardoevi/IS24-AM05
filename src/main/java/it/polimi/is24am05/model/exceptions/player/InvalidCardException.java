package it.polimi.is24am05.model.exceptions.player;


/**
 * Exception thrown when a player wants to play a card that is not in his hand
 */
public class InvalidCardException extends Exception{
    @Override
    public String getMessage() {
        return "Invalid card";
    }
}
