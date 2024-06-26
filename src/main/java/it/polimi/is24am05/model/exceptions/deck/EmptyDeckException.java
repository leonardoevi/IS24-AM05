package it.polimi.is24am05.model.exceptions.deck;

/**
 * Exception thrown when trying to draw from a deck when there are no more covered cards, regardless of the visible cards.
 */
public class EmptyDeckException extends Exception{
    @Override
    public String getMessage() {
        return "Deck is empty";
    }
}
