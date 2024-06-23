package it.polimi.is24am05.controller.server.socket;

import java.io.Serializable;
import java.util.Map;

/**
 * Message sent via socket
 * @param title specifies message type
 * @param arguments map of arguments, accessible through their name
 */
public record Message(String title, Map<String, Object> arguments) implements Serializable {}
