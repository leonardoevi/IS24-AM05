package it.polimi.is24am05.controller.exceptions;

import java.util.List;

public class NicknameAlreadyUsedException extends Exception {
    final private List<String> usedNicknames;

    public NicknameAlreadyUsedException(List<String> usedNicknames) {
        this.usedNicknames = usedNicknames;
    }

    public List<String> getUsedNicknames() {
        return usedNicknames;
    }

    @Override
    public String getMessage() {
        return "Nickname already used";
    }
}
