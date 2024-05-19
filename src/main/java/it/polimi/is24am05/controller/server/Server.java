package it.polimi.is24am05.controller.server;

import it.polimi.is24am05.controller.Controller;
import it.polimi.is24am05.controller.server.rmi.RmiHandlersProvider;
import it.polimi.is24am05.controller.server.rmi.RmiHandlersProviderImp;
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
            clientHandler.notifyJoinServer();
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyJoinGame(String client, List<String> nicknames) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.notifyJoinGame(nicknames);
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyOthersJoinGame(String client) {
        List<ClientHandler> copy;
        synchronized (this){
            copy = new ArrayList<>(clientHandlers.stream()
                    .filter(c -> !c.getNickname().equals(client)).toList());
        }
        for(ClientHandler c : copy)
            c.notifyOthersJoinGame(client);
    }

    public void notifyGameCreated(String client, Game pov) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.notifyGameCreated(pov);
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyPlaceStarterSide(String client, PlayArea playArea) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.notifyPlaceStarterSide(playArea);
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyOthersPlaceStarterSide(String client, PlayArea playArea) {
        List<ClientHandler> copy;
        synchronized (this){
            copy = new ArrayList<>(clientHandlers.stream()
                    .filter(c -> !c.getNickname().equals(client)).toList());
        }
        for(ClientHandler c : copy)
            c.notifyOthersPlaceStarterSide(client, playArea);
    }

    public void notifyHandsAndObjectivesDealt(String client, Game pov) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.notifyHandsAndObjectivesDealt(pov);
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyChooseObjective(String client) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.notifyChooseObjective();
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyAllGameStarted() {
        List<ClientHandler> copy;
        synchronized (this){
            copy = new ArrayList<>(clientHandlers);
        }
        for(ClientHandler c : copy)
            c.notifyAllGameStarted();
    }

    public void notifyPlaceSide(String client, PlayArea playArea, int points) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.notifyPlaceSide(playArea, points);
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyOthersPlaceSide(String client, PlayArea playArea, int points) {
        List<ClientHandler> copy;
        synchronized (this){
            copy = new ArrayList<>(clientHandlers.stream()
                    .filter(c -> !c.getNickname().equals(client)).toList());
        }
        for(ClientHandler c : copy)
            c.notifyOthersPlaceSide(client, playArea, points);
    }

    public void notifyDrawVisible(String client, Deck deck, List<Card> hand) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.notifyDrawVisible(deck, hand);
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyOthersDrawVisible(String client, boolean isGold, Deck deck, List<Card> hand) {
        List<ClientHandler> copy;
        synchronized (this){
            copy = new ArrayList<>(clientHandlers.stream()
                    .filter(c -> !c.getNickname().equals(client)).toList());
        }
        for(ClientHandler c : copy)
            c.notifyOthersDrawVisible(client, isGold, deck, hand);
    }

    public void notifyDrawDeck(String client, Deck deck, List<Card> hand) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.notifyDrawDeck(deck, hand);
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyOthersDrawDeck(String client, boolean isGold, Deck deck, List<Card> hand) {
        List<ClientHandler> copy;
        synchronized (this){
            copy = new ArrayList<>(clientHandlers.stream()
                    .filter(c -> !c.getNickname().equals(client)).toList());
        }
        for(ClientHandler c : copy)
            c.notifyOthersDrawDeck(client, isGold, deck, hand);
    }

    public void notifyGameResumed(String client, Game pov) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.notifyGameResumed(pov);
        } catch (NoSuchPlayerException ignored) {}
    }

    public void notifyOthersGameResumed(String client) {
        List<ClientHandler> copy;
        synchronized (this){
            copy = new ArrayList<>(clientHandlers.stream()
                    .filter(c -> !c.getNickname().equals(client)).toList());
        }
        for(ClientHandler c : copy)
            c.notifyOthersGameResumed(client);
    }

    public void notifyOthersQuitGame(String client) {
        List<ClientHandler> copy;
        synchronized (this){
            copy = new ArrayList<>(clientHandlers.stream()
                    .filter(c -> !c.getNickname().equals(client)).toList());
        }
        for(ClientHandler c : copy)
            c.notifyOthersQuitGame(client);
    }

    public void notifyAllGamePaused(String client) {
        List<ClientHandler> copy;
        synchronized (this){
            copy = new ArrayList<>(clientHandlers);
        }
        for(ClientHandler c : copy)
            c.notifyAllGamePaused();
    }

    public void notifyException(String client, Exception exception) {
        try {
            ClientHandler clientHandler = getClientHandler(client);
            clientHandler.notifyException(exception);
        } catch (NoSuchPlayerException ignored) {}
    }
}
