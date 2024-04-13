package it.polimi.is24am05.model.exceptions.playArea;

/**
 * Generic exception thrown when the placement of a card is not allowed for some reason. For example:
 * - the first card placed is not a Starting Card
 * - the specified coordinates are blocked
 */
public class PlacementNotAllowedException extends Exception {
}
