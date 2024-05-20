package it.polimi.is24am05.controller.server;

import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.controller.server.rmi.RmiHandlersProvider;
import it.polimi.is24am05.controller.server.rmi.RmiHandlersProviderImp;
import it.polimi.is24am05.controller.server.socket.SocketClientHandler;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.deck.Deck;
import it.polimi.is24am05.model.exceptions.game.NoSuchPlayerException;
import it.polimi.is24am05.model.game.Game;
import it.polimi.is24am05.model.playArea.PlayArea;

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

    public void notifyJoinServer(String client) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.addLog(client + " joined the server");
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyJoinGame(String client, List<String> nicknames) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.addLog(nicknames.toString());
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyOthersJoinGame(String client) {
        List<ClientHandler> copy;
        synchronized (this){
            copy = new ArrayList<>(clientHandlers.stream()
                    .filter(c -> !c.getNickname().equals(client)).toList());
        }
        for(ClientHandler c : copy)
            c.addLog(client + " joined the game");
    }

    public void notifyGameCreated(String client, Game pov) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.addLog("Game created!");
            clientHandler.setGame(pov);
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyPlaceStarterSide(String client, Game pov) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.addLog("Starter card placed!");
            clientHandler.setGame(pov);
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyOthersPlaceStarterSide(String client, Game pov) {
        List<ClientHandler> copy;
        synchronized (this){
            copy = new ArrayList<>(clientHandlers.stream()
                    .filter(c -> !c.getNickname().equals(client)).toList());
        }
        for(ClientHandler c : copy)
            c.setGame(pov);
    }

    public void notifyHandsAndObjectivesDealt(String client, Game pov) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.setGame(pov);
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyChooseObjective(String client) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.addLog("Objective chosen!");
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyAllGameStarted() {
        List<ClientHandler> copy;
        synchronized (this){
            copy = new ArrayList<>(clientHandlers);
        }
        for(ClientHandler c : copy)
            c.addLog("Game started!");
    }

    public void notifyPlaceSide(String client, Game pov) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
                clientHandler.addLog("Card placed!");
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyOthersPlaceSide(String client, Game pov) {
        List<ClientHandler> copy;
        synchronized (this){
            copy = new ArrayList<>(clientHandlers.stream()
                    .filter(c -> !c.getNickname().equals(client)).toList());
        }
        for(ClientHandler c : copy)
            c.setGame(pov);
    }

    public void notifyDrawVisible(String client, Game pov) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.setGame(pov);
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyOthersDrawVisible(String client, Game pov) {
        List<ClientHandler> copy;
        synchronized (this){
            copy = new ArrayList<>(clientHandlers.stream()
                    .filter(c -> !c.getNickname().equals(client)).toList());
        }
        for(ClientHandler c : copy)
            c.setGame(pov);
    }

    public void notifyDrawDeck(String client, Game pov) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.setGame(pov);
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyOthersDrawDeck(String client, Game pov) {
        List<ClientHandler> copy;
        synchronized (this){
            copy = new ArrayList<>(clientHandlers.stream()
                    .filter(c -> !c.getNickname().equals(client)).toList());
        }
        for(ClientHandler c : copy)
            c.setGame(pov);
    }

    public void notifyGameResumed(String client, Game pov) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.setGame(pov);
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyOthersGameResumed(String client) {
        List<ClientHandler> copy;
        synchronized (this){
            copy = new ArrayList<>(clientHandlers.stream()
                    .filter(c -> !c.getNickname().equals(client)).toList());
        }
        for(ClientHandler c : copy)
            c.addLog("Game resumed!");
    }

    public void notifyOthersQuitGame(String client) {
        List<ClientHandler> copy;
        synchronized (this){
            copy = new ArrayList<>(clientHandlers.stream()
                    .filter(c -> !c.getNickname().equals(client)).toList());
        }
        for(ClientHandler c : copy)
            c.addLog(client + " has quit the game!");
    }

    public void notifyAllGamePaused(String client) {
        List<ClientHandler> copy;
        synchronized (this){
            copy = new ArrayList<>(clientHandlers);
        }
        for(ClientHandler c : copy)
            c.addLog("Game paused!");
    }

    public void notifyException(String client, Exception exception) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.addLog(exception.getMessage());
        } catch (NoSuchPlayerException ignored) {}
    }
}
