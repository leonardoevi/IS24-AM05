package it.polimi.is24am05.model.playArea;

import it.polimi.is24am05.model.card.side.PlacedSide;
import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.card.starterCard.StarterBackSide;
import it.polimi.is24am05.model.card.starterCard.StarterFrontSide;
import it.polimi.is24am05.model.enums.Corner;
import it.polimi.is24am05.model.enums.element.Element;
import it.polimi.is24am05.model.enums.element.Item;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.exceptions.card.InvalidCornerException;
import it.polimi.is24am05.model.exceptions.playArea.InvalidCoordinatesException;
import it.polimi.is24am05.model.exceptions.playArea.NoAdjacentCardException;
import it.polimi.is24am05.model.exceptions.playArea.PlacementNotAllowedException;

import java.util.*;

/**
 * The PlayArea is a matrix of Cards.
 * Cards cannot be placed next to one another.
 * If we imagine the PlayArea as a chessboard, Cards can only be placed on "white" cells.
 */
public class PlayArea {
    /**
     * The PlayArea is represented as a Map.
     * It maps Coordinates (i, j) to the Corresponding PlayedSide
     * This way, it is easier to handle Card Placements.
     */
    private final Map<Tuple, PlacedSide> playArea;

    /**
     * This attribute keeps track of the number of visible elements on the PlayArea
     */
    private final Map<Element, Integer> visibleElements;

    /**
     * The frontier is the set of empty Coordinates that are neighbours to an existing card.
     */
    private final Set<Tuple> frontier;

    /**
     * This attribute keeps track of the coordinates where it is no longer possible to place a card.
     * This might happen when a card without a corner is placed.
     */
    private final Set<Tuple> blocked;

    /**
     * This counter is increased each time a card is placed.
     * It is needed to keep track of the order of card placements.
     */
    private int turnCount;

    /**
     * The following 4 attributes keep track of the boundaries of the play area.
     * They are needed to turn the Map into an actual Matrix
     */
    private int minI, minJ, maxI, maxJ;

    /**
     * Initializes an empty Play Area
     */
    public PlayArea() {
        // Initialize playArea to an empty map
        this.playArea = new HashMap<>();

        // Initialize element counter to zero
        this.visibleElements = new HashMap<>();
        for(Resource r : Resource.values())
            this.visibleElements.put(r,0);
        for(Item i : Item.values())
            this.visibleElements.put(i,0);

        // Initialize frontier
        this.frontier = new HashSet<>();
        this.frontier.add(new Tuple(0,0));

        // Initialize set of blocked coordinates
        this.blocked = new HashSet<>();

        // Initialize turn counter
        this.turnCount = 0;

        // Initialize min max values
        minI = 0; minJ = 0; maxI = 0; maxJ = 0;
    }


    /**
     * @param coord Set of coordinates to check
     * @return True if the specified set of coordinates correspond to a "white" cell in the chessboard
     */
    private static boolean isWhite(Tuple coord){
        return Math.abs(coord.i%2) == Math.abs(coord.j%2);
    }

    /**
     * @param coord A "white" set of coordinates
     * @return The four "white" coordinates diagonally adjacent to param
     * @throws InvalidCoordinatesException If param doesn't correspond to a "white" set of coordinates
     */
    public static Tuple[] getNeighbours(Tuple coord) throws InvalidCoordinatesException {
        // Check for invalid coordinates
        if(!isWhite(coord))
            throw new InvalidCoordinatesException();

        // Allocate array of neighbours
        Tuple[] neighbours = new Tuple[4];

        // Fill the array
        neighbours[0] = new Tuple( coord.i -1, coord.j -1);
        neighbours[1] = new Tuple( coord.i -1, coord.j +1);
        neighbours[2] = new Tuple( coord.i +1, coord.j +1);
        neighbours[3] = new Tuple( coord.i +1, coord.j -1);

        return neighbours;
    }

