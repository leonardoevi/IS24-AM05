package it.polimi.is24am05.controller;

import it.polimi.is24am05.controller.exceptions.ConnectionRefusedException;
import it.polimi.is24am05.controller.exceptions.FirstConnectionException;
import it.polimi.is24am05.controller.exceptions.InvalidNumUsersException;
import it.polimi.is24am05.controller.server.Server;
import it.polimi.is24am05.model.Player.Player;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.enums.state.GameState;
import it.polimi.is24am05.model.enums.state.PlayerState;
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

import java.io.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static it.polimi.is24am05.model.game.Game.MAX_PLAYERS;
import static it.polimi.is24am05.model.game.Game.MIN_PLAYERS;

public class Controller {
    private int numUsers;

    /**
     * List of connected clients, identified by name
     */
    private final List<String> users = new LinkedList<>();
    private LobbyState lobbyState;
    private final Server server = new Server(this);
    public Game game;

    private final List<String> messageRecipents = new ArrayList<>();

    // Schedule a thread that periodically saves the game
    private final ScheduledExecutorService saver = Executors.newSingleThreadScheduledExecutor();

    /**
     * Controller for a loaded game
     * @param game old game
     */
    public Controller(Game game) throws IOException {
        this.game = game;
        this.numUsers = game.getNicknames().size();
        this.lobbyState = LobbyState.OLD;

        this.messageRecipents.addAll(game.getNicknames().stream().toList());
        startSaver();
        server.start();
    }

    /**
     * Controller for a new game
     */
    public Controller() throws IOException {
        this.lobbyState = LobbyState.NEW;

        startSaver();
        server.start();
    }

