package it.polimi.is24am05.controller;

import it.polimi.is24am05.controller.exceptions.ConnectionRefusedException;
import it.polimi.is24am05.controller.exceptions.FirstConnectionException;
import it.polimi.is24am05.controller.exceptions.InvalidNumUsersException;
import it.polimi.is24am05.model.exceptions.game.PlayerNamesMustBeDifferentException;
import it.polimi.is24am05.model.exceptions.game.TooFewPlayersException;
import it.polimi.is24am05.model.exceptions.game.TooManyPlayersException;
import it.polimi.is24am05.model.game.Game;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

import static it.polimi.is24am05.model.game.Game.MAX_PLAYERS;
import static it.polimi.is24am05.model.game.Game.MIN_PLAYERS;

public class Controller {
    private int numUsers;

    /**
     * List of connected clients, identified by name
     */
    private List<String> users;
    private LobbyState lobbyState;
    private Game game;

    /**
     * Controller for a loaded game
     * @param game old game
     */
    public Controller(Game game) {
        this.game = game;
        this.users = new LinkedList<>();
        this.numUsers = game.getNicknames().size();
        this.lobbyState = LobbyState.OLD;
    }

    /**
     * Controller for a new game
     */
    public Controller(){
        this.lobbyState = LobbyState.NEW;
    }

    /**
     * Handles a new connection from the network
     * @param playerNickname nickname of the player trying to connect
     * @throws ConnectionRefusedException if the connection may not be established
     * @throws FirstConnectionException if this is the first connection, a different method must be invoked
     */
    public void newConnection(String playerNickname) throws ConnectionRefusedException, FirstConnectionException {
        if(this.lobbyState == LobbyState.OLD){
            // If the user was not part of the old game
            if(!game.getNicknames().contains(playerNickname))
                throw new ConnectionRefusedException("User not part of the old game");
            // If the user is not already connected
            if(this.users.contains(playerNickname))
                throw new ConnectionRefusedException("User already connected");
            // Add the user to the list of reconnected users
            users.add(playerNickname);

            // If all have reconnected restart the game
            if(this.users.size() == this.numUsers) {
                for (String nickname : this.users)
                    game.reConnect(nickname);

                this.lobbyState = LobbyState.STARTED;
            }

            this.game.reConnect(playerNickname); // TODO: REMOVE THIS LINE?
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
                    this.lobbyState = LobbyState.STARTED;
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

            this.game.reConnect(playerNickname);
        }
    }

    /**
     * Method to invoke if the connection to be established is the first
     * @param playerNickname nickname of the player trying to connect
     * @param numUsers number of players for the new game
     * @throws FirstConnectionException if this is NOT the first connection, a different method must be invoked
     * @throws InvalidNumUsersException if the number of players is not valid
     */
    public void newConnection(String playerNickname, int numUsers) throws FirstConnectionException, InvalidNumUsersException {
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
        for(String nickname : game.getNicknames())
            game.disconnect(nickname);

        return game;
    }

    public void disconnect(String nickname) {
        // TODO: gestire eccezione ??
        users.remove(nickname);
        game.disconnect(nickname);
    }
}