    /**
     * @param coord starting coordinates
     * @param corner direction of adjacency
     * @return Returns the coordinates diagonally adjacent in the specified direction
     */
    private static Tuple getNeighbour(Tuple coord, Corner corner){
        switch (corner){
            case NW -> {
                return new Tuple(coord.i -1, coord.j -1);
            }
            case NE -> {
                return new Tuple(coord.i -1, coord.j +1);
            }
            case SE -> {
                return new Tuple(coord.i +1, coord.j +1);
            }
            case SW -> {
                return new Tuple(coord.i +1, coord.j -1);
            }
            // Should never happen
            case null, default -> {
                return coord;
            }
        }
    }

    /**
     * Updates the frontier, adding the empty "white" cells adjacent to the last added card
     * @param lastAdd Coordinates of the last added Card
     * @throws InvalidCoordinatesException If the specified coordinates are not "white"
     */
    private void updateFrontier(Tuple lastAdd) throws InvalidCoordinatesException {
        // Check for invalid coordinates
        if(!isWhite(lastAdd))
            throw new InvalidCoordinatesException();

        // Check if lastAdd is in the playArea
        if(!this.playArea.containsKey(lastAdd))
            return;

        // Update the frontier
        frontier.remove(lastAdd);
        for(Tuple neighbour : getNeighbours(lastAdd))
            if(!playArea.containsKey(neighbour))
                frontier.add(neighbour);
    }

    /**
     * After placing the card at the specified coordinates, updates the count of visible Elements
     * @param coord Coordinates of the card that was last added
     * @throws InvalidCoordinatesException If the specified coordinates are not "white"
     */
    private void updateVisibleElements(Tuple coord) throws InvalidCoordinatesException {
        // Get card neighbours
        Tuple[] neighbours = getNeighbours(coord);

        // For each neighbouring set of coordinates
        for(Tuple neighbour : neighbours){
            // If there is a card
            if(this.playArea.containsKey(neighbour)){
                Corner covered = getCoveredCorner(neighbour, coord);
                try {
                    // Remove the element that the card is covering
                    Element toRemove = this.playArea.get(neighbour).getSide().getCorner(covered).getFirst();
                    int count = this.visibleElements.get(toRemove);
                    this.visibleElements.put(toRemove, count - 1);

                    // If the neighbour does not have the corner or the corner is empty, do nothing
                } catch (NoSuchElementException | InvalidCornerException e) {
                    continue;
                }
            }
        }

        // Add the Resources that the Side is showing
        Side lastAdd = this.playArea.get(coord).getSide();
        for(Corner corner : Corner.values()){
            try {
                List<Element> elementsToAdd = lastAdd.getCorner(corner);
                for(Element toAdd : elementsToAdd){
                    int count = this.visibleElements.get(toAdd);
                    this.visibleElements.put(toAdd, count + 1);
                }
                //  If the card does not have the corner, do nothing
            } catch (InvalidCornerException e) {
                continue;
            }
        }
    }

    /**
     * Returns the corner that the top card is covering
     * @param bottom Coordinates of the card that is covered
     * @param top Coordinates of the card on top
     * @return The corner of the Bottom Card, that the Top card is covering
     * @throws InvalidCoordinatesException If the two Cards are not adjacent
     */
    public static Corner getCoveredCorner(Tuple bottom, Tuple top) throws InvalidCoordinatesException{
        // If the two coordinates are not adjacent
        if(Arrays.stream(getNeighbours(top)).noneMatch(x -> x.equals(bottom)))
            throw new InvalidCoordinatesException();

        int deltaI = top.i - bottom.i;
        int deltaJ = top.j - bottom.j;

        if(deltaI > 0) {
            if (deltaJ > 0)
                return Corner.SE;
            else
                return Corner.SW;
        } else {
            if (deltaJ > 0)
                return Corner.NE;
            else
                return Corner.NW;
        }
    }

