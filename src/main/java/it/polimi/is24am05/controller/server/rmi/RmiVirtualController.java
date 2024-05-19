package it.polimi.is24am05.controller.server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface visible from the RMI client java VM, allows the client to play the game
 */
public interface RmiVirtualController extends Remote {
    void joinServerRMI(String nickname) throws RemoteException;
    void joinGameRMI() throws RemoteException;
    void setNumberOfPlayersRMI(int numberOfPlayers) throws RemoteException;
    void placeStarterSideRMI(boolean isFront) throws RemoteException;
    void chooseObjectiveRMI(String objectiveId) throws RemoteException;
    void placeSideRMI(String cardId, boolean isFront, int i, int j) throws RemoteException;
    void drawVisibleRMI(String cardId) throws RemoteException;
    void drawDeckRMI(boolean isGold) throws RemoteException;
    void disconnectRMI() throws RemoteException;

    // to keep connection alive
    void pong(String key) throws RemoteException;
}
