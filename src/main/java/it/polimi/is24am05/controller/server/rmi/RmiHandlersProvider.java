package it.polimi.is24am05.controller.server.rmi;

import it.polimi.is24am05.client.rmi.RmiVirtualClient;
import it.polimi.is24am05.controller.server.VirtualClient;

import java.rmi.*;

/**
 * Provides a client with a controller for the game.
 * The controller given to the clients allows them to invoke the methods needed to play.
 */
public interface RmiHandlersProvider extends Remote {

    /**
     * @return an object that implements the methods needed to play the game
     * @throws RemoteException if something goes wrong during RMI connection or method invocation
     */
    public RmiVirtualController connect(RmiVirtualClient virtualClient) throws RemoteException;
}
