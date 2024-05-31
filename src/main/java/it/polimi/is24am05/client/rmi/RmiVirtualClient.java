package it.polimi.is24am05.client.rmi;

import it.polimi.is24am05.model.game.Game;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface visible from the RMI server java VM, allows the server to notify the client
 */
public interface RmiVirtualClient extends Remote {
    public void setGameRMI(Game game) throws RemoteException;
    public void addLogRMI(String log) throws RemoteException;

    // Allow the server to ping the client
    public void pingRMI(String key) throws RemoteException;
    public void pongRMI(String key) throws RemoteException;
}
