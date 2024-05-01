package it.polimi.is24am05.controller;

import it.polimi.is24am05.controller.exceptions.ConnectionRefusedException;
import it.polimi.is24am05.controller.exceptions.FirstConnectionException;
import it.polimi.is24am05.controller.exceptions.InvalidNumUsersException;
import it.polimi.is24am05.controller.socketServer.SocketServer;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.enums.state.GameState;
import it.polimi.is24am05.model.exceptions.deck.EmptyDeckException;
import it.polimi.is24am05.model.exceptions.deck.InvalidVisibleCardException;
import it.polimi.is24am05.model.exceptions.game.*;
import it.polimi.is24am05.model.exceptions.playArea.InvalidCoordinatesException;
import it.polimi.is24am05.model.exceptions.playArea.NoAdjacentCardException;
import it.polimi.is24am05.model.exceptions.playArea.PlacementNotAllowedException;
import it.polimi.is24am05.model.exceptions.player.InvalidCardException;
import it.polimi.is24am05.model.exceptions.player.InvalidSideException;
import it.polimi.is24am05.model.exceptions.player.InvalidStarterSideException;
import it.polimi.is24am05.model.exceptions.player.ObjectiveNotAllowedException;
import it.polimi.is24am05.model.game.Game;
import it.polimi.is24am05.model.objective.Objective;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

import static it.polimi.is24am05.model.game.Game.MAX_PLAYERS;
import static it.polimi.is24am05.model.game.Game.MIN_PLAYERS;

// TODO: remove debug printf() commands
public class Controller {
    private int numUsers;

    /**
     * List of connected clients, identified by name
     */
    private List<String> users = new LinkedList<>();
    private LobbyState lobbyState;
    private final List<GameServer> servers = new LinkedList<>();
    public Game game;

    /**
     * Controller for a loaded game
     * @param game old game
     */
    public Controller(Game game) {
        this.game = game;
        this.numUsers = game.getNicknames().size();
        this.lobbyState = LobbyState.OLD;
        setServers();

    }

    /**
     * Controller for a new game
     */
    public Controller(){
        this.lobbyState = LobbyState.NEW;
        setServers();
    }

    /**
     * Handles a new connection from the network
     * @param playerNickname nickname of the player trying to connect
     * @throws ConnectionRefusedException if the connection may not be established
     * @throws FirstConnectionException if this is the first connection, a different method must be invoked
     */
    public synchronized void newConnection(String playerNickname) throws ConnectionRefusedException, FirstConnectionException {
        if(this.lobbyState == LobbyState.OLD){
            // If the user was not part of the old game
            if(!game.getNicknames().contains(playerNickname))
                throw new ConnectionRefusedException("User not part of the old game");
            // If the user is already connected
            if(this.users.contains(playerNickname))
                throw new ConnectionRefusedException("User already connected");
            // Add the user to the list of reconnected users
            users.add(playerNickname);

            // If all have reconnected resume the game
            if(this.users.size() == this.numUsers) {
                for (String nickname : this.users) {
                    try {
                        game.reconnect(nickname);
                    } catch (NoSuchPlayerException ignored) {}
                }
                //tell all players the game is resumed
                for (GameServer server : servers){
                    server.sendBroadcast("Game resumed");
                }
                this.lobbyState = LobbyState.STARTED;
                // TODO: send the users the game state
                for (GameServer server : servers) {
                    server.sendBroadcast(this.game.toString());
                }
            }
            //tell the user the game is waiting for others to reconnect
            else {
                for (GameServer server : servers) {
                    server.send("Waiting for others to reconnect", playerNickname);
                }
            }

        } else if (this.lobbyState == LobbyState.NEW) {
            // If numUsers parameter is not already set
            if(this.numUsers == 0)
                throw new FirstConnectionException("First Player to connect must specify numUsers parameter");

            // A user with the same name is already connected
            if(this.users.contains(playerNickname))
                throw new ConnectionRefusedException("User already connected with same nickname");

            this.users.add(playerNickname);

            // If enough player are connected
            if(this.users.size() == this.numUsers) {
                try {
                    this.game = new Game(this.users);
                    System.out.println("New game started with players: " + this.game.getNicknames());
                    this.lobbyState = LobbyState.STARTED;
                    // TODO: send the users the game state
                    for (GameServer server : servers) {
                        server.sendBroadcast(this.game.toString());
                    }
                } catch (PlayerNamesMustBeDifferentException | TooManyPlayersException | TooFewPlayersException e) {
                    // Should never happen
                    throw new RuntimeException(e);
                }
            }

        } else if(this.lobbyState == LobbyState.STARTED){
            // If the user was not part of the game
            if(!game.getNicknames().contains(playerNickname))
                throw new ConnectionRefusedException("User not part of the game");
            // If the user is already connected
            if(!game.getDisconnected().contains(playerNickname))
                throw new ConnectionRefusedException("User already connected");

            try {
                this.game.reconnect(playerNickname);
                // TODO: send the user the game state
                for (GameServer server : servers) {
                    server.send(this.game.toString(), playerNickname);
                }
                // If the game was stopped and now resumed after this connection, send it broadcast
                for (GameServer server : servers) {
                    server.sendBroadcast("Game resumed");
                }
            } catch (NoSuchPlayerException ignored) {}
        }
    }