    /**
     * Adds the specified card to the PlayArea
     * @param side Side of the card to play
     * @param coord Coordinates of the card to play
     * @throws InvalidCoordinatesException If the specified coordinates are not "white"
     * @throws NoAdjacentCardException If the specified coordinates are not diagonally adjacent to any card
     * @throws PlacementNotAllowedException If a neighbouring card doesn't allow the placement
     */
    public int playSide(Side side, Tuple coord) throws InvalidCoordinatesException, NoAdjacentCardException, PlacementNotAllowedException {
        // Check for invalid coordinates
        if(!isWhite(coord))
            throw new InvalidCoordinatesException();

        // Check if coordinates are in the frontier
        if(!this.frontier.contains(coord))
            throw new NoAdjacentCardException();

        // Check if the first card is a Starting Card
        if(this.playArea.keySet().isEmpty() && !(side instanceof StarterFrontSide || side instanceof StarterBackSide))
            throw new PlacementNotAllowedException();

        // Check if coordinates are blocked
        if(this.blocked.contains(coord))
            throw new PlacementNotAllowedException();

        // Place Card
        PlacedSide toPlace = new PlacedSide(side, this.turnCount);
        this.playArea.put(coord, toPlace);

        // Update min max values
        minI = Integer.min(coord.i, minI);
        minJ = Integer.min(coord.j, minJ);
        maxI = Integer.max(coord.i, maxI);
        maxJ = Integer.max(coord.j, maxJ);

        // Update Frontier
        updateFrontier(coord);

        // Update visibleElements
        updateVisibleElements(coord);

        // Update blocked positions
        for(Corner c : Corner.getEdges()) {
            // If the card does not have a corner and the neighbouring cell is empty
            if (!side.hasCorner(c) && !this.playArea.containsKey(getNeighbour(coord, c)))
                // No card can be placed in that cell
                this.blocked.add(getNeighbour(coord, c));
        }

        // Increase turn count
        this.turnCount += 1;

        // Return points given by the card placement
        return side.getPlacementPoints(this);
    }

    /**
     * @param coord Coordinates to check
     * @return True if a Card can be placed at the specified Coordinates
     */
    public boolean isFree(Tuple coord){
        return this.frontier.contains(coord) && !this.blocked.contains(coord);
    }

    /**
     * @param coord Coordinates to check
     * @return True if a Card occupies the specified Coordinates
     */
    public boolean isOccupied(Tuple coord){
        return this.playArea.containsKey(coord);
    }

    /**
     * PlayArea Getter
     * @return attribute playArea
     */
    public Map<Tuple, PlacedSide> getPlayArea() {
        return new HashMap<>(this.playArea);
    }

    /**
     * @return The PlayArea, represented as a Matrix of PlayedSides.
     */
    public PlacedSide[][] getMatrixPlayArea(){
        // Start with a shift of zero
        int iShift = 0;
        int jShift = 0;

        // Calculate necessary i and j Shifts, such that no card has a negative i or j coordinate
        // Make sure that the coordinates are "white" by increasing shifts by 2 each time
        while(this.minI + iShift < 0)
            iShift += 2;

        while (this.minJ + jShift < 0)
            jShift += 2;

        // Allocate Matrix
        PlacedSide[][] area = new PlacedSide[this.maxI + iShift + 1][this.maxJ + jShift + 1];

        // Fill up the matrix
        // For each Card in the PlayArea
        for(Tuple coord : this.playArea.keySet()){
            // Put the Card in the matrix, shifted by the previously calculated amount
            area[coord.i + iShift][coord.j + jShift] = playArea.get(coord);
        }

        // Return the matrix
        return area;
    }

    /**
     * Visible Elements getter
     * @return attribute visibleElements
     */
    public Map<Element, Integer> getVisibleElements() {
        return visibleElements;
    }

    /**
     * @return The set of coordinates where it is possible to place a Card
     */
    public Set<Tuple> getFrontier() {
        Set<Tuple> retVal = new HashSet<>(frontier);
        retVal.removeAll(blocked);
        return retVal;
    }
}
