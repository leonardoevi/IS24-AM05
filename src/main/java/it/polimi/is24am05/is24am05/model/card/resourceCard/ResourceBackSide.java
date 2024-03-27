package it.polimi.is24am05.is24am05.model.card.resourceCard;

import java.util.*;

import it.polimi.is24am05.is24am05.model.enums.element.Element;
import it.polimi.is24am05.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.is24am05.model.enums.Corner;

import it.polimi.is24am05.is24am05.model.card.side.Side;

/**
 * Resource back sides of the game.
 */
public enum ResourceBackSide implements Side {
    RBS_001(Resource.FUNGI),
    RBS_015(Resource.PLANT),
    RBS_028(Resource.ANIMAL);

    /**
     * Resource at the center of this side.
     */
    private final Resource center;

    /**
     * Constructor.
     *
     * @param center resource at the center of this side.
     */
    ResourceBackSide(Resource center) {
        this.center = center;
    }

    /**
     * Checks if a corner is present on this side, i.e. a card can cover the corner.
     * <p>
     * As all resource back sides have all corners, it always returns true.
     *
     * @param corner the corner to check.
     * @return true, see above.
     */
    @Override
    public boolean hasCorner(Corner corner) {
        return true;
    }

    /**
     * Gets the elements inside the given corner of this side.
     * <p>
     * As all resource back sides have all corners, no exception is ever thrown.
     *
     * @param corner the corner to get.
     * @return the element inside the given corner of this side.
     */
    @Override
    public List<Element> getCorner(Corner corner) {
        if (corner == Corner.CE)
            return List.of(center);
        return List.of();
    }
}