    /**
     * Method to invoke if the connection to be established is the first
     * @param playerNickname nickname of the player trying to connect
     * @param numUsers number of players for the new game
     * @throws FirstConnectionException if this is NOT the first connection, a different method must be invoked
     * @throws InvalidNumUsersException if the number of players is not valid
     */
    public synchronized void newConnection(String playerNickname, int numUsers) throws FirstConnectionException, InvalidNumUsersException {
        if(this.lobbyState != LobbyState.NEW)
            throw new FirstConnectionException("Wrong method invocation");
        if(this.numUsers != 0)
            // Could happen if accessed at the same time
            throw new FirstConnectionException("Not the first player to connect");
        if(numUsers < MIN_PLAYERS || numUsers > MAX_PLAYERS)
            throw new InvalidNumUsersException();

        this.numUsers = numUsers;
        try {
            newConnection(playerNickname);
        } catch (ConnectionRefusedException ignored) {}
    }

    /**
     * Allows a player to place its starter Card
     * @param playerNickname player nickname
     * @param frontVisible true if the card will be facing up (showing the front side)
     * @throws NoSuchPlayerException propagated
     * @throws MoveNotAllowedException propagated
     * @throws InvalidStarterSideException propagated
     */
    public synchronized void playStarterCard(String playerNickname, Boolean frontVisible) throws NoSuchPlayerException, MoveNotAllowedException, InvalidStarterSideException {
        // A game must be loaded
        if(this.game == null || this.lobbyState != LobbyState.STARTED)
            throw new RuntimeException("Game not started yet");

        Card toPlay = game.getStarterCard(playerNickname);

        if(frontVisible)
            game.placeStarterSide(playerNickname, toPlay.getFrontSide());
        else
            game.placeStarterSide(playerNickname, toPlay.getBackSide());
    }

    /**
     * Allows a player to choose its objective card
     * @param playerNickname player nickname
     * @param objective objective chosen
     * @throws NoSuchPlayerException propagated
     * @throws MoveNotAllowedException propagated
     * @throws ObjectiveNotAllowedException propagated
     */
    public synchronized void chooseObjective(String playerNickname, Objective objective) throws NoSuchPlayerException, MoveNotAllowedException, ObjectiveNotAllowedException {
        // A game must be loaded
        if(this.game == null || this.lobbyState != LobbyState.STARTED)
            throw new RuntimeException("Game not started yet");

        game.chooseObjective(playerNickname, objective);
    }

    /**
     * Allows a player to place a card
     * @param playerNickname player nickname
     * @param card card placed
     * @param frontVisible true if the card will be facing up (showing the front side)
     * @param i row to place the card
     * @param j column to place the card
     * @throws PlacementNotAllowedException propagated
     * @throws NoSuchPlayerException propagated
     * @throws NotYourTurnException propagated
     * @throws MoveNotAllowedException propagated
     * @throws InvalidSideException propagated
     * @throws InvalidCoordinatesException propagated
     * @throws InvalidCardException propagated
     * @throws NoAdjacentCardException propagated
     */
    public synchronized void placeSide(String playerNickname, Card card, Boolean frontVisible, int i, int j) throws PlacementNotAllowedException, NoSuchPlayerException, NotYourTurnException, MoveNotAllowedException, InvalidSideException, InvalidCoordinatesException, InvalidCardException, NoAdjacentCardException {
        // A game must be loaded
        if(this.game == null || this.lobbyState != LobbyState.STARTED)
            throw new RuntimeException("Game not started yet");

        Side toPlay;
        if(frontVisible)
            toPlay = card.getFrontSide();
        else
            toPlay = card.getBackSide();

        game.placeSide(playerNickname, card, toPlay, i, j);
    }

