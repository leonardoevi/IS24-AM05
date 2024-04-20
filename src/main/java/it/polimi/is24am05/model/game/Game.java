package it.polimi.is24am05.model.game;

import it.polimi.is24am05.model.Player.Player;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.goldCard.GoldCard;
import it.polimi.is24am05.model.card.resourceCard.ResourceCard;
import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.card.starterCard.StarterCard;
import it.polimi.is24am05.model.deck.Deck;
import it.polimi.is24am05.model.enums.Color;
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
import it.polimi.is24am05.model.objective.Objective;

import java.io.Serializable;
import java.util.*;

public class Game implements Serializable {
    /**
     * List of winners of the game, updated after game is ended.
     */
    private List<Player> winners = new ArrayList<>();
    /**
     * Players in the game. No player can be added.
     */
    private final List<Player> players;


    /**
     * Players connected in the game, I assume that no player can disconnect before playing the initial card an choosing the objective card
     */

    private final Set<Player> connected;

    /**
     * Keeps track of the player that is expected to play i.e. players.get(turn)
     */

    private int turn;

    /**
     * Decks
     */
    private final Deck resourceDeck, goldDeck;

    /**
     * State of the game, used to know what move is expected
     */
    private GameState gameState;


    /**
     * Used to track che state of the game, i.e gameState, before the game is paused
     */
    private GameState lastGameState;

    /**
     * Objectives that every player can see
     */
    private final Objective[] sharedObjectives;

    // Magic Numbers
    private final static int NUMBER_OF_SHARED_OBJECTIVES = 2;
    private final static int NUMBER_OF_OBJECTIVES_TO_DEAL = 2;
    private final static int POINTS_TO_END = 20;
    private final static int MAX_PLAYERS = Color.values().length;
    private final static int MIN_PLAYERS = 2;

    /**
     * Game constructor. Initializes Players with a starting card only. Initializes Decks.
     * All players are then expected to place the card they were given.
     * @param nicknames List of the UNIQUE names of the players that will be part of the game
     * @throws TooManyPlayersException if there are more players than the rules of the game allow for
     */
    public Game(List<String> nicknames) throws PlayerNamesMustBeDifferentException, TooManyPlayersException, TooFewPlayersException {
        // Make sure all string are different
        if(nicknames.stream().distinct().count() != nicknames.size())
            throw new PlayerNamesMustBeDifferentException();

        // Make sure there are not too many or too few players
        if(nicknames.size() > MAX_PLAYERS)
            throw new TooManyPlayersException();
        if(nicknames.size() < MIN_PLAYERS)
            throw new TooFewPlayersException();

        // Initialize Players, with a randomly assigned color <-- might change later
            // Prepare a random list of Colors
        List<Color> colors = new LinkedList<>(Arrays.stream(Color.values()).toList());
        Collections.shuffle(colors);

            // Fill up the list of players
        this.players = new ArrayList<>(nicknames.size());
        for(String nick : nicknames){
            Color color = colors.getFirst();
            players.add(new Player(nick, color));
            colors.remove(color);
        }

        // Fill up the set of connected players, i.e all the players
        this.connected=new HashSet<>(nicknames.size());
        connected.addAll(this.players);

        // Randomly decide the order in which players are going to take turns
        Collections.shuffle(this.players);
        this.turn = 0;

        // Initialize Decks
        this.resourceDeck = new Deck(ResourceCard.values());
        this.goldDeck = new Deck(GoldCard.values());


        // Deal a StarterCard to each Player
            // Prepare a shuffled list of StarterCards
        List<StarterCard> starterCards = new LinkedList<>(Arrays.stream(StarterCard.values()).toList());
        Collections.shuffle(starterCards);
            // Give a card to each player
        for(Player player : this.players){
            StarterCard starterCard = starterCards.getFirst();
            player.dealStarterCard(starterCard);
            starterCards.remove(starterCard);
        }


        // SharedObjectives will be revealed later
        this.sharedObjectives = new Objective[NUMBER_OF_SHARED_OBJECTIVES];

        // Set game state
        this.gameState = GameState.PLACE_STARTER_CARDS;
    }

