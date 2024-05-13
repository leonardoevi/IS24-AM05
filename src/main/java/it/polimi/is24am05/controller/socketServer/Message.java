package it.polimi.is24am05.controller.socketServer;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

/**
 * Template of messages sent and received by sockets.
 * @param title the title of the message as a string.
 * @param arguments the arguments of the message as a map from strings to objects.
 */
public record Message(String title, Map<String, Object> arguments) implements Serializable {

    @Override
    public Map<String, Object> arguments() {
        return new HashMap<>(arguments);
    }
}
