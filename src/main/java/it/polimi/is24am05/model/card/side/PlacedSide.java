package it.polimi.is24am05.model.card.side;

import it.polimi.is24am05.model.playArea.Tuple;

/**
 * Wrapper of a placed Side.
 */
public class PlacedSide {
    /**
     * Placed side.
     */
    private final Side side;
    /**
     * Turn counter at the time of the side being placed.
     * <p>
     * As new sides must be placed on top of old ones, the side with the higher timestamp covers the one with the lower.
     */
    private final int turnCount;

    private final Tuple actualCoord;

    /**
     * Constructor.
     *
     * @param side placed side.
     * @param turnCount turn counter at the time of the side being placed.
     * @param actualCoord coordinates of the card in the playArea
     */
    public PlacedSide(Side side, int turnCount, Tuple actualCoord) {
        this.side = side;
        this.turnCount = turnCount;
        this.actualCoord = actualCoord;
    }

    /**
     * Gets the placed side.
     *
     * @return the placed side.
     */
    public Side getSide() {
        return side;
    }

    /**
     * Gets the turn counter at the time of the side being placed.
     *
     * @return the turn counter at the time of the side being placed.
     */
    public int getTurnCount() {
        return turnCount;
    }

    public Tuple getActualCoord() {
        return actualCoord;
    }
}