    /**
     * @param nickname Player name
     * @return The Player with the specified nickname
     * @throws NoSuchPlayerException If such a player is not in the game
     */
    private Player findPlayer(String nickname) throws NoSuchPlayerException {
        return players.stream().filter(p -> p.getNickname().equals(nickname)).findFirst().orElseThrow(NoSuchPlayerException::new);
    }

    /**
     * @param player is the Player
     * @return true if player is connected
     */
    private boolean isPlayerConnected(Player player)
    {
        return this.connected.contains(player);
    }

    /**
     * @param expected Expected game state
     * @throws MoveNotAllowedException If the current game state is different from the expected game state
     */
    private void checkState(GameState expected) throws MoveNotAllowedException {
        if(this.gameState != expected)
            throw new MoveNotAllowedException("Current game state different from expected");
    }

    /**
     * @param expected Expected game state
     * @param alternative Alternative expected game state
     * @throws MoveNotAllowedException If the current game state is different from the expected game state
     */
    private void checkState(GameState expected, GameState alternative) throws MoveNotAllowedException {
        if(this.gameState != expected && this.gameState != alternative)
            throw new MoveNotAllowedException("Current game state different from expected");
    }

    /**
     * @return True if a player can draw a card from one of the decks
     */
    private boolean cardLeftToDraw(){
        return !this.goldDeck.getVisible().isEmpty() || !this.resourceDeck.getVisible().isEmpty();
    }

    /**
     * After game initialization players have to place their starter Card
     * @param playerName Name of the player that is placing the card
     * @param side Side of the card that is facing up
     * @throws InvalidStarterSideException If the side does not correspond to the card
     * @throws NoSuchPlayerException If such a player is not in the game
     * @throws MoveNotAllowedException If the current game state is not PLACE_STARTER_CARDS
     */
    public void placeStarterSide(String playerName, Side side) throws InvalidStarterSideException, NoSuchPlayerException, MoveNotAllowedException {
        // Check if the current game state is PLACE_STARTER_CARDS
        checkState(GameState.PLACE_STARTER_CARDS);

        // Find the player
        Player toPlay = findPlayer(playerName);

        // Check if it's time for the player to place the starter card
        if(toPlay.getState() != PlayerState.PLACE_STARTER_CARD)
            throw new MoveNotAllowedException("Starter card already placed");

        // Place the card
        toPlay.playStarterCard(side);

        // Update Player state
        toPlay.setState(PlayerState.CHOOSE_OBJECTIVE);

        // If every player has placed their card
        boolean allHavePlaced = this.players.stream()
                .allMatch(p -> p.getState() == PlayerState.CHOOSE_OBJECTIVE);

        if(allHavePlaced) {
            // Deal the objectives
            dealHandsAndObjectives();
        }
    }

    /**
     * After placing the starter Cards each player receives a GoldCard, 2 ResourceCards and 2 Objectives.
     * Players must all choose which ObjectiveCard they wish to keep.
     */
    private void dealHandsAndObjectives(){
        // Every Player has placed their starterCard

        // Each player draws 2 Resource Cards and 1 Gold Card
        // Assume there are enough cards in the decks
        assert ResourceCard.values().length - 2 > players.size() * 2;
        assert GoldCard.values().length - 2 > players.size() * 2; // -2 is for the card that are revealed

        for(Player player : players){
            try {
                player.drawCard(this.resourceDeck.drawTop());
                player.drawCard(this.resourceDeck.drawTop());
                player.drawCard(this.goldDeck.drawTop());
            } catch (EmptyDeckException e) {
                throw new RuntimeException("Not enough cards in the deck to initialize player hands");
            }
        }

        // Prepare a shuffled list of objective
        List<Objective> objectivesDeck = new LinkedList<>(Arrays.stream(Objective.values()).toList());
        Collections.shuffle(objectivesDeck);

        // Assume there are more than NUMBER_OF_SHARED_OBJECTIVES + NUMBER_OF_OBJECTIVES_TO_DEAL * MAX_PLAYERS
        // objectives in total
        assert objectivesDeck.size() > (NUMBER_OF_SHARED_OBJECTIVES + NUMBER_OF_OBJECTIVES_TO_DEAL * MAX_PLAYERS);

        // Reveal the common objectives
        for (int i=0; i < this.sharedObjectives.length; i++){
            Objective toReveal = objectivesDeck.getFirst();
            this.sharedObjectives[i] = toReveal;
            objectivesDeck.remove(toReveal);
        }

        // Deal the secret objectives to each player
        for (Player player : this.players){
            Objective[] objectivesHand = new Objective[NUMBER_OF_OBJECTIVES_TO_DEAL];
            // Fill the objectives Hand
            for (int i=0; i < objectivesHand.length; i++){
                Objective toDeal = objectivesDeck.getFirst();
                objectivesHand[i] = toDeal;
                objectivesDeck.remove(toDeal);
            }
            player.dealObjectives(objectivesHand);
        }

        // Player will now select an objective each
        this.gameState = GameState.CHOOSE_OBJECTIVE;

    }

