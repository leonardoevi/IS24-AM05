package it.polimi.is24am05.model.exceptions.card;

/**
 * Thrown by getCorner if a corner is not present on the side, i.e. hasCorner returns false.
 */
public class InvalidCornerException extends Exception {
    public InvalidCornerException() {}
}
