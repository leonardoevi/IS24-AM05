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
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RmiServerHandler extends ServerHandler {
    private RmiVirtualController virtualController;
    private final RmiVirtualClient rmiFromServer;

    protected volatile String lastHeartBeat = "pippo";
    private final ScheduledExecutorService serverChecker = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) {
        try {
            new RmiServerHandler("localhost", "9696", "GUI");
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

    }

    public RmiServerHandler(String serverIP, String serverPort, String viewType) throws RemoteException {
        super(serverIP, serverPort, viewType);
        rmiFromServer = new RmiFromServer();

        startConnection();
    }


    public void startConnection() throws RemoteException {
        // Try connecting to the server
        try {
            RmiHandlersProvider provider = (RmiHandlersProvider) Naming.lookup("rmi://" + serverIP + ":" + serverPort + "/RmiHandlerProvider");
            virtualController = provider.connect(rmiFromServer);

            // Set up a demon thread to check if server is still up
            Thread demon = new Thread(new RmiServerChecker()); demon.setDaemon(true); demon.setName("Rmi connection checker");
            serverChecker.scheduleAtFixedRate(demon, 1, 2, TimeUnit.SECONDS);

        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            notifyViewServerUnreachable();
            killRmiReaper();
            throw new RemoteException("Rmi connection to server failed");
        }
    }

    private static void killRmiReaper(){
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread t : threadSet) {
            if(t.getName().contains("RMI")){
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
        }
        serverChecker.shutdownNow();
        killRmiReaper();
        notifyViewServerUnreachable();
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
        public void pingRMI(String key) throws RemoteException {
            /*
            if(new Random().nextInt() % 4 == 0) {
                virtualController.pongRMI("UPSIE...");
                return;
            }
             */

            virtualController.pongRMI(key);
        }

        @Override
        public void pongRMI(String key) throws RemoteException {
            //System.out.println("Received: " + key);
            lastHeartBeat = key;
        }
    }

    class RmiServerChecker implements Runnable {
        @Override
        public void run() {
            // Generate a random string
            String heartBeat = UUID.randomUUID().toString();

            //System.out.println("Sending: " + heartBeat);

            new Thread(() -> {
                try {
                    virtualController.pingRMI(heartBeat);
                } catch (RemoteException ignored) {}
            }).start();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}

            // Check if client responded to the ping message
            if(!heartBeat.equals(lastHeartBeat)) {
                new Thread(() -> {
                    notifyViewServerUnreachable();
                    serverChecker.shutdownNow();
                    killRmiReaper();
                }).start();
            }
        }
    }
}
