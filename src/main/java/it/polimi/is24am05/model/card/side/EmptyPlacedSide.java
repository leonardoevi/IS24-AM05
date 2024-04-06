package it.polimi.is24am05.model.card.side;

import it.polimi.is24am05.model.playArea.Tuple;

/**
 * Class Used only for PlayArea display, allocated exclusively by the getMatrixPlayArea() method
 * Represents an empty spot in the PlayArea, where a Card might be placed
 * Overrides some methods to make sure it does not interfere with other code (LayoutMultiplier compute() method)
 */
public class EmptyPlacedSide extends PlacedSide{
    /**
     * Constructor.
     *
     * @param actualCoord coordinates of the card in the playArea
     */
    public EmptyPlacedSide(Tuple actualCoord) {
        super(null, -1, actualCoord);
    }
}
