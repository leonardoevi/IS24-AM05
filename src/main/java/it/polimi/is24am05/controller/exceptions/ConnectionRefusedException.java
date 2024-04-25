package it.polimi.is24am05.controller.exceptions;

public class ConnectionRefusedException extends Exception{
    public String reason;

    public ConnectionRefusedException(String reason) {
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return reason;
    }
}
