package it.polimi.is24am05.controller.exceptions;

public class KoException extends Exception {
    private String message;

    public KoException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}