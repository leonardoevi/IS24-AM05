package it.polimi.is24am05.model.exceptions.player;

/**
 * Exception thrown when a player wants to play a side that is not associated with the card he wants to play
 */
public class InvalidSideException extends Exception{
    @Override
    public String getMessage() {
        return "Invalid side";
    }
}
