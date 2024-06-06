package it.polimi.is24am05.model.game;

import it.polimi.is24am05.model.Player.Player;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.goldCard.GoldBackSide;
import it.polimi.is24am05.model.card.goldCard.GoldCard;
import it.polimi.is24am05.model.card.goldCard.GoldCardVisible;
import it.polimi.is24am05.model.card.resourceCard.ResourceCard;
import it.polimi.is24am05.model.card.resourceCard.ResourceCardVisible;
import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.card.starterCard.StarterCard;
import it.polimi.is24am05.model.deck.Deck;
import it.polimi.is24am05.model.enums.Color;
import it.polimi.is24am05.model.enums.element.Resource;
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
import it.polimi.is24am05.model.objective.ObjectiveDisplayer;
import it.polimi.is24am05.model.objective.SharedObjectiveDisplayer;
import it.polimi.is24am05.model.playArea.AreaDisplayer;
import it.polimi.is24am05.model.playArea.PlayArea;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.is24am05.model.Player.HandDisplayer.handToString;
import static it.polimi.is24am05.model.deck.DeckDisplayer.deckToString;

public class Game implements Serializable, Cloneable {
    /**
     * List of winners of the game, updated after game is ended.
     */
    private List<Player> winners = new ArrayList<>();
    /**
     * Players in the game. No player can be added.
     */
    private List<Player> players;


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
     * Used to track che state of the game, i.e. gameState, before the game is paused
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
    public final static int MAX_PLAYERS = Color.values().length;
    public final static int MIN_PLAYERS = 2;

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
    public synchronized void placeStarterSide(String playerName, Side side) throws InvalidStarterSideException, NoSuchPlayerException, MoveNotAllowedException {
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
    private void dealHandsAndObjectives() throws NoSuchPlayerException {
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

        // for each player still disconnected, choose  random objective
        for(String nickname: this.getDisconnected())
        {
            if(findPlayer(nickname).getState()==PlayerState.CHOOSE_OBJECTIVE)
            {
                System.out.println(nickname + " is disconnected, objective randomly chosen. (2)");
                chooseRandomObjective(nickname);
            }
        }


    }

    /**
     * Allows a player to choose the objective they wish to keep
     * @param playerName Player choosing
     * @param objective  Objective to be chosen
     * @throws MoveNotAllowedException If the current game state is not CHOOSE_OBJECTIVE
     * @throws NoSuchPlayerException If such a player is not in the game
     * @throws ObjectiveNotAllowedException If the player doesn't have that objective card
     */
    public synchronized void chooseObjective(String playerName, Objective objective) throws MoveNotAllowedException, NoSuchPlayerException, ObjectiveNotAllowedException {
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

        // If each player has chosen an objective card
        if(allHaveChosen) {
            for (Player p : players)
                p.setState(PlayerState.PLACE);

            //if there are enough players connected
            if(this.connected.size() >=2)
            {
                this.gameState=GameState.GAME;

                //if player that should play is disconnected, nextTurn()
                if(getDisconnected().contains(this.players.get(this.turn).getNickname())){
                    nextTurn();
                }
            }
            else{
                //it there are not enough players, the game is paused
                this.lastGameState = GameState.GAME;
                this.gameState = GameState.PAUSE;
            }





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
    public synchronized void placeSide(String playerName, Card card, Side side, int i, int j) throws MoveNotAllowedException, NoSuchPlayerException, NotYourTurnException, PlacementNotAllowedException, InvalidSideException, InvalidCoordinatesException, InvalidCardException, NoAdjacentCardException {
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
     * @throws MoveNotAllowedException If the current game state is not GAME or GAME_ENDING
     * @throws NoSuchPlayerException If such a player is not in the game
     * @throws NotYourTurnException If it is not the Player's turn
     * @throws EmptyDeckException propagated
     */
    public synchronized void drawDeck(String playerName, boolean fromGold) throws MoveNotAllowedException, NoSuchPlayerException, NotYourTurnException, EmptyDeckException {
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
     * @throws MoveNotAllowedException If the current game state is not GAME or GAME_ENDING
     * @throws NoSuchPlayerException If such a player is not in the game
     * @throws NotYourTurnException If it is not the Player's turn
     * @throws InvalidVisibleCardException propagated
     */
    public synchronized void drawVisible(String playerName, Card visible) throws MoveNotAllowedException, NoSuchPlayerException, NotYourTurnException, InvalidVisibleCardException{
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
     * <p>
     * If the following player is disconnected, immediately ends its turn too
     */
    private void nextTurn(){
        // If a player has reached 20 points
        if(players.stream().anyMatch(p -> p.getPoints() >= POINTS_TO_END))
            this.gameState = GameState.GAME_ENDING;

        // If only last turns must be played
        if(this.gameState == GameState.GAME_ENDING){
            // Increase the index
            this.turn += 1;
        } else
            // Increase the index, keeping it inside the boundary
            this.turn = (this.turn + 1) % players.size();

        // If the index is out of bound, the game has ended
        if(this.turn >= players.size()) {
            this.gameState = GameState.END;
            endGame();
            return;
        }

        // If the player that is expected to play now is disconnected, skip its turn
        if(!this.connected.contains(this.players.get(this.turn))){
            nextTurn();
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
     * if a player  disconnects before placing the starter Card, a random side is placed random
     * if a player is disconnects before choosing the objective card , a random objective card is chosen random
     * If a player is disconnects after placing a card but before drawing, a random card is drawn to end its turn correctly
     * @param nickname is the nickname of the player to disconnect
     * @throws NoSuchPlayerException propagated
     */
    public synchronized void disconnect(String nickname) throws NoSuchPlayerException {

        // If the player is already disconnected do nothing
        if(!this.connected.contains(findPlayer(nickname)))
            return;

        //DISCONNECTION PRE GAME

       //if a player disconnects before placing the starter card, a random side will be places
        if(findPlayer(nickname).getState() == PlayerState.PLACE_STARTER_CARD)
        {
            this.connected.remove(findPlayer(nickname));
            placeRandomStarterSide(nickname);
            return;

        }

        //if the players disconnect after all the other players have placed the starter card a random objective will be chosen
        // else the random objective for him will be chosen after all players have placed the starter card (see dealHeandAndObjectives()
        if(findPlayer(nickname).getState() == PlayerState.CHOOSE_OBJECTIVE)
        {
            this.connected.remove(findPlayer(nickname));

            if(gameState==GameState.CHOOSE_OBJECTIVE)
                System.out.println(nickname + " is disconnected, objective randomly chosen. (1)");
               chooseRandomObjective(nickname);

            return;
        }

         //DISCONNECTION DURING THE GAME

        // If the player is mid-turn during the game  make them draw a random card
        if(this.players.get(this.turn).getNickname().equals(nickname) && findPlayer(nickname).getState() == PlayerState.DRAW)
            this.drawSomething(nickname);

        // Remove the player from connected players
        this.connected.remove(findPlayer(nickname));

        // If exactly one player remains in the game after this disconnection and we are not in the pre game, pause the game
        if(this.connected.size() == 1){
            this.lastGameState = this.gameState;
            this.gameState = GameState.PAUSE;
        }

        // If the player disconnecting was expected to play and there are enough players to keep playing
        if(this.players.get(this.turn).getNickname().equals(nickname) && this.connected.size()>=2 ){
            nextTurn();
        }
    }

    /**
     * handles the connection f a player previously disconnected
     * @param nickname is the nickname of the player to reconnect
     * @throws NoSuchPlayerException propagated
     */
    public synchronized void reconnect(String nickname) throws NoSuchPlayerException{
        // If the player is already connected do nothing
        if(this.connected.contains(findPlayer(nickname)))
            return;

        this.connected.add(findPlayer(nickname));

        //RECONNECTION PRE GAME
        //if a players reconnect PRE GAME, nothing happens

        if(gameState==GameState.PLACE_STARTER_CARDS || gameState==GameState.CHOOSE_OBJECTIVE )
            return;

        //RECONNECTION DURING THE GAME
        // If there are enough players to play
        if(this.connected.size() == 2){

              this.gameState = this.lastGameState;
              this.lastGameState = null;


            // If the player that is expected to play is still not connected
            if(!this.connected.contains(this.players.get(this.turn)))
                nextTurn();
        }
    }
    private void placeRandomStarterSide(String nickname) {
        try {
            Card starterCard=findPlayer(nickname).getStarterCard();
            Side randomSide= new Random().nextInt(2) == 0 ? starterCard.getBackSide() : starterCard.getFrontSide();
             placeStarterSide(nickname, randomSide );
        } catch (NoSuchPlayerException | InvalidStarterSideException | MoveNotAllowedException ignored) {}


    }

    private void chooseRandomObjective(String nickname)
    {
        try {
            Objective[] objectivesHand=findPlayer(nickname).getObjectivesHand();
            Objective randomObjective=objectivesHand[new Random().nextInt(2)];
             chooseObjective(nickname, randomObjective );
        } catch (NoSuchPlayerException | MoveNotAllowedException | ObjectiveNotAllowedException ignored) {}

    }

    private void drawSomething(String nickname){
        Card toDraw = this.getResourceDeck().getVisible().stream().findAny().orElse(null);
        if(toDraw == null)
            toDraw = this.getGoldDeck().getVisible().stream().findAny().orElse(null);

        // If the player was expected to draw a card, something to draw must be left in one of the decks
        assert toDraw != null;

        try {
            this.drawVisible(nickname, toDraw);
        } catch (MoveNotAllowedException | InvalidVisibleCardException | NotYourTurnException |
                 NoSuchPlayerException ignored) {}
    }

    @Override
    public synchronized String toString(){
        StringBuilder stringBuilder = new StringBuilder("\n=============================");
        for(Player p : this.getPlayers()){
            stringBuilder.append("\nPlayer: ").append(p.getNickname()).append("\n");
            stringBuilder.append("\nState: ").append(p.getState()).append("\n");
            stringBuilder.append("Points: ").append(p.getPoints()).append("\n");

            stringBuilder.append("Play area: ").append("\n");
            stringBuilder.append(new AreaDisplayer(p.getPlayArea())).append("\n");

            if(p.getState() == PlayerState.PLACE_STARTER_CARD) {
                stringBuilder.append("Hand: ").append("\n");
                stringBuilder.append(handToString(List.of(p.getStarterCard()))).append("\n");
            }

            if(this.gameState != GameState.PLACE_STARTER_CARDS && this.gameState != GameState.CHOOSE_OBJECTIVE){
                if(p.getObjective() != null) {
                    Objective[] personalAndShared = List.of(p.getObjective(), getSharedObjectives()[0], getSharedObjectives()[1]).toArray(Objective[]::new);
                    stringBuilder.append(new SharedObjectiveDisplayer(personalAndShared).toString("Alesio")).append("\n");
                }
            }

            if(p.getHand() != null && !p.getHand().isEmpty()) {
                stringBuilder.append("Hand: ").append("\n");
                stringBuilder.append(handToString(p.getHand())).append("\n");
            }

            if(this.gameState == GameState.CHOOSE_OBJECTIVE)
                if(p.getObjectivesHand() != null) {
                    stringBuilder.append("\n")
                            .append("To choose from:\n")
                            .append(new SharedObjectiveDisplayer(p.getObjectivesHand()))
                            .append("\n");

                    stringBuilder.append("\n")
                            .append("Shared objectives:\n")
                            .append(new SharedObjectiveDisplayer(this.sharedObjectives))
                            .append("\n");
                }

        }

        if(this.gameState == GameState.GAME || this.gameState == GameState.GAME_ENDING) {
            stringBuilder.append("Decks: ").append("\n");
            stringBuilder.append(deckToString(this.getResourceDeck(), false, this.getGoldDeck(), true)).append("\n");
        }

        stringBuilder.append("Game State: " + this.gameState+ "\n");
        if(this.gameState == GameState.GAME || this.gameState == GameState.GAME_ENDING){
            stringBuilder.append(players.get(turn).getNickname()).append("'s turn.\n");
        }

        return stringBuilder.toString();
    }

    /**
     * returns a clone of the current game
     *
     * @throws CloneNotSupportedException If the clone does not work
     */
    protected Game clone() throws CloneNotSupportedException {
        Game cloned=(Game) super.clone();
        cloned.players=new ArrayList<>();

        for(Player p: this.players){

            cloned.players.add(p.clone());

        }

        return cloned;
    }
    /**
     * allows to display the game to a player with only necessary information about other players
     * @param nickname Player name
     * @throws NoSuchPlayerException If such a player is not in the game
     */
    public Game getPov(String nickname) throws NoSuchPlayerException {


        try {
            //copy of the current game
            Game gameToDisplay = this.clone();

            Player main = gameToDisplay.findPlayer(nickname); //Throws NoSuchPlayerException

            //obscure information about other players
            for (Player p : gameToDisplay.getPlayers()) {

                if(!p.getNickname().equals(nickname)) {
                    List<Card> cardToDisplay=new ArrayList<>();
                     for(Card c :p.getHand()) {

                       if(c.getBackSide() instanceof GoldBackSide)
                       {
                           if(c.getBackSide().getSeed().equals(Resource.INSECT))
                               cardToDisplay.add(GoldCardVisible.GCV_I);
                           if(c.getBackSide().getSeed().equals( Resource.ANIMAL))
                               cardToDisplay.add(GoldCardVisible.GCV_A);
                           if(c.getBackSide().getSeed().equals( Resource.FUNGI))
                               cardToDisplay.add(GoldCardVisible.GCV_F);
                           if(c.getBackSide().getSeed().equals(Resource.PLANT))
                               cardToDisplay.add(GoldCardVisible.GCV_P);

                       }
                       else{
                           if(c.getBackSide().getSeed().equals(Resource.INSECT))
                               cardToDisplay.add(ResourceCardVisible.RCV_I);
                           if(c.getBackSide().getSeed().equals(Resource.ANIMAL))
                               cardToDisplay.add(ResourceCardVisible.RCV_A);
                           if(c.getBackSide().getSeed().equals( Resource.FUNGI))
                               cardToDisplay.add(ResourceCardVisible.RCV_F);
                           if(c.getBackSide().getSeed().equals( Resource.PLANT))
                               cardToDisplay.add(ResourceCardVisible.RCV_P);

                       }
                       //obscure information about the cloned player in the cloned game
                       p.obscure(cardToDisplay);

                     }
                }
            }
        return gameToDisplay;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    // Getters and Setters

    public synchronized List<Player> getWinners() {
        // Check if game has ended
        return new ArrayList<>(this.winners);
    }

    public synchronized List<Player> getPlayers() {
        return new ArrayList<>(this.players);
    }
    public synchronized Set<Player> getConnectedPlayers() {
        return new HashSet<>(this.connected);
    }

    public synchronized int getTurn() {
        return turn;
    }

    public synchronized Deck getResourceDeck() {
        return resourceDeck;
    }

    public synchronized Deck getGoldDeck() {
        return goldDeck;
    }

    public synchronized GameState getGameState() {
        return gameState;
    }

    public synchronized Objective[] getSharedObjectives() {
        return sharedObjectives.clone();
    }

    public synchronized Set<String> getDisconnected(){
        return this.players.stream()
                    .filter(p -> !this.connected.contains(p))
                    .map(Player::getNickname)
                    .collect(Collectors.toSet());
    }

    public synchronized Set<String> getNicknames(){
        return this.players.stream().map(Player::getNickname).collect(Collectors.toSet());
    }

    /**
     * Returns the starter card of a player
     * @param playerNickname player nickname
     * @return Its starter card
     * @throws NoSuchPlayerException if the player is not in the game
     */
    public synchronized Card getStarterCard(String playerNickname) throws NoSuchPlayerException {
        Player player = findPlayer(playerNickname);
        return player.getStarterCard();
    }

    /**
     * Gets the playArea of the given player.
     * @param nickname given player's nickname.
     * @return the playArea of the given player.
     * @throws NoSuchPlayerException if the given player is not in the game.
     */
    public synchronized PlayArea getPlayArea(String nickname) throws NoSuchPlayerException {
        Player player = findPlayer(nickname);
        return player.getPlayArea();
    }

    /**
     * Gets the points of the given player.
     * @param nickname given player's nickname.
     * @return the points of the given player.
     * @throws NoSuchPlayerException if the given player is not in the game.
     */
    public synchronized int getPoints(String nickname) throws NoSuchPlayerException {
        Player player = findPlayer(nickname);
        return player.getPoints();
    }

    /**
     * Gets the hand of the given player.
     * @param nickname given player's nickname.
     * @return the hand of the given player.
     * @throws NoSuchPlayerException if the given player is not in the game.
     */
    public synchronized List<Card> getHand(String nickname) throws NoSuchPlayerException {
        Player player = findPlayer(nickname);
        return player.getHand();
    }

    /**
     * Gets the blurred hand of the given player.
     * @param nickname given player's nickname.
     * @return the playArea of the given player.
     * @throws NoSuchPlayerException if the given player is not in the game.
     */
    public synchronized List<Resource> getBlurredHand(String nickname) throws NoSuchPlayerException {
        Player player = findPlayer(nickname);
        return player.getBlurredHand();
    }

    public synchronized PlayerState getPlayerState(String nickname) throws NoSuchPlayerException {
        Player player = findPlayer(nickname);
        return player.getState();
    }
}
