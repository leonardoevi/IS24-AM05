package it.polimi.is24am05.client.view;

import it.polimi.is24am05.client.ServerHandler;
import it.polimi.is24am05.client.model.ClientModel;

/**
 * Class that allows the visualization of the game state
 */
public abstract class View implements Observer{
    protected final ClientModel clientModel;
    protected final ServerHandler server;

    protected View(ClientModel clientModel, ServerHandler server) {
        this.clientModel = clientModel;
        this.server = server;
        clientModel.addObserver(this);
    }

    /**
     * Kills all the processes when the connection to the server is no longer available
     */
    public abstract void serverUnreachable();
}