    /**
     * Allows a player to choose the objective they wish to keep
     * @param playerName Player choosing
     * @param objective  Objective to be chosen
     * @throws MoveNotAllowedException If the current game state is not CHOOSE_OBJECTIVE
     * @throws NoSuchPlayerException If such a player is not in the game
     * @throws ObjectiveNotAllowedException If the player doesn't have that objective card
     */
    public void chooseObjective(String playerName, Objective objective) throws MoveNotAllowedException, NoSuchPlayerException, ObjectiveNotAllowedException {
        // Check if the current game state is CHOOSE_OBJECTIVE
        checkState(GameState.CHOOSE_OBJECTIVE);

        // Find the player
        Player player = findPlayer(playerName);

        // Check if it's time for the player to choose an objective
        if(player.getState() != PlayerState.CHOOSE_OBJECTIVE)
            throw new MoveNotAllowedException("It is not time to choose an objective");

        // Choose the objective
        player.chooseObjective(objective);

        // Update player state
        player.setState(PlayerState.DRAW);

        // If all players have chosen their objective
        boolean allHaveChosen = this.players.stream()
                .allMatch(p -> p.getState() == PlayerState.DRAW);

        // It is time to start playing
        if(allHaveChosen) {
            this.gameState = GameState.GAME;
            for (Player p : players)
                p.setState(PlayerState.PLACE);
        }
    }

    /**
     * Allows a player to place a card in their play area
     * If there is at least a card left to draw, sets its PlayerState to DRAW
     * Sets the game state to GAME_ENDING and ends the player turn otherwise.
     * @param playerName The player that wants to play
     * @param card The card to be placed
     * @param side The side that will be visible
     * @param i Coordinate i in the PlayArea
     * @param j Coordinate j in the PlayArea
     * @throws MoveNotAllowedException If it is not time to play a card or if the current game state is not GAME
     * @throws NoSuchPlayerException If such a player is not in the game
     * @throws NotYourTurnException If it is not the Player's turn
     * @throws PlacementNotAllowedException propagated
     * @throws InvalidSideException propagated
     * @throws InvalidCoordinatesException propagated
     * @throws InvalidCardException propagated
     * @throws NoAdjacentCardException propagated
     */
    public void placeSide(String playerName, Card card, Side side, int i, int j) throws MoveNotAllowedException, NoSuchPlayerException, NotYourTurnException, PlacementNotAllowedException, InvalidSideException, InvalidCoordinatesException, InvalidCardException, NoAdjacentCardException {
        // Check if the current game state is GAME
        checkState(GameState.GAME, GameState.GAME_ENDING);     // throws MoveNotAllowedException

        // Find the player
        Player player = findPlayer(playerName);     // throws NoSuchPlayerException

        // Check if it's the Player turn
        if(this.players.get(turn) != player || !isPlayerConnected(player)){
            throw new NotYourTurnException();
        }

        // Check if it's time for the player to place a Card
        if(player.getState() != PlayerState.PLACE)
            throw new MoveNotAllowedException("It is time to place a card");


        // Place the card
        player.playSide(card, side, i, j);  // throws InvalidCardException
                                            // InvalidSideException
                                            // PlacementNotAllowedException
                                            // InvalidCoordinatesException
                                            // NoAdjacentCardException

        // If there is something left to draw
        if(cardLeftToDraw())
            // Time to draw a card
            player.setState(PlayerState.DRAW);
        else {
            // The game is ending and the player must not Draw
            this.gameState = GameState.GAME_ENDING;
            nextTurn();
        }
    }

