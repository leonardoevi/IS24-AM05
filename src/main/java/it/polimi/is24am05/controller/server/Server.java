package it.polimi.is24am05.controller.server;

import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.controller.server.rmi.RmiHandlersProvider;
import it.polimi.is24am05.controller.server.rmi.RmiHandlersProviderImp;
import it.polimi.is24am05.controller.server.socket.SocketClientHandler;
import it.polimi.is24am05.model.exceptions.game.NoSuchPlayerException;
import it.polimi.is24am05.model.game.Game;

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

public class Server {
    private final List<ClientHandler> clientHandlers;
    private final Controller controller;

    public Server(Controller controller) {
        this.clientHandlers = new ArrayList<>();
        this.controller = controller;
    }

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
            serverSocket = new ServerSocket(9999);
        } catch (IOException e) {
            System.out.println("Failed to start socket server");
            return;
        }

        System.out.println("Socket Server ready on port: " + serverSocket.getLocalPort());

        // Keep accepting connections
        while (true) {
            try {
                final Socket socket = serverSocket.accept();
                System.out.println("New connection!");
                // Let the thread pool handle the communication with the client
                threadPool.submit(new SocketClientHandler(controller,this, socket));
            } catch (IOException ignored) {
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

    public synchronized List<String> getNicknames(){
        return clientHandlers.stream().map(ClientHandler::getNickname).toList();
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        clientHandlers.add(clientHandler);
    }

    public synchronized void unsubscribe(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }

    private synchronized ClientHandler getClientHandler(String nickname) throws NoSuchPlayerException {
        return clientHandlers.stream().filter(h -> h.getNickname().equals(nickname)).findFirst()
                .orElseThrow(NoSuchPlayerException::new);
    }

    /**
     * Update the players local model
     * @param nicknames players nicknames to which an update will be sent
     */
    public void broadcastGameUpdated(List<String> nicknames) {
        Game toSend = controller.game;
        for (String nickname : nicknames) {
            try{
                getClientHandler(nickname).setGame(toSend);
            } catch (NoSuchPlayerException e) {
                System.out.println("Player " + nickname + " not found, unable to update game");
            }
        }
    }

    public void gameUpdated(String nickname) {
        try{
            getClientHandler(nickname).setGame(controller.game);
        } catch (NoSuchPlayerException e) {
            System.out.println("Player " + nickname + " not found, unable to update game");
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
                System.out.println("Player " + nickname + " not found, unable to send log");
            }
        }
    }
}
