package it.polimi.is24am05.controller.exceptions;

/**
 * Thrown when a user is the first to connect to a new game, without specifying the numUserParameter
 */
public class FirstConnectionException extends Exception{
    public String reason;

    public FirstConnectionException(String reason) {
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return reason;
    }
}
