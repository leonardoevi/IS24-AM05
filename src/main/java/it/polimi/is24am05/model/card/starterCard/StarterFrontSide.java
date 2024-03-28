package it.polimi.is24am05.model.card.starterCard;

import java.util.*;

import it.polimi.is24am05.model.enums.element.Element;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.enums.Corner;

import it.polimi.is24am05.model.card.side.Side;

import it.polimi.is24am05.model.exceptions.card.InvalidCornerException;

/**
 * Starter front sides.
 */
public enum StarterFrontSide implements Side {
    SFS_081(
        Map.of(
            Corner.NW, Optional.empty(),
            Corner.NE, Optional.of(List.of(Resource.PLANT)),
            Corner.SE, Optional.empty(),
            Corner.SW, Optional.of(List.of(Resource.INSECT)),
            Corner.CE, Optional.of(List.of(Resource.INSECT))
        )
    ),
    SFS_082(
            Map.of(
                    Corner.NW, Optional.of(List.of(Resource.ANIMAL)),
                    Corner.NE, Optional.empty(),
                    Corner.SE, Optional.of(List.of(Resource.FUNGI)),
                    Corner.SW, Optional.empty(),
                    Corner.CE, Optional.of(List.of(Resource.FUNGI))
            )
    ),
    SFS_083(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.NE, Optional.empty(),
                    Corner.SE, Optional.empty(),
                    Corner.SW, Optional.empty(),
                    Corner.CE, Optional.of(List.of(Resource.FUNGI, Resource.PLANT))
            )
    ),
    SFS_084(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.NE, Optional.empty(),
                    Corner.SE, Optional.empty(),
                    Corner.SW, Optional.empty(),
                    Corner.CE, Optional.of(List.of(Resource.ANIMAL, Resource.INSECT))
            )
    ),
    SFS_085(
        Map.of(
            Corner.NW, Optional.empty(),
            Corner.NE, Optional.empty(),
            Corner.CE, Optional.of(List.of(
                Resource.ANIMAL,
                Resource.INSECT,
                Resource.PLANT
            ))
        )
    ),
    SFS_086(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.NE, Optional.empty(),
                    Corner.CE, Optional.of(List.of(
                            Resource.ANIMAL,
                            Resource.FUNGI,
                            Resource.PLANT
                    ))
            )
    );

    /**
     * Maps corners of this side to their list of resources.
     * <p>
     * Corners not present on this side are not in the map, while empty corners map to empty optionals.
     */
    private final Map<Corner, Optional<List<Resource>>> corners;

    /**
     * Constructor.
     *
     * @param corners maps corners of this side to their list of resources.
     */
    StarterFrontSide(Map<Corner, Optional<List<Resource>>> corners) {
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
        if (!hasCorner(corner))
            throw new InvalidCornerException();
        if (corners.get(corner).isPresent())
            // The array list is constructed to cast types
            return new ArrayList<>(corners.get(corner).get());
        return List.of();
    }

    @Override
    public Resource getSeed() {
        return null;
    }
}
