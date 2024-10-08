package it.polimi.is24am05.model.Player;

import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.card.starterCard.StarterCard;
import it.polimi.is24am05.model.enums.Color;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.enums.state.PlayerState;
import it.polimi.is24am05.model.exceptions.game.NoSuchPlayerException;
import it.polimi.is24am05.model.exceptions.playArea.InvalidCoordinatesException;
import it.polimi.is24am05.model.exceptions.playArea.NoAdjacentCardException;
import it.polimi.is24am05.model.exceptions.playArea.PlacementNotAllowedException;
import it.polimi.is24am05.model.exceptions.player.*;
import it.polimi.is24am05.model.objective.Objective;
import it.polimi.is24am05.model.playArea.PlayArea;
import it.polimi.is24am05.model.playArea.Tuple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable, Cloneable {

    /**
     * nickname chosen by the player, it can't change during the game
     */
    private final String nickname;

    /**
     * Color of the token chosen by the player, it can't change during the game
     */
    private final Color color;

    /**
     * Since the state of the Player changes continually during the game, this attribute keep track of the current state
     */
    private PlayerState state;

    /**
     * This attribute keeps track of points accumulated by the player
     */
    private int points;


    /**
     * This attribute keeps track of the starter card
     */
    private StarterCard starterCard;
    /**
     * This attribute keeps track of the cards in the player's hand
     */
    private List<Card> hand;

    /**
     * each player has a playArea where they can place the cards
     */
    private final PlayArea playArea;

    /**
     * This attribute keeps track of the two Objectives Cards in the player's hand
     */
    private Objective[] objectivesHand;

    /**
     * This attribute keeps track of the  ObjectiveCard chosen by the player
     */
    private Objective objective;

    private int satisfiedObjectiveCards = 0;

    /**
     * Initializes a player with his nickname and Color of the token
     */

    public Player(String nickname, Color color) {
        this.nickname = nickname;
        this.color = color;
        this.hand = new ArrayList<Card>();
        this.points = 0;
        this.playArea = new PlayArea();
        this.objectivesHand = new Objective[2];
        this.objective = null;
        this.state = PlayerState.PLACE_STARTER_CARD;
    }

    /**
     * initialize the starter card of the player
     *
     * @param starterCard is the starterCard given to the player
     */
    public void dealStarterCard(StarterCard starterCard) {
        this.starterCard=starterCard;
    }

    /**
     * initialize the hand of the player
     *
     * @param hand is the List of 3 Cards given to the player at the beginning of the game
     */

    public void dealHand(List<Card> hand) {
        this.hand = hand;
    }

    /**
     * initialize the objectivesHand of the player
     *
     * @param objectivesHand is the array of 2 objective cards given to the player at the beginning of the game
     */

    public void dealObjectives(Objective[] objectivesHand) {
        this.objectivesHand = objectivesHand;
    }

    /**
     * initialize the attribute objective
     *
     * @param objective is the objective card chosen between the 2 objectives card by the player
     * @throws ObjectiveNotAllowedException  if the player doesn't have that objective card
     *
     */
    public void chooseObjective(Objective objective) throws ObjectiveNotAllowedException {
        if(!this.objectivesHand[0].equals(objective) && !this.objectivesHand[1].equals(objective))
            throw new ObjectiveNotAllowedException();

        this.objective = objective;
    }

    /**
     * places the side of the starter card chosen by the player in his playArea
     *
     * @param starterSide is the Side of the starter Card chosen by the player
     * @throws InvalidStarterSideException  If the side is not associated with the starterCard of the player
     *
     */

    public void playStarterCard(Side starterSide) throws InvalidStarterSideException {

        if(!starterCard.getBackSide().equals(starterSide) && !starterCard.getFrontSide().equals(starterSide))
            throw new InvalidStarterSideException();
        try {
            playArea.playSide(starterSide, new Tuple(0, 0));
        }
        catch( InvalidCoordinatesException | NoAdjacentCardException |
               PlacementNotAllowedException ignored) {}
    }

    /**
     * places the side of the card in the player's playArea at coordinates (i,j)
     *
     * @param card is the card to be played  chosen by the player
     * @param side is the Side of the card the player wants to place
     * @param i    is the first coordinate
     * @param j    is the second coordinate
     * @throws InvalidCardException  If the player's hand doesn't contain Card
     * @throws InvalidSideException  If the side is not associated with the card
     * @throws InvalidCoordinatesException  If the specified coordinates are not "white"
     * @throws NoAdjacentCardException      If the specified coordinates are not diagonally adjacent to any card
     * @throws PlacementNotAllowedException If a neighbouring card doesn't allow the placement
     */
    public void playSide(Card card, Side side, int i, int j) throws InvalidCardException, InvalidSideException, PlacementNotAllowedException, InvalidCoordinatesException, NoAdjacentCardException  {

        if(!hand.contains(card))
            throw new InvalidCardException();
        if(!card.getFrontSide().equals(side) && !card.getBackSide().equals(side))
            throw new InvalidSideException();

        points += this.playArea.playSide(side, new Tuple(i, j));
        this.hand.remove(card);

    }
    /**
     * add the drawn card to the player's hand
     *
     * @param card is card drawn by the player
     */
    public void drawCard(Card card)
    {
        hand.add(card);
    }


    /**
     * nickname Getter
     *
     * @return attribute nickname
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * color Getter
     *
     * @return attribute color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * State Getter
     *
     * @return attribute State
     */
    public PlayerState getState() {
        return this.state;
    }

    /**
     * points Getter
     *
     * @return attribute points
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * hand Getter
     *
     * @return a copy of the attribute hand
     */
    public List<Card> getHand() {
        return new ArrayList<>(this.hand);
    }

    /**
     * playArea Getter
     *
     * @return the attribute playArea
     */
    public PlayArea getPlayArea() {
        return this.playArea;
    }

    /**
     * objectivesHand Getter
     *
     * @return the attribute objectivesHand
     */
    public Objective[] getObjectivesHand() {
        return this.objectivesHand;
    }

    /**
     * objective Getter
     *
     * @return the attribute objective
     */
    public Objective getObjective() {
        return this.objective;
    }

    /**
     * starterCard Getter
     *
     * @return the attribute starterCard
     */
    public StarterCard getStarterCard() {
        return this.starterCard;
    }

    public int getSatisfiedObjectiveCards() {
        return satisfiedObjectiveCards;
    }

    /**
     * state Setter
     */
    public void setState(PlayerState state)
    {
        this.state=state;

    }


    /**
     * Evaluates the objectives for the player, both private and shared.
     * Increases the player points accordingly.
     * Sets the number of objectiveCard that the player has satisfied.
     */
    public void evaluateObjectives(Objective[] sharedObjectives) {
        int objectivePoints = 0;
        // Evaluate shared objectives
        for (Objective o : sharedObjectives) {
            objectivePoints = o.evaluate(this.getPlayArea());
            if (objectivePoints > 0) {
                this.satisfiedObjectiveCards += 1;
                this.points += objectivePoints;
                objectivePoints = 0;
            }
        }

        // Evaluate player objective
        objectivePoints = this.objective.evaluate(this.getPlayArea());
        if (objectivePoints > 0) {
            this.satisfiedObjectiveCards += 1;
            this.points += objectivePoints;
        }
    }
    /**
     * returns a clone of the player
     */
    public Player clone()
    {
        try {
            Player cloned=(Player) super.clone();
            return cloned;

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Hides the player's hand from other players and obscure player's objective hand and objective
     *
     * @param handToDisplay is the hand of only back side cards  that can be shown to other players
     */
       public void obscure(List<Card> handToDisplay)
        {
            this.hand=handToDisplay;
            this.objectivesHand=null;
            this.objective=null;
        }




    /**
     * Gets the blurred hand of this player, i.e. the resources on the backsides.
     * @return the blurred hand of this player, i.e. the resources on the backsides.
     */
    public List<Resource> getBlurredHand() {
        return hand.stream().map(Card::getBackSide).map(Side::getSeed).toList();
    }
}
