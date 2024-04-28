package it.polimi.is24am05.model.exceptions.deck;

/**
 * Exception thrown when drawing a visible card, if the specified card is not actually visible
 */
public class InvalidVisibleCardException extends Exception{
    @Override
    public String getMessage() {
        return "Invalid visible card";
    }
}
