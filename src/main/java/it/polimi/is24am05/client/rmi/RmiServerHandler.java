package it.polimi.is24am05.client.rmi;

import it.polimi.is24am05.client.ServerHandler;
import it.polimi.is24am05.controller.server.rmi.RmiHandlersProvider;
import it.polimi.is24am05.controller.server.rmi.RmiVirtualController;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiServerHandler extends ServerHandler {
    private RmiVirtualController virtualController;
    private final RmiVirtualClient rmiFromServer;

    public RmiServerHandler(String serverIP, String serverPort) throws RemoteException {
        super(serverIP, serverPort, null); // TODO: pass view
        rmiFromServer = new RmiFromServer();

        startConnection();
    }


    public void startConnection() {
        // Try connecting to the server
        try {
            RmiHandlersProvider provider = (RmiHandlersProvider) Naming.lookup("rmi://" + serverIP + ":" + serverPort + "/RmiHandlerProvider");
            virtualController = provider.connect(rmiFromServer);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void joinServer() {

    }

    @Override
    public void joinGame() {

    }

    @Override
    public void setNumberOfPlayers(int numberOfPlayers) {

    }

    @Override
    public void placeStarterSide(boolean isFront) {

    }

    @Override
    public void chooseObjective(String objectiveId) {

    }

    @Override
    public void placeSide(String cardId, boolean isFront, int i, int j) {

    }

    @Override
    public void drawVisible(String cardId) {

    }

    @Override
    public void drawDeck(boolean isGold) {

    }

    @Override
    public void quitServer() {

    }

    /*
    @Override
    public void joinServer() {
        try {
            virtualController.joinServerRMI(this.getNickname());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void joinGame() {
        try {
            virtualController.joinGameRMI();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setNumberOfPlayers(int numberOfPlayers) {
        try {
            virtualController.setNumberOfPlayersRMI(numberOfPlayers);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeStarterSide(boolean isFront) {
        try {
            virtualController.placeStarterSideRMI(isFront);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void chooseObjective(String objectiveId) {
        try {
            virtualController.chooseObjectiveRMI(objectiveId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeSide(String cardId, boolean isFront, int i, int j) {
        try {
            virtualController.placeSideRMI(cardId, isFront, i, j);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawVisible(String cardId) {
        try {
            virtualController.drawVisibleRMI(cardId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawDeck(boolean isGold) {
        try {
            virtualController.drawDeckRMI(isGold);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void disconnect() {
        try {
            virtualController.disconnectRMI();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
     */

    class RmiFromServer extends UnicastRemoteObject implements RmiVirtualClient {

        protected RmiFromServer() throws RemoteException {}

        @Override
        public void printMessageRMI(String message) throws RemoteException {
            printText(message);
        }
    }
}
