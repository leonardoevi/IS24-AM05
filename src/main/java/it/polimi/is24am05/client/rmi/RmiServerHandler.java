package it.polimi.is24am05.client.rmi;

import it.polimi.is24am05.client.ServerHandler;
import it.polimi.is24am05.controller.server.rmi.RmiHandlersProvider;
import it.polimi.is24am05.controller.server.rmi.RmiVirtualController;
import it.polimi.is24am05.model.game.Game;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RmiServerHandler extends ServerHandler {
    private RmiVirtualController virtualController;
    private final RmiVirtualClient rmiFromServer;
    private ScheduledExecutorService serverChecker = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) throws Exception {
        try {
            new RmiServerHandler("localhost", "9696");
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

    }

    public RmiServerHandler(String serverIP, String serverPort) throws RemoteException {
        super(serverIP, serverPort);
        rmiFromServer = new RmiFromServer();

        startConnection();
    }


    public void startConnection() throws RemoteException {
        // Try connecting to the server
        try {
            RmiHandlersProvider provider = (RmiHandlersProvider) Naming.lookup("rmi://" + serverIP + ":" + serverPort + "/RmiHandlerProvider");
            virtualController = provider.connect(rmiFromServer);

            // Set up a demon thread to check if server is still up
            serverChecker.scheduleAtFixedRate(new RmiServerChecker(), 0, 1, TimeUnit.SECONDS);

        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            notifyViewServerUnreachable();
            killRmiReaper();
            throw new RemoteException("Rmi connection to server failed");
        }
    }

    private static void killRmiReaper(){
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread t : threadSet) {
            if(t.getName().equals("RMI Reaper")){
                //System.out.println("Killing RMI Reaper");
                t.interrupt();
                }
        }
    }


    @Override
    public void joinServer() {
        try {
            virtualController.joinServerRMI(this.getNickname());
        } catch (RemoteException e) {
            notifyViewServerUnreachable();
            killRmiReaper();
        }
    }

    @Override
    public void joinGame() {
        try {
            virtualController.joinGameRMI();
        } catch (RemoteException e) {
            notifyViewServerUnreachable();
            killRmiReaper();
        }
    }

    @Override
    public void setNumberOfPlayers(int numberOfPlayers) {
        try {
            virtualController.setNumberOfPlayersRMI(numberOfPlayers);
        } catch (RemoteException e) {
            notifyViewServerUnreachable();
            killRmiReaper();
        }
    }

    @Override
    public void placeStarterSide(boolean isFront) {
        try {
            virtualController.placeStarterSideRMI(isFront);
        } catch (RemoteException e) {
            notifyViewServerUnreachable();
            killRmiReaper();
        }
    }

    @Override
    public void chooseObjective(String objectiveId) {
        try {
            virtualController.chooseObjectiveRMI(objectiveId);
        } catch (RemoteException e) {
            notifyViewServerUnreachable();
            killRmiReaper();
        }
    }

    @Override
    public void placeSide(String cardId, boolean isFront, int i, int j) {
        try {
            virtualController.placeSideRMI(cardId, isFront, i, j);
        } catch (RemoteException e) {
            notifyViewServerUnreachable();
            killRmiReaper();
        }
    }

    @Override
    public void drawVisible(String cardId) {
        try {
            virtualController.drawVisibleRMI(cardId);
        } catch (RemoteException e) {
            notifyViewServerUnreachable();
            killRmiReaper();
        }
    }

    @Override
    public void drawDeck(boolean isGold) {
        try {
            virtualController.drawDeckRMI(isGold);
        } catch (RemoteException e) {
            notifyViewServerUnreachable();
            killRmiReaper();
        }
    }

    @Override
    public void disconnect() {
        try {
            virtualController.disconnectRMI();
        } catch (RemoteException e) {
            notifyViewServerUnreachable();
            killRmiReaper();
        }
    }

    class RmiFromServer extends UnicastRemoteObject implements RmiVirtualClient {

        protected RmiFromServer() throws RemoteException {}


        @Override
        public void setGameRMI(Game game) throws RemoteException {
            setGame(game);
        }

        @Override
        public void addLogRMI(String log) throws RemoteException {
            addLog(log);
        }

        @Override
        public void pingRMI() throws RemoteException {
        }
    }

    class RmiServerChecker implements Runnable {
        @Override
        public void run() {
            try {
                virtualController.pongRMI();
            } catch (RemoteException e) {
                serverChecker.shutdown();
                notifyViewServerUnreachable();
                killRmiReaper();
            }
        }
    }
}
