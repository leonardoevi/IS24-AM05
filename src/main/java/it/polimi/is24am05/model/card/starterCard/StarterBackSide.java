package it.polimi.is24am05.model.card.starterCard;

import java.util.*;

import it.polimi.is24am05.model.enums.element.Element;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.enums.Corner;

import it.polimi.is24am05.model.card.side.Side;

import it.polimi.is24am05.model.exceptions.card.InvalidCornerException;

/**
 * Starter back sides.
 */
public enum StarterBackSide implements Side {
    SBS_081(
        Map.of(
            Corner.NW, Resource.FUNGI,
            Corner.NE, Resource.PLANT,
            Corner.SE, Resource.ANIMAL,
            Corner.SW, Resource.INSECT
        )
    ),
    SBS_082(
            Map.of(
                    Corner.NW, Resource.PLANT,
                    Corner.NE, Resource.ANIMAL,
                    Corner.SE, Resource.INSECT,
                    Corner.SW, Resource.FUNGI
            )
    ),
    SBS_083(
            Map.of(
                    Corner.NW, Resource.INSECT,
                    Corner.NE, Resource.ANIMAL,
                    Corner.SE, Resource.PLANT,
                    Corner.SW, Resource.FUNGI
            )
    ),
    SBS_084(
            Map.of(
                    Corner.NW, Resource.PLANT,
                    Corner.NE, Resource.INSECT,
                    Corner.SE, Resource.FUNGI,
                    Corner.SW, Resource.ANIMAL
            )
    ),
    SBS_085(
        Map.of(
            Corner.NW, Resource.INSECT,
            Corner.NE, Resource.FUNGI,
            Corner.SE, Resource.ANIMAL,
            Corner.SW, Resource.PLANT
        )
    ),
    SBS_086(
            Map.of(
                    Corner.NW, Resource.FUNGI,
                    Corner.NE, Resource.ANIMAL,
                    Corner.SE, Resource.INSECT,
                    Corner.SW, Resource.PLANT
            )
    );

    /**
     * Maps corners of this side to their resource.
     * <p>
     * Corners not present on this side are not in the map.
     */
    private final Map<Corner, Resource> corners;

    /**
     * Constructor.
     *
     * @param corners maps corners of this side to their resource.
     */
    StarterBackSide(Map<Corner, Resource> corners) {
        this.corners = corners;
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
        if(!hasCorner(corner))
            throw new InvalidCornerException();
        return List.of(corners.get(corner));
    }
}
