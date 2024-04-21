package it.polimi.is24am05.controller;

public enum LobbyState {
    /**
     * The lobby is loading a saved game, waiting for all players to reconnect
     */
    OLD,

    /**
     * A new game is created, waiting for all players to connect
     */
    NEW,

    /**
     * The game has started
     */
    STARTED
}
