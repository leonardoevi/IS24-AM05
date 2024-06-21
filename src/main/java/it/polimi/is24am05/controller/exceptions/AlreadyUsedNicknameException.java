package it.polimi.is24am05.controller.exceptions;

import java.util.List;

public class AlreadyUsedNicknameException extends Exception {
    private final List<String> nicknames;

    public AlreadyUsedNicknameException(List<String> nicknames) {
        this.nicknames = nicknames;
    }

    @Override
    public String getMessage() {
        return String.join(", ", nicknames);
    }
}
