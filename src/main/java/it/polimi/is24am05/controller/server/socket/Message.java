package it.polimi.is24am05.controller.server.socket;

import java.util.Map;

public record Message(String title, Map<String, Object> arguments) {}
