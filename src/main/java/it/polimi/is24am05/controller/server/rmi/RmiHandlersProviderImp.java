package it.polimi.is24am05.controller.server.rmi;

import it.polimi.is24am05.client.rmi.RmiVirtualClient;
import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.controller.server.Server;

import java.rmi.RemoteException;

public class RmiHandlersProviderImp implements RmiHandlersProvider {
    private final Controller controller;
    private final Server server;

    public RmiHandlersProviderImp(Controller controller, Server server) {
        this.controller = controller;
        this.server = server;
    }

    @Override
    public RmiVirtualController connect(RmiVirtualClient virtualClient) throws RemoteException {
        return new RmiClientHandler(controller, server, virtualClient);
    }
}
