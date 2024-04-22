package it.polimi.is24am05.model.exceptions.player;


/**
 * Exception thrown when a player wants to choose an objective card not contained in his objectives hand
 */
public class ObjectiveNotAllowedException extends Exception {
    @Override
    public String getMessage() {
        return "Objective not possessed";
    }
}
