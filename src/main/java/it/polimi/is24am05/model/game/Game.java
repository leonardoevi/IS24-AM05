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

import java.util.*;

public class Game {
    /**
     * List of winners of the game, updated after game is ended.
     */
    private List<Player> winners = new ArrayList<>();
    /**
     * Players in the game. No player can be added.
     */
    private final List<Player> players;

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
     * @param expected Expected game state
     * @throws MoveNotAllowedException If the current game state is different from the expected game state
     */
    private void checkState(GameState expected) throws MoveNotAllowedException {
        if(this.gameState != expected)
            throw new MoveNotAllowedException("Current game state different from expected");
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

        // Find the player and place the card
        Player toPlay = findPlayer(playerName);
        toPlay.playStarterCard(side);

        // If every player has placed their card
        boolean allHavePlaced = true;
        for(Player p : this.players)
            allHavePlaced = allHavePlaced && p.hasPlacedStarterCard;

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

        // Choose the objective
        player.chooseObjective(objective);

        // If all players have chosen their objective
        boolean allHaveChosen = true;
        for(Player p : this.players)
            allHaveChosen = allHaveChosen && p.hasChosenObjective;

        // It is time to start playing
        if(allHaveChosen)
            this.gameState = GameState.GAME;

    }

    /**
     * Allows a player to place a card in their play area
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
        checkState(GameState.GAME);     // throws MoveNotAllowedException

        // Find the player
        Player player = findPlayer(playerName);     // throws NoSuchPlayerException

        // Check if it's time for the player to place a Card
        if(player.playerState != PlayerState.PLACE)
            throw new MoveNotAllowedException("It is time to place a card");

        // Check if it's the Player turn
        if(this.players.get(turn) != player){
            throw new NotYourTurnException();
        }

        // Place the card
        player.playSide(card, side, i, j);  // throws InvalidCardException
                                            // InvalidSideException
                                            // PlacementNotAllowedException
                                            // InvalidCoordinatesException
                                            // NoAdjacentCardException

        // Time to draw a card
        player.playerState = PlayerState.DRAW;
    }

    // WE MIGHT WANT TO HANDLE THE CASE WHERE THERE IS NO MORE CARDS LEFT TO DRAW

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
        checkState(GameState.GAME);     // throws MoveNotAllowedException

        // Find the player
        Player player = findPlayer(playerName);     // throws NoSuchPlayerException

        // Check if it is time for the player to draw
        if(player.getState() != PlayerState.DRAW)
            throw new MoveNotAllowedException("It is time to draw a Card");

        // Check if it's the Player turn
        if(this.players.get(turn) != player){
            throw new NotYourTurnException();
        }

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
        player.playerState = PlayerState.PLACE;
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
    public void drawVisible(String playerName, Card visible) throws MoveNotAllowedException, NoSuchPlayerException, NotYourTurnException, InvalidVisibleCardException {
        // Check if the current game state is GAME
        checkState(GameState.GAME);     // throws MoveNotAllowedException

        // Find the player
        Player player = findPlayer(playerName);     // throws NoSuchPlayerException

        // Check if it's the Player turn
        if(this.players.get(turn) != player){
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
        player.playerState = PlayerState.PLACE;
        nextTurn();

    }

    /**
     * Ends the current player turn.
     * If a player has reached POINTS_TO_END points it allows each player left to play,
     * such that everyone will have played the same number of turns.
     * After everyone has played the last turn, it ends the game.
     */
    private void nextTurn(){
        // If a Player has reached 20 points
        if( players.stream().anyMatch(player -> player.getPoints() >= POINTS_TO_END) ){
            // Increase the index
            this.turn += 1;
        } else
            // Increase the index, keeping it inside the boundary
            this.turn = (this.turn + 1) % players.size();

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

        int winnerPoints = ranking.getFirst().getPoints();
        int winnerObjectives = ranking.getFirst().getSatisfiedObjectiveCards();

        this.winners = ranking.stream()
                        .filter(p -> p.getPoints() == winnerPoints && p.getSatisfiedObjectiveCards() == winnerObjectives)
                        .toList();
    }

    // Getters and Setters

    public List<Player> getWinners() {
        // Check if game has ended
        return new ArrayList<>(this.winners);
    }
}
