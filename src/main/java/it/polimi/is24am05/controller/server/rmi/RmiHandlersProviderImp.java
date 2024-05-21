package it.polimi.is24am05.controller.server.rmi;

import it.polimi.is24am05.client.rmi.RmiVirtualClient;
import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.controller.server.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiHandlersProviderImp extends UnicastRemoteObject implements RmiHandlersProvider {
    private final Controller controller;
    private final Server server;

    public RmiHandlersProviderImp(Controller controller, Server server) throws RemoteException {
        this.controller = controller;
        this.server = server;
    }

    @Override
    public RmiVirtualController connect(RmiVirtualClient virtualClient) throws RemoteException {
        System.out.println("New rmi connection!");
        return new RmiClientHandler(controller, server, virtualClient).getRmiVirtualController();
    }
}
