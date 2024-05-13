package it.polimi.is24am05.controller.exceptions;

public class InvalidNumUsersException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid number of users";
    }
}