    /**
     * Allows a player to draw a Card from a Deck
     * Ends the player turn
     * @param playerName Player name
     * @param fromGold true if the player wants to draw from the Gold Cards Deck
     * @throws MoveNotAllowedException If the current game state is not CHOOSE_OBJECTIVE
     * @throws NoSuchPlayerException If such a player is not in the game
     * @throws NotYourTurnException If it is not the Player's turn
     * @throws EmptyDeckException propagated
     */
    public void drawDeck(String playerName, boolean fromGold) throws MoveNotAllowedException, NoSuchPlayerException, NotYourTurnException, EmptyDeckException {
        // Check if the current game state is GAME
        checkState(GameState.GAME, GameState.GAME_ENDING);     // throws MoveNotAllowedException

        // Find the player
        Player player = findPlayer(playerName);     // throws NoSuchPlayerException

        // Check if it's the Player turn
        if(this.players.get(turn) != player || !isPlayerConnected(player)){
            throw new NotYourTurnException();
        }

        // Check if it is time for the player to draw
        if(player.getState() != PlayerState.DRAW)
            throw new MoveNotAllowedException("It is time to draw a Card");

        // Select the deck
        Deck toDrawFrom;
        if(fromGold)
            toDrawFrom = this.goldDeck;
        else
            toDrawFrom = this.resourceDeck;

        // Draw the card
        Card drawn = toDrawFrom.drawTop(); // throws EmptyDeckException

        // Add it to the player hand
        player.drawCard(drawn);

        // End the turn
        player.setState(PlayerState.PLACE);
        nextTurn();
    }

    /**
     * Allows a player to draw a visible Card
     * Ends the player turn
     * @param playerName Player name
     * @param visible the card to Draw
     * @throws MoveNotAllowedException If the current game state is not CHOOSE_OBJECTIVE
     * @throws NoSuchPlayerException If such a player is not in the game
     * @throws NotYourTurnException If it is not the Player's turn
     * @throws InvalidVisibleCardException propagated
     */
    public void drawVisible(String playerName, Card visible) throws MoveNotAllowedException, NoSuchPlayerException, NotYourTurnException, InvalidVisibleCardException{
        // Check if the current game state is GAME
        checkState(GameState.GAME, GameState.GAME_ENDING);     // throws MoveNotAllowedException

        // Find the player
        Player player = findPlayer(playerName);


        // Check if it's the Player turn
        if(this.players.get(turn) != player || !isPlayerConnected(player)){
            throw new NotYourTurnException();
        }

        // Check if it is time for the player to draw
        if(player.getState() != PlayerState.DRAW)
            throw new MoveNotAllowedException("It is time to draw a Card");

        // Select the correct Deck to draw from
        Deck toDrawFrom;
        if(visible instanceof ResourceCard)
            toDrawFrom = this.resourceDeck;
        else
            toDrawFrom = this.goldDeck;

        // Draw the card
        Card drawn = toDrawFrom.drawVisible(visible);

        // Add the card to player hand
        player.drawCard(drawn);

        // End the turn
        player.setState(PlayerState.PLACE);
        nextTurn();

    }

    /**
     * Ends the current player turn.
     * If a player has reached POINTS_TO_END points it allows each player left to play,
     * such that everyone will have played the same number of turns.
     * After everyone has played the last turn, it ends the game.
     */
    private void nextTurn(){

        // If a player has reached 20 points
        if(players.stream().anyMatch(p -> p.getPoints() >= POINTS_TO_END))
            this.gameState = GameState.GAME_ENDING;

        // If only last turns must be played
        if(this.gameState == GameState.GAME_ENDING){
            // Increase the index
            this.turn += 1;
            while(this.turn<this.players.size() && !isPlayerConnected(this.players.get(this.turn)))
             {
                 this.turn+=1;
             }
        } else
        {
            // Increase the index, keeping it inside the boundary
            this.turn = (this.turn + 1) % players.size();

          while(!isPlayerConnected(this.players.get(this.turn)))
            {
                 this.turn = (this.turn + 1) % players.size();
            }
        }

        // If the index is out of bound, the game has ended
        if(this.turn >= players.size()) {
            this.gameState = GameState.END;
            endGame();
        }
    }