    // Start the game saver demon thread
    private void startSaver(){
        Thread t = new Thread(new GameSaver(this)); t.setDaemon(true);
        saver.scheduleAtFixedRate(t, 3, 10, TimeUnit.SECONDS);
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
            // Broadcast update
            server.broadcastLog(messageRecipents, playerNickname + " rejoined!");
            // If all have reconnected resume the game
            if(this.users.size() == this.numUsers) {
                // Reconnect users starting from the user that was expected to play, in the same order of playing
                int turn = game.getTurn();
                List<String> nicknames =  game.getPlayers().stream().map(Player::getNickname).collect(Collectors.toList());

                // Sort the list such that the first player is the one that was expected to play
                while (turn > 0){
                    String toAppend = nicknames.getFirst();
                    nicknames.removeFirst();
                    nicknames.add(toAppend);
                    turn--;
                }

                // Reconnect the players
                for(String nickname : nicknames){
                    try {
                        game.reconnect(nickname);
                    } catch (NoSuchPlayerException e) {
                        System.out.println("Error reconnecting: " + nickname);
                    }
                }


                //tell all players the game is resumed
                this.lobbyState = LobbyState.STARTED;
                server.broadcastGameUpdated(messageRecipents);
                server.broadcastLog(messageRecipents, "Game restarted!"); // TODO : change this message
            }
        } else if (this.lobbyState == LobbyState.NEW) {
            // If numUsers parameter is not already set
            if(this.numUsers == 0)
                throw new FirstConnectionException("First Player to connect must specify numUsers parameter");

            // A user with the same name is already connected
            if(this.users.contains(playerNickname))
                throw new ConnectionRefusedException("User already connected with same nickname");

            this.users.add(playerNickname);

            // If all players are connected
            if(this.users.size() == this.numUsers) {
                try {
                    this.game = new Game(this.users);
                    this.messageRecipents.addAll(users);
                    this.lobbyState = LobbyState.STARTED;
                    server.broadcastGameUpdated(messageRecipents);
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
                GameState stateBeforeReconnection = game.getGameState();
                this.game.reconnect(playerNickname);
                GameState stateAfterReconnection = game.getGameState();

                server.gameUpdated(playerNickname);
                server.broadcastLog(messageRecipents.stream()
                        .filter(s -> !s.equals(playerNickname))
                        .toList(), playerNickname + " reconnected!");

                if(stateBeforeReconnection != stateAfterReconnection) {
                    server.broadcastGameUpdated(messageRecipents.stream()
                            .filter(s -> !s.equals(playerNickname))
                            .toList());
                    server.broadcastLog(messageRecipents, "Game resumed!");
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

        server.broadcastGameUpdated(messageRecipents);
    }

    /**
     * Allows a player to choose its objective card
     * @param playerNickname player nickname
     * @param objectiveId id of the chosen objective
     * @throws NoSuchPlayerException propagated
     * @throws MoveNotAllowedException propagated
     * @throws ObjectiveNotAllowedException propagated
     */
    public synchronized void chooseObjective(String playerNickname, String objectiveId) throws NoSuchPlayerException, MoveNotAllowedException, ObjectiveNotAllowedException {
        // A game must be loaded
        if(this.game == null || this.lobbyState != LobbyState.STARTED)
            throw new RuntimeException("Game not started yet");
        Objective objective;

        try {
            objective = Objective.valueOf(objectiveId);
        } catch (IllegalArgumentException e) {
            // Translate exception
            throw new ObjectiveNotAllowedException();
        }

        game.chooseObjective(playerNickname, objective);
        GameState stateAfterChoosing = game.getGameState();

        server.broadcastLog(messageRecipents.stream()
                .filter(s -> !s.equals(playerNickname))
                .toList(), playerNickname + " has chosen the secret objective!");
        server.broadcastLog(List.of(playerNickname), "Objective chosen!");

        if(stateAfterChoosing != GameState.CHOOSE_OBJECTIVE)
            server.broadcastGameUpdated(messageRecipents);

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

        server.broadcastGameUpdated(messageRecipents);
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

        server.broadcastGameUpdated(messageRecipents);
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

        server.broadcastGameUpdated(messageRecipents);
    }

    /**
     * Starts Socket and RMI servers.
     * @param args One string containing the path to an old game save file
     */
    public static void main(String[] args) throws IOException {
        try {
            Game oldGame = loadGame(args[0]);
            System.out.println("Old game loaded");
            new Controller(oldGame);
        } catch (IOException | ClassNotFoundException | ArrayIndexOutOfBoundsException e) {
            // Create a lobby for a new game if the file provided is not found or no file is provided
            System.out.println("Starting a new game");
            new Controller();
        }
    }




    /**
     * Records a player as disconnected
     * @param nickname of the player disconnected
     */
    public synchronized void disconnect(String nickname) throws NoSuchPlayerException {
        if(users.remove(nickname))
            System.out.println("Disconnecting " + nickname);

        if(this.game == null)
            return;

        GameState stateBeforeDisconnection = game.getGameState();
        PlayerState playerStateBeforeDisconnection = game.getPlayerState(nickname);
        game.disconnect(nickname);
        GameState stateAfterDisconnection = game.getGameState();

        // If the disconnection happened during PLACE_STARTER_CARD game state, broadcast the new game state
        if(stateBeforeDisconnection.equals(GameState.PLACE_STARTER_CARDS) && playerStateBeforeDisconnection.equals(PlayerState.PLACE_STARTER_CARD)) {
            server.broadcastGameUpdated(messageRecipents);
            server.broadcastLog(messageRecipents, nickname + "'s starter card automatically placed");
        }

        // If the disconnection happened during CHOOSE_OBJECTIVE game state, broadcast the new game state
        if(stateBeforeDisconnection.equals(GameState.CHOOSE_OBJECTIVE) && playerStateBeforeDisconnection.equals(PlayerState.CHOOSE_OBJECTIVE)) {
            server.broadcastGameUpdated(messageRecipents);
            server.broadcastLog(messageRecipents, nickname + "'s objective automatically chosen");
        }

        server.broadcastLog(messageRecipents, nickname + " disconnected!");

        // If the game is stopped after this disconnection
        if( (stateBeforeDisconnection == GameState.GAME || stateBeforeDisconnection == GameState.GAME_ENDING) && stateAfterDisconnection == GameState.PAUSE){
            server.broadcastLog(messageRecipents, "Game paused!");
        }
    }

    /**
     * Gets the list of users.
     * @return the list of users.
     */
    public synchronized List<String> getUsers() {
        return new LinkedList<>(users);
    }

    /**
     * Saves the current game state to a file. Without modifying anything.
     */
    public synchronized void saveGame(String path){
        if(game == null){
            //System.out.println("No game yet");
            return;
        }

        if(game.getGameState().equals(GameState.END)){
            System.out.println("Game already ended, skip saving");
            return;
        }

        try {
            // Create FileOutputStream to write data to a file
            System.out.print("⨕");
            FileOutputStream fileOut = new FileOutputStream(path);
            // Create ObjectOutputStream to serialize object
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            // Write object to file
            objectOut.writeObject(game);
            // Close streams
            objectOut.close();
            fileOut.close();
        } catch (Exception e) {
            System.out.println("Saving game failed");
        }
    }

    /**
     * @param gamePath path of the game save file
     * @return the game
     * @throws IOException propagated
     * @throws ClassNotFoundException propagated
     */
    public static Game loadGame(String gamePath) throws IOException, ClassNotFoundException {
        // Create FileInputStream to read data from the file
        FileInputStream fileIn = new FileInputStream(gamePath);
        // Create ObjectInputStream to deserialize object
        ObjectInputStream objectIn = new ObjectInputStream(fileIn);
        // Read object from file
        Game game = (Game) objectIn.readObject();
        // Close streams
        objectIn.close();
        fileIn.close();

        return game;
    }
}
