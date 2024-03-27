package it.polimi.is24am05.is24am05.model.card.resourceCard;

import java.util.*;

import it.polimi.is24am05.is24am05.model.enums.element.Element;
import it.polimi.is24am05.is24am05.model.enums.Corner;

import it.polimi.is24am05.is24am05.model.card.side.Side;
import it.polimi.is24am05.is24am05.model.enums.element.Item;
import it.polimi.is24am05.is24am05.model.enums.element.Resource;

import it.polimi.is24am05.is24am05.model.exceptions.card.InvalidCornerException;

import it.polimi.is24am05.is24am05.model.playArea.PlayArea;

/**
 * Resource front sides.
 */
public enum ResourceFrontSide implements Side {
    RFS_001(
        Map.of(
            Corner.NW, Optional.of(Resource.FUNGI),
            Corner.NE, Optional.empty(),
            Corner.SW, Optional.of(Resource.FUNGI)
        ), 0
    ),
    RFS_015(
        Map.of(
            Corner.NE, Optional.of(Resource.INSECT),
            Corner.SE, Optional.of(Resource.PLANT),
            Corner.SW, Optional.of(Item.QUILL)
        ), 0
    ),
    RFS_028(
        Map.of(
            Corner.NE, Optional.empty(),
            Corner.SE, Optional.empty(),
            Corner.SW, Optional.of(Resource.ANIMAL)
        ), 1
    );

    /**
     * Maps corners of this side to their element.
     * <p>
     * Corners not present on this side are not in the map, while empty corners map to empty optionals.
     */
    private final Map<Corner, Optional<Element>> corners;
    /**
     * Points awarded after placing this side on the play area.
     */
    private final int points;

    /**
     * Constructor.
     *
     * @param corners maps corners of this side to their element.
     * @param points points awarded after placing this side on the play area.
     */
    ResourceFrontSide(Map<Corner, Optional<Element>> corners, int points) {
        this.corners = corners;
        this.points = points;
    }

    /**
     * Checks if a corner is present on this side, i.e. a card can cover the corner.
     *
     * @param corner the corner to check.
     * @return true if the corner is present on this side, false otherwise.
     */
    @Override
    public boolean hasCorner(Corner corner) {
        return corners.containsKey(corner);
    }

    /**
     * Gets the elements inside the given corner of this side.
     *
     * @param corner the corner to get.
     * @return the element inside the given corner of this side.
     * @throws InvalidCornerException thrown if the corner is not present on this side, i.e. hasCorner returns false.
     */
    @Override
    public List<Element> getCorner(Corner corner) throws InvalidCornerException {
        if (!hasCorner(corner))
            throw new InvalidCornerException();
        if (corners.get(corner).isPresent())
            return List.of(corners.get(corner).get());
        return List.of();
    }

    /**
     * Gets the points awarded after placing this side on the play area.
     *
     * @param playArea the play area where this side was placed.
     * @return the points awarded after placing this side on the play area.
     */
    @Override
    public int getPlacementPoints(PlayArea playArea) {
        return points;
    }
}
