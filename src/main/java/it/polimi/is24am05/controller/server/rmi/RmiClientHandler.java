package it.polimi.is24am05.controller.server.rmi;

import it.polimi.is24am05.client.rmi.RmiVirtualClient;
import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.controller.server.ClientHandler;
import it.polimi.is24am05.controller.server.Server;
import it.polimi.is24am05.model.game.Game;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
import java.util.concurrent.*;

public class RmiClientHandler extends ClientHandler {
    private final RmiVirtualClient virtualClient;

    private final RmiVirtualController rmiFromClient;

    // Single thread that runs RMI methods
    private final ExecutorService rmiExecutor = Executors.newSingleThreadExecutor(new DeamonThreadFactory());

    // variables needed for connection check
    protected volatile String lastHeartBeat = "Manuel";
    private final ScheduledExecutorService connectionDemon = Executors.newSingleThreadScheduledExecutor();

    public RmiClientHandler(Controller controller, Server server, RmiVirtualClient virtualClient) throws RemoteException {
        super(controller, server);
        this.virtualClient = virtualClient;
        this.rmiFromClient = new RmiFromClient();

        connectionDemon.scheduleAtFixedRate(new ConnectionCheckRoutine(), 1, 2, TimeUnit.SECONDS);
    }

    // This getter is needed to give the Client an object that implements the RmiVirtualController interface
    public RmiVirtualController getRmiVirtualController() {
        return rmiFromClient;
    }

    // Methods invoked by the Server, needed to notify the client of some events
    // FROM SERVER TO CLIENT


    @Override
    public void setGame(Game game) {
        rmiExecutor.submit(() -> {
            try {
                virtualClient.setGameRMI(game);
            } catch (RemoteException ignored) {}
        });
    }

    @Override
    public void addLog(String log) {
        rmiExecutor.submit(() -> {
            try {
                virtualClient.addLogRMI(log);
            } catch (RemoteException ignored) {}
        });
    }

    /**
     * This class is needed to create a Demon thread to submit to the executor service.
     * Having a demon thread execute RMI requests is necessary to avoid waiting for long timeouts in case the
     * network fails
     */
    public static class DeamonThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }
    }

    // this class implements RMI methods, from the RmiVirtualController interface, invoked by the client
    // simply propagates RMI calls to the super class ClientHandler
    class RmiFromClient extends UnicastRemoteObject implements RmiVirtualController {

        protected RmiFromClient() throws RemoteException {
        }

        // FROM CLIENT TO SERVER

        @Override
        public void joinServerRMI(String nickname) throws RemoteException {
            joinServer(nickname);
        }

        @Override
        public void joinGameRMI() throws RemoteException {
            joinGame();
        }

        @Override
        public void setNumberOfPlayersRMI(int numberOfPlayers) throws RemoteException {
            setNumberOfPlayers(numberOfPlayers);
        }

        @Override
        public void placeStarterSideRMI(boolean isFront) throws RemoteException {
            placeStarterSide(isFront);
        }

        @Override
        public void chooseObjectiveRMI(String objectiveId) throws RemoteException {
            chooseObjective(objectiveId);
        }

        @Override
        public void placeSideRMI(String cardId, boolean isFront, int i, int j) throws RemoteException {
            placeSide(cardId, isFront, i, j);
        }

        @Override
        public void drawVisibleRMI(String cardId) throws RemoteException {
            drawVisible(cardId);
        }

        @Override
        public void drawDeckRMI(boolean isGold) throws RemoteException {
            drawDeck(isGold);
        }

        @Override
        public void disconnectRMI() throws RemoteException {
            disconnect();
        }

        @Override
        public void pingRMI(String key) throws RemoteException {
            virtualClient.pongRMI(key);
        }

        @Override
        public void pongRMI(String key) throws RemoteException {
            lastHeartBeat = key;
            //System.out.println("Received: " + key);
        }
    }


    private class ConnectionCheckRoutine implements Runnable {
        @Override
        public void run() {
            // Generate a random string
            String heartBeat = UUID.randomUUID().toString();

            //System.out.println("Sending: " + heartBeat);

            Thread pinger = new Thread(() -> {
                try {
                    virtualClient.pingRMI(heartBeat);
                } catch (RemoteException ignored) {}
            });
            pinger.setDaemon(true);
            pinger.start();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}


            // Check if client responded to the ping message
            if (!heartBeat.equals(lastHeartBeat)) {
                disconnect();
                connectionDemon.shutdown();
                rmiExecutor.shutdown();
            }
        }
    }
}