    /**
     * Adds the points to each player depending on the objectives they achieved.
     * Selects the winner(s).
     */
    private void endGame(){
        // Evaluate objectives for each player
        for(Player player : this.players)
            player.evaluateObjectives(this.sharedObjectives);

        // Find Winner(s)
        List<Player> ranking = players.stream()
                        .sorted(Comparator.comparingInt(Player::getPoints)
                                          .thenComparingInt(Player::getSatisfiedObjectiveCards))
                        .toList();

            // The player with fewer points is first in the list
        int winnerPoints = ranking.getLast().getPoints();
        int winnerObjectives = ranking.getLast().getSatisfiedObjectiveCards();

        this.winners = ranking.stream()
                        .filter(p -> p.getPoints() == winnerPoints && p.getSatisfiedObjectiveCards() == winnerObjectives)
                        .toList();
    }

    /**
     * handles the disconnection of a player
     * @param nickname is the nickname of the player to disconnect
     * @throws NoSuchPlayerException propagated
     */

    public void disconnect(String nickname) throws NoSuchPlayerException {
        Player toDisconnect=findPlayer(nickname);
        this.connected.remove(toDisconnect);

        //if there are too few players the game is paused
        if(connected.size()<MIN_PLAYERS)
        {
            if(connected.size()==1)
            {
                nextTurn();
            }
            this.lastGameState =this.gameState;
            this.gameState=GameState.PAUSE;
        }

        //if there are enough players to play and if the disconnected player was supposed to play, next turn
        else if(this.players.get(turn).equals(toDisconnect))
        {
            nextTurn();
        }

    }

    /**
     * handles the connection f a player previously disconnected
     * @param nickname is the nickname of the player to reconnect
     * @throws NoSuchPlayerException propagated
     */

    public void reconnect(String nickname) throws NoSuchPlayerException{
        Player toConnect=findPlayer(nickname);
        this.connected.add(toConnect);

        //if the player was disconnected before drawing a card and there is card left to draw, he is assigned a random card
        if(toConnect.getState()==PlayerState.DRAW)
        {
            if(cardLeftToDraw())
            {
               if(!this.resourceDeck.isEmpty()) {
                   try {
                       toConnect.drawCard(this.resourceDeck.drawTop());
                   } catch (EmptyDeckException ignored){}
               }

               else if(!this.goldDeck.isEmpty()) {
                   try {
                       toConnect.drawCard(this.goldDeck.drawTop());
                   } catch (EmptyDeckException ignored) {}
                  }

                else  if(!this.resourceDeck.getVisible().isEmpty()) {

                       Card[] t = (Card[]) resourceDeck.getVisible().toArray();
                       Random rand = new Random();
                       int index = rand.nextInt(t.length);
                   try {
                       toConnect.drawCard(this.resourceDeck.drawVisible(t[index]));
                   } catch (InvalidVisibleCardException ignored) {}

               }
                else {
                   Card[] t = (Card[]) goldDeck.getVisible().toArray();
                   Random rand = new Random();
                   int index = rand.nextInt(t.length);
                   try {
                       toConnect.drawCard(this.goldDeck.drawVisible(t[index]));
                   } catch (InvalidVisibleCardException ignored) {}
               }

               }

            }
       //if the game was in pause and now there are enough players to play, the game goes on
        if(this.gameState==GameState.PAUSE && this.connected.size()>=MIN_PLAYERS)
        {
            this.gameState=this.lastGameState;
        }

    }


    // Getters and Setters

    public List<Player> getWinners() {
        // Check if game has ended
        return new ArrayList<>(this.winners);
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(this.players);
    }
    public Set<Player> getConnectedPlayers() {
        return new HashSet<>(this.connected);
    }

    public int getTurn() {
        return turn;
    }

    public Deck getResourceDeck() {
        return resourceDeck;
    }

    public Deck getGoldDeck() {
        return goldDeck;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Objective[] getSharedObjectives() {
        return sharedObjectives.clone();
    }
}
