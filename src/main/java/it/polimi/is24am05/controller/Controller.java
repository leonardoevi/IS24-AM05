package it.polimi.is24am05.controller;

import it.polimi.is24am05.controller.exceptions.ConnectionRefusedException;
import it.polimi.is24am05.controller.exceptions.FirstConnectionException;
import it.polimi.is24am05.controller.exceptions.InvalidNumUsersException;
import it.polimi.is24am05.controller.server.Server;
import it.polimi.is24am05.model.Player.Player;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.deck.Deck;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

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

    /**
     * Controller for a loaded game
     * @param game old game
     */
    public Controller(Game game) throws IOException {
        this.game = game;
        this.numUsers = game.getNicknames().size();
        this.lobbyState = LobbyState.OLD;
        server.start();
    }

    /**
     * Controller for a new game
     */
    public Controller() throws IOException {
        this.lobbyState = LobbyState.NEW;
        server.start();
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
            // Reply to the user
            server.notifyJoinGame(playerNickname, getUsers());
            // Broadcast update
            server.notifyOthersJoinGame(playerNickname);
            // If all have reconnected resume the game
            if(this.users.size() == this.numUsers) {
                for (String nickname : this.users) {
                    try {
                        game.reconnect(nickname);
                    } catch (NoSuchPlayerException ignored) {}
                }
                //tell all players the game is resumed
                this.lobbyState = LobbyState.STARTED;
                for (Player player : game.getPlayers())
                    server.notifyGameResumed(player.getNickname(), null); // TODO
            }
        } else if (this.lobbyState == LobbyState.NEW) {
            // If numUsers parameter is not already set
            if(this.numUsers == 0)
                throw new FirstConnectionException("First Player to connect must specify numUsers parameter");
            // A user with the same name is already connected
            if(this.users.contains(playerNickname))
                throw new ConnectionRefusedException("User already connected with same nickname");
            this.users.add(playerNickname);
            // Reply to the user
            server.notifyJoinGame(playerNickname, getUsers());
            // Broadcast update
            server.notifyOthersJoinGame(playerNickname);
            // If all players are connected
            if(this.users.size() == this.numUsers) {
                try {
                    this.game = new Game(this.users);
                    this.lobbyState = LobbyState.STARTED;
                    for (Player player : game.getPlayers())
                        notifyGameCreated(player);
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
                server.notifyGameResumed(playerNickname, null); // TODO
                server.notifyOthersGameResumed(playerNickname);
            } catch (NoSuchPlayerException ignored) {}
        }
    }

    public void notifyGameCreated(Player player) {
        List<Map<String, Object>> players = new ArrayList<>();
        for (Player p : game.getPlayers()) {
            if (p.equals(player))
                continue;
            players.add(Map.of(
                    "nickname", p.getNickname(),
                    "turn", game.getPlayerTurn(p),
                    "starterCard", p.getStarterCard()
            ));
        }
        server.notifyGameCreated(
                player.getNickname(),
                game.getResourceDeck().getPov(),
                game.getGoldDeck().getPov(),
                game.getPlayerTurn(player),
                player.getColor(),
                player.getStarterCard(),
                players
        );
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

        server.notifyPlaceStarterSide(playerNickname, game.getPlayArea(playerNickname));
        server.notifyOthersPlaceStarterSide(playerNickname, game.getPlayArea(playerNickname));
        // If the game state changed, i.e. all players placed their starter card, update the clients
        if (game.getGameState().equals(GameState.CHOOSE_OBJECTIVE)) {
            for (Player player : game.getPlayers()) {
                server.notifyHandsAndObjectivesDealt(player.getNickname(), null); // TODO
            }
        }
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
        Objective objective = Objective.valueOf(objectiveId);
        game.chooseObjective(playerNickname, objective);

        server.notifyChooseObjective(playerNickname);
        // If the game state changed, i.e. all players chose their objective, update the clients
        if (game.getGameState().equals(GameState.GAME))
            server.notifyAllGameStarted();
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

        server.notifyPlaceSide(playerNickname, game.getPlayArea(playerNickname), game.getPoints(playerNickname));
        server.notifyOthersPlaceSide(playerNickname, game.getPlayArea(playerNickname), game.getPoints(playerNickname));
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

        Deck deck = fromGold ? game.getGoldDeck() : game.getResourceDeck();
        server.notifyDrawDeck(playerNickname, deck, game.getHand(playerNickname));
        server.notifyOthersDrawDeck(playerNickname, fromGold, deck, null); // TODO
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

        boolean isGold = visible.getId() > 40;
        Deck deck = visible.getId() > 40 ? game.getGoldDeck() : game.getResourceDeck();
        server.notifyDrawVisible(playerNickname, deck, game.getHand(playerNickname));
        server.notifyOthersDrawVisible(playerNickname, isGold, deck, null); // TODO
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

        server.notifyOthersQuitGame(nickname);
        // If the game is stopped after this disconnection
        if( (stateBeforeDisconnection == GameState.GAME || stateBeforeDisconnection == GameState.GAME_ENDING) && stateAfterDisconnection == GameState.PAUSE){
            server.notifyAllGamePaused(nickname);
        }
    }

    /**
     * Gets the list of users.
     * @return the list of users.
     */
    public synchronized List<String> getUsers() {
        return new LinkedList<>(users);
    }
}
