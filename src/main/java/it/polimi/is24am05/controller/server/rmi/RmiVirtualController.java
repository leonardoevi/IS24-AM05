package it.polimi.is24am05.controller.server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface visible from the RMI client java VM, allows the client to play the game
 */
public interface RmiVirtualController extends Remote {
    public void joinServerRMI(String nickname) throws RemoteException;
    public void leaveServerRMI() throws RemoteException;
    public void joinGameRMI() throws RemoteException;
    public void setNumberOfPlayersRMI(int numberOfPlayers) throws RemoteException;
    public void placeStarterSideRMI(boolean isFront) throws RemoteException;
    public void chooseObjectiveRMI(String objectiveId) throws RemoteException;
    public void placeSideRMI(String cardId, boolean isFront, int i, int j) throws RemoteException;
    public void drawVisibleRMI(String cardId) throws RemoteException;
    public void drawDeckRMI(boolean isGold) throws RemoteException;
    public void disconnectRMI() throws RemoteException;

    public void pingRMI(String key) throws RemoteException;
    public void pongRMI(String key) throws RemoteException;
}
