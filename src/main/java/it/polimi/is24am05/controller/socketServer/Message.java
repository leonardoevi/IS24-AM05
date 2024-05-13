package it.polimi.is24am05.controller.socketServer;

import java.util.Map;
import java.util.HashMap;

public record Message(String title, Map<String, Object> arguments) {

    @Override
    public Map<String, Object> arguments() {
        return new HashMap<>(arguments);
    }
}
