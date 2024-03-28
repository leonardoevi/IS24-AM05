package it.polimi.is24am05.model.card.side;

import java.util.*;

import it.polimi.is24am05.model.enums.element.Element;
import it.polimi.is24am05.model.enums.Corner;

import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.exceptions.card.InvalidCornerException;

import it.polimi.is24am05.model.playArea.PlayArea;

/**
 * Implemented by resource, gold and starter sides to enable polymorphism.
 */
public interface Side {
    /**
     * Checks if a corner is present on this side, i.e. a card can cover the corner.
     *
     * @param corner the corner to check.
     * @return true if the corner is present on this side, false otherwise.
     */
    boolean hasCorner(Corner corner);

    /**
     * Gets the elements inside the given corner of this side.
     * <p>
     * As the center counts as a corner, the return type is set to a list to support starter front sides that have
     * multiple elements at the center. All other sides will always return a list of length one.
     *
     * @param corner the corner to get.
     * @return the elements inside the given corner of this side.
     * @throws InvalidCornerException thrown if the corner is not present on this side, i.e. hasCorner returns false.
     */
    List<Element> getCorner(Corner corner) throws InvalidCornerException;

    /**
     * Gets the total points awarded after placing this side on the play area.
     * <p>
     * It defaults to 0 and is overwritten by resource and gold front sides.
     *
     * @param playArea the play area where this side was placed.
     * @return the total points awarded after placing this side on the play area.
     */
    default int getPlacementPoints(PlayArea playArea) {
        return 0;
    }

    Resource getSeed();
}
