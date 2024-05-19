package it.polimi.is24am05.client.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface visible from the RMI server java VM, allows the server to notify the client
 */
public interface RmiVirtualClient extends Remote {
    public void printMessageRMI(String message) throws RemoteException;
}
