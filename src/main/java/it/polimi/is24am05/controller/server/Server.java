package it.polimi.is24am05.controller.server;

import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.controller.server.rmi.RmiHandlersProvider;
import it.polimi.is24am05.controller.server.rmi.RmiHandlersProviderImp;
import it.polimi.is24am05.controller.server.socket.SocketClientHandler;
import it.polimi.is24am05.model.exceptions.game.NoSuchPlayerException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Multiplexer to all connected clients
 */
public class Server {
    private final List<ClientHandler> clientHandlers;
    private final Controller controller;

    public Server(Controller controller) {
        this.clientHandlers = new ArrayList<>();
        this.controller = controller;
    }

    /**
     * Start the server and start accepting Socket and RMI connections
     * @throws RemoteException if something goes wrong during any of the startup operations
     */
    public void start() throws RemoteException {
        new Thread(this::startSocket).start();
        startRMI();
    }

    private void startSocket() {
        ServerSocket serverSocket;
        // Define a fixed pool of threads to handle clients connections
        final ExecutorService threadPool = Executors.newFixedThreadPool(8);
        // Create the server socket to accept clients connections
        try {
            serverSocket = new ServerSocket(6969);
        } catch (IOException e) {
            System.out.println("Failed to start socket server");
            return;
        }

        System.out.println("Socket Server ready on port: " + serverSocket.getLocalPort());

        // Keep accepting connections
        while (true) {
            try {
                final Socket socket = serverSocket.accept();
                System.out.println("New socket connection!");
                // Let the thread pool handle the communication with the client
                threadPool.submit(new SocketClientHandler(controller,this, socket));
            } catch (IOException ignored) {
                System.out.println("ERRORE ");
                break;
            }
        }

        System.out.println("Shutting down thread pool");
        threadPool.shutdown();
    }

    private void startRMI() throws RemoteException {
        RmiHandlersProvider handlersProvider = new RmiHandlersProviderImp(controller, this);
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(9696);
            registry.bind("RmiHandlerProvider", handlersProvider);
            System.out.println("RMI Server bound and ready on port: 9696");
        } catch (RemoteException | AlreadyBoundException e) {
            System.out.println("Failed to start RMI server");
        }
    }

    /**
     * @return the nicknames of the connected clients
     */
    public synchronized List<String> getNicknames(){
        return clientHandlers.stream().map(ClientHandler::getNickname).toList();
    }

    /**
     * Add a client to the list of connected clients
     * @param clientHandler to add
     */
    public synchronized void subscribe(ClientHandler clientHandler) {
        System.out.println(clientHandler.getNickname() + " subscribed");
        clientHandlers.add(clientHandler);
    }

    /**
     * Removes a client from the list of connected clients
     * @param clientHandler to remove
     */
    public synchronized void unsubscribe(ClientHandler clientHandler) {
        if(clientHandlers.remove(clientHandler))
            System.out.println(clientHandler.getNickname() + " unsubscribed");
    }

    /**
     * @param clientHandler to check
     * @return true if the specified client is in the list of connected clients
     */
    public synchronized boolean isSubscribed(ClientHandler clientHandler) {
        return clientHandlers.contains(clientHandler);
    }

    /**
     * @param nickname to find
     * @return the client with the specified nickname
     * @throws NoSuchPlayerException if no client with the specified nickname is in the list
     */
    private synchronized ClientHandler getClientHandler(String nickname) throws NoSuchPlayerException {
        return clientHandlers.stream().filter(h -> h.getNickname().equals(nickname)).findFirst()
                .orElseThrow(NoSuchPlayerException::new);
    }

    /**
     * Update the players local model
     * @param nicknames players nicknames to which an update will be sent
     */
    public void broadcastGameUpdated(List<String> nicknames) {
        for (String nickname : nicknames) {
            try{
                getClientHandler(nickname).setGame(controller.game.getPov(nickname));
            } catch (NoSuchPlayerException e) {
                //System.out.println("Player " + nickname + " not found, unable to update game");
            }
        }
    }

    /**
     * Update the game model of the client with the specified nickname
     * @param nickname to update
     */
    public void gameUpdated(String nickname) {
        try{
            getClientHandler(nickname).setGame(controller.game.getPov(nickname));
        } catch (NoSuchPlayerException e) {
            //System.out.println("Player " + nickname + " not found, unable to update game");
        }
    }

    /**
     * Send a Log message to players
     * @param nicknames players nicknames to which an update will be sent
     */
    public void broadcastLog(List<String> nicknames, String log){
        for (String nickname : nicknames) {
            try{
                getClientHandler(nickname).addLog(log);
            } catch (NoSuchPlayerException e) {
                //System.out.println("Player " + nickname + " not found, unable to send log");
            }
        }
    }
}