    /**
     * Allows a player to draw a card from the decks
     * @param playerNickname player nickname
     * @param fromGold true if the card is drawn from the GoldCard deck
     * @throws NoSuchPlayerException propagated
     * @throws NotYourTurnException propagated
     * @throws MoveNotAllowedException propagated
     * @throws EmptyDeckException propagated
     */
    public synchronized void drawDeck(String playerNickname, Boolean fromGold) throws NoSuchPlayerException, NotYourTurnException, MoveNotAllowedException, EmptyDeckException {
        // A game must be loaded
        if(this.game == null || this.lobbyState != LobbyState.STARTED)
            throw new RuntimeException("Game not started yet");

        game.drawDeck(playerNickname, fromGold);
    }

    /**
     * Allows a player to draw a visible card
     * @param playerNickname player nickname
     * @param visible visible card chosen to draw
     * @throws MoveNotAllowedException propagated
     * @throws NoSuchPlayerException propagated
     * @throws NotYourTurnException propagated
     * @throws InvalidVisibleCardException propagated
     */
    public synchronized void drawVisible(String playerNickname, Card visible) throws MoveNotAllowedException, NoSuchPlayerException, NotYourTurnException, InvalidVisibleCardException {
        // A game must be loaded
        if(this.game == null || this.lobbyState != LobbyState.STARTED)
            throw new RuntimeException("Game not started yet");

        game.drawVisible(playerNickname, visible);
    }

    /**
     * Starts Socket and RMI servers.
     * @param args One string containing the path to an old game save file
     */
    public static void main(String[] args) {
        Controller controller;
        try {
            Game oldGame = loadGame(args[0]);
            System.out.println("Old game loaded");
            controller = new Controller(oldGame);

        } catch (IOException | ClassNotFoundException | ArrayIndexOutOfBoundsException e) {
            // Create a lobby for a new game if the file provided is not found or no file is provided
            System.out.println("Starting a new game");
            controller = new Controller();
        }

        // Start threads for Socket and RMI servers, pass the controller.
    }


    /**
     * @param gamePath path of the game save file
     * @return the game
     * @throws IOException propagated
     * @throws ClassNotFoundException propagated
     */
    private static Game loadGame(String gamePath) throws IOException, ClassNotFoundException {
         // Create FileInputStream to read data from the file
        FileInputStream fileIn = new FileInputStream(gamePath);
        // Create ObjectInputStream to deserialize object
        ObjectInputStream objectIn = new ObjectInputStream(fileIn);
        // Read object from file
        Game game = (Game) objectIn.readObject();
        // Close streams
        objectIn.close();
        fileIn.close();

        // Set all players to disconnected
        for(String nickname : game.getNicknames()) {
            try {
                game.disconnect(nickname);
            } catch (NoSuchPlayerException ignored) {}
        }

        return game;
    }

    /**
     * Records a player as disconnected
     * @param nickname of the player disconnected
     */
    public synchronized void disconnect(String nickname) throws NoSuchPlayerException {
        users.remove(nickname);

        if(this.game == null)
            return;

        GameState stateBeforeDisconnection = game.getGameState();
        game.disconnect(nickname);
        GameState stateAfterDisconnection = game.getGameState();

        // If the game is stopped after this disconnection
        if( (stateBeforeDisconnection == GameState.GAME || stateBeforeDisconnection == GameState.GAME_ENDING) && stateAfterDisconnection == GameState.PAUSE){
            // TODO tell all players the game was stopped
            for (GameServer server : servers) {
                server.sendBroadcast("Game stopped");
            }
            // Send the new game state
            // They may be able to tell given the new game state
        }
    }
    private void setServers(){
        SocketServer socketServer = new SocketServer(this);
        this.servers.add(socketServer);
        for(GameServer server: servers) {
            new Thread(server::start).start();
        }
    }
}
