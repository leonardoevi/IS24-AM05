package it.polimi.is24am05.model.card.goldCard;

import java.util.*;

import it.polimi.is24am05.model.enums.element.Element;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.enums.element.Item;
import it.polimi.is24am05.model.enums.Corner;

import it.polimi.is24am05.model.card.side.Side;

import it.polimi.is24am05.model.card.goldCard.goldCardMultiplier.GoldCardMultiplier;
import it.polimi.is24am05.model.card.goldCard.goldCardMultiplier.CornerMultiplier;
import it.polimi.is24am05.model.card.goldCard.goldCardMultiplier.ItemMultiplier;

import it.polimi.is24am05.model.exceptions.card.InvalidCornerException;
import it.polimi.is24am05.model.playArea.PlayArea;

/**
 * Gold front sides.
 */
public enum GoldFrontSide implements Side {
    GFS_041(
        Map.of(
            Corner.NE, Optional.empty(),
            Corner.SE, Optional.of(Item.QUILL),
            Corner.SW, Optional.empty()
        ),
        Map.of(
            Resource.FUNGI, 2,
            Resource.ANIMAL, 1
        ),
        1,
        ItemMultiplier.QUILL
    ),
    GFS_054(
        Map.of(
            Corner.NE, Optional.empty(),
            Corner.SE, Optional.empty(),
            Corner.SW, Optional.empty()
        ),
        Map.of(
            Resource.PLANT, 3,
            Resource.INSECT, 1
        ),
        2,
        CornerMultiplier.CORNER
    );

    /**
     * Maps corners of this side to their item.
     * <p>
     * Corners not present on this side are not in the map, while empty corners map to empty optionals.
     */
    private final Map<Corner, Optional<Item>> corners;
    /**
     * Counts the resources needed to place this side.
     */
    private final Map<Resource, Integer> conditions;
    /**
     * Basic points of this side.
     */
    private final int points;
    /**
     * Multiplier of this side.
     */
    private final GoldCardMultiplier multiplier;

    /**
     * Constructor.
     *
     * @param corners paps corners of this side to their item.
     * @param conditions counts the resources needed to place this side.
     * @param points basic points of this side.
     * @param multiplier multiplier of this side.
     */
    GoldFrontSide(Map<Corner, Optional<Item>> corners, Map<Resource, Integer> conditions, int points, GoldCardMultiplier multiplier) {
        this.corners = corners;
        this.conditions = conditions;
        this.points = points;
        this.multiplier = multiplier;
    }

    /**
     * Gets the conditions.
     *
     * @return the conditions.
     */
    public Map<Resource, Integer> getConditions() {
        return conditions;
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
     * Gets the total points awarded after placing this side on the play area.
     * <p>
     * It is computed as the basic points of this side times the multiplication factor of the multiplier,
     * i.e. the number of times the points conditions are met.
     *
     * @param playArea the play area where this side was placed.
     * @return the total points awarded after placing this side on the play area.
     */
    @Override
    public int getPlacementPoints(PlayArea playArea) {
        return points * multiplier.compute(playArea);
    }
}
