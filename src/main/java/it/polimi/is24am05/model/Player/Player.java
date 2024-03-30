package it.polimi.is24am05.model.Player;

import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.card.starterCard.StarterCard;
import it.polimi.is24am05.model.enums.Color;
import it.polimi.is24am05.model.enums.state.PlayerState;
import it.polimi.is24am05.model.exceptions.playArea.InvalidCoordinatesException;
import it.polimi.is24am05.model.exceptions.playArea.NoAdjacentCardException;
import it.polimi.is24am05.model.exceptions.playArea.PlacementNotAllowedException;
import it.polimi.is24am05.model.objective.Objective;
import it.polimi.is24am05.model.playArea.PlayArea;
import it.polimi.is24am05.model.playArea.Tuple;

import java.util.ArrayList;
import java.util.List;

public class Player {

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
     * This attribute keeps track of the cards in the player's hand
     */
    private List<Card> hand;

    /**
     * each player has playArea->view class PlayArea
     */
    private PlayArea playArea;

    /**
     * This attribute keeps track of the two Objectives Cards in the player's hand
     */
    private Objective[] objectivesHand;

    /**
     * This attribute keeps track of the  ObjectiveCard chosen by the player
     */
    private Objective objective;

    /**
     * Initializes a player with his nickname and Color of the token
     */

    Player(String nickname, Color color) {
        this.nickname = nickname;
        this.color = color;
        this.hand = new ArrayList<Card>();

        this.points = 0;
        this.playArea = new PlayArea();
        this.objectivesHand = new Objective[2];
        this.objective = null;
    }


    /**
     * places the side of the starter card chosen by the player in his playArea
     *
     * @param starterSide is the Side of the starter Card chosen by the player
     * @throws InvalidCoordinatesException  If the specified coordinates are not "white"
     * @throws NoAdjacentCardException      If the specified coordinates are not diagonally adjacent to any card
     * @throws PlacementNotAllowedException If a neighbouring card doesn't allow the placement
     *                                      since it's the first card to be placed in the playArea, and the methods force the placement in coordinates (0,0)
     *                                      exceptions can't happne
     */

    public void dealStarterCard(Side starterSide) throws PlacementNotAllowedException, InvalidCoordinatesException, NoAdjacentCardException {
        playArea.playSide(starterSide, new Tuple(0, 0));
    }


    /**
     * initialize the hand of the player
     *
     * @param hand is the List of Card given to the player at the beginning of the game
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
     * initialize the attribute obective
     *
     * @param objective is the objective card chosen between the 2 objectives card by the player
     */
    public void choseObjective(Objective objective) {
        this.objective = objective;
    }

    /**
     * places the side of a card in the player's playArea at coordinates (i,j)
     *
     * @param side is the Side of the Card to be positioned in the playArea
     * @param i    is the first coordinate
     * @param j    is the second coordinate
     * @throws InvalidCoordinatesException  If the specified coordinates are not "white"
     * @throws NoAdjacentCardException      If the specified coordinates are not diagonally adjacent to any card
     * @throws PlacementNotAllowedException If a neighbouring card doesn't allow the placement
     */
    public void playSide(Side side, int i, int j) throws PlacementNotAllowedException, InvalidCoordinatesException, NoAdjacentCardException {

        points += this.playArea.playSide(side, new Tuple(i, j));

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
     * state Setter
     */
    public void setState(PlayerState state)
    {
        this.state=state;

    }


}
