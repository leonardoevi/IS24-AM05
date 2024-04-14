package it.polimi.is24am05.model.card.goldCard;

import java.util.*;

import it.polimi.is24am05.model.card.goldCard.goldCardMultiplier.AlwaysTrueMultiplier;
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
    GFS_042(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.NE, Optional.of(Item.INKWELL),
                    Corner.SE, Optional.empty()
            ),
            Map.of(
                    Resource.FUNGI, 2,
                    Resource.PLANT, 1
            ),
            1,
            ItemMultiplier.INKWELL
    ),
    GFS_043(
            Map.of(
                    Corner.NW, Optional.of(Item.MANUSCRIPT),
                    Corner.NE, Optional.empty(),
                    Corner.SW, Optional.empty()
            ),
            Map.of(
                    Resource.FUNGI, 2,
                    Resource.INSECT, 1
            ),
            1,
            ItemMultiplier.MANUSCRIPT
    ),
    GFS_044(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.NE, Optional.empty(),
                    Corner.SE, Optional.empty()
            ),
            Map.of(
                    Resource.FUNGI, 3,
                    Resource.ANIMAL, 1
            ),
            2,
            CornerMultiplier.CORNER
    ),
    GFS_045(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.NE, Optional.empty(),
                    Corner.SW, Optional.empty()
            ),
            Map.of(
                    Resource.FUNGI, 3,
                    Resource.PLANT, 1
            ),
            2,
            CornerMultiplier.CORNER
    ),
    GFS_046(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.SW, Optional.empty(),
                    Corner.SE, Optional.empty()
            ),
            Map.of(
                    Resource.FUNGI, 3,
                    Resource.INSECT, 1
            ),
            2,
            CornerMultiplier.CORNER
    ),
    GFS_047(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.SW, Optional.of(Item.INKWELL)
            ),
            Map.of(
                    Resource.FUNGI, 3
            ),
            3,
            AlwaysTrueMultiplier.ALWAYSTRUE
    ),
    GFS_048(
            Map.of(
                    Corner.NW, Optional.of(Item.QUILL),
                    Corner.NE, Optional.empty()
            ),
            Map.of(
                    Resource.FUNGI, 3
            ),
            3,
            AlwaysTrueMultiplier.ALWAYSTRUE
    ),
    GFS_049(
            Map.of(
                    Corner.NE, Optional.of(Item.MANUSCRIPT),
                    Corner.SE, Optional.empty()
            ),
            Map.of(
                    Resource.FUNGI, 3
            ),
            3,
            AlwaysTrueMultiplier.ALWAYSTRUE
    ),
    GFS_050(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.SW, Optional.empty()
            ),
            Map.of(
                    Resource.FUNGI, 5
            ),
            5,
            AlwaysTrueMultiplier.ALWAYSTRUE
    ),
    GFS_051(
            Map.of(
                    Corner.NW, Optional.of(Item.QUILL),
                    Corner.NE, Optional.empty(),
                    Corner.SW, Optional.empty()
            ),
            Map.of(
                    Resource.PLANT, 2,
                    Resource.INSECT, 1
            ),
            1,
            ItemMultiplier.QUILL
    ),
    GFS_052(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.NE, Optional.of(Item.MANUSCRIPT),
                    Corner.SE, Optional.empty()
            ),
            Map.of(
                    Resource.PLANT, 2,
                    Resource.FUNGI, 1
            ),
            1,
            ItemMultiplier.MANUSCRIPT
    ),
    GFS_053(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.SW, Optional.of(Item.INKWELL),
                    Corner.SE, Optional.empty()
            ),
            Map.of(
                    Resource.PLANT, 2,
                    Resource.ANIMAL, 1
            ),
            1,
            ItemMultiplier.INKWELL
    ),
    GFS_054(
            Map.of(
                    Corner.NE, Optional.empty(),
                    Corner.SW, Optional.empty(),
                    Corner.SE, Optional.empty()
            ),
            Map.of(
                    Resource.PLANT, 3,
                    Resource.INSECT, 1
            ),
            2,
            CornerMultiplier.CORNER
    ),
    GFS_055(
            Map.of(
                    Corner.NE, Optional.empty(),
                    Corner.NW, Optional.empty(),
                    Corner.SW, Optional.empty()
            ),
            Map.of(
                    Resource.PLANT, 3,
                    Resource.ANIMAL, 1
            ),
            2,
            CornerMultiplier.CORNER
    ),
    GFS_056(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.SE, Optional.empty(),
                    Corner.SW, Optional.empty()
            ),
            Map.of(
                    Resource.PLANT, 3,
                    Resource.FUNGI, 1
            ),
            2,
            CornerMultiplier.CORNER
    ),
    GFS_057(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.SW, Optional.of(Item.QUILL)
            ),
            Map.of(
                    Resource.PLANT, 3
            ),
            3,
            AlwaysTrueMultiplier.ALWAYSTRUE
    ),
    GFS_058(
            Map.of(
                    Corner.NW, Optional.of(Item.MANUSCRIPT),
                    Corner.NE, Optional.empty()
            ),
            Map.of(
                    Resource.PLANT, 3
            ),
            3,
            AlwaysTrueMultiplier.ALWAYSTRUE
    ),
    GFS_059(
            Map.of(
                    Corner.NE, Optional.of(Item.INKWELL),
                    Corner.SE, Optional.empty()
            ),
            Map.of(
                    Resource.PLANT, 3
            ),
            3,
            AlwaysTrueMultiplier.ALWAYSTRUE
    ),
    GFS_060(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.NE, Optional.empty()
            ),
            Map.of(
                    Resource.PLANT, 5
            ),
            5,
            AlwaysTrueMultiplier.ALWAYSTRUE
    ),
    GFS_061(
            Map.of(
                    Corner.NW, Optional.of(Item.INKWELL),
                    Corner.NE, Optional.empty(),
                    Corner.SW, Optional.empty()
            ),
            Map.of(
                    Resource.ANIMAL, 2,
                    Resource.INSECT, 1
            ),
            1,
            ItemMultiplier.INKWELL
    ),
    GFS_062(
            Map.of(
                    Corner.NE, Optional.empty(),
                    Corner.SE, Optional.of(Item.MANUSCRIPT),
                    Corner.SW, Optional.empty()
            ),
            Map.of(
                    Resource.ANIMAL, 2,
                    Resource.PLANT, 1
            ),
            1,
            ItemMultiplier.MANUSCRIPT
    ),
    GFS_063(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.SW, Optional.of(Item.QUILL),
                    Corner.SE, Optional.empty()
            ),
            Map.of(
                    Resource.ANIMAL, 2,
                    Resource.FUNGI, 1
            ),
            1,
            ItemMultiplier.QUILL
    ),
    GFS_064(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.NE, Optional.empty(),
                    Corner.SE, Optional.empty()
            ),
            Map.of(
                    Resource.ANIMAL, 3,
                    Resource.INSECT, 1
            ),
            2,
            CornerMultiplier.CORNER
    ),
    GFS_065(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.SW, Optional.empty(),
                    Corner.SE, Optional.empty()
            ),
            Map.of(
                    Resource.ANIMAL, 3,
                    Resource.FUNGI, 1
            ),
            2,
            CornerMultiplier.CORNER
    ),
    GFS_066(
            Map.of(
                    Corner.NE, Optional.empty(),
                    Corner.SW, Optional.empty(),
                    Corner.SE, Optional.empty()
            ),
            Map.of(
                    Resource.ANIMAL, 3,
                    Resource.PLANT, 1
            ),
            2,
            CornerMultiplier.CORNER
    ),
    GFS_067(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.SW, Optional.of(Item.MANUSCRIPT)
            ),
            Map.of(
                    Resource.ANIMAL, 3
            ),
            3,
            AlwaysTrueMultiplier.ALWAYSTRUE
    ),
    GFS_068(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.NE, Optional.of(Item.INKWELL)
            ),
            Map.of(
                    Resource.ANIMAL, 3
            ),
            3,
            AlwaysTrueMultiplier.ALWAYSTRUE
    ),
    GFS_069(
            Map.of(
                    Corner.NE, Optional.empty(),
                    Corner.SE, Optional.of(Item.QUILL)
            ),
            Map.of(
                    Resource.ANIMAL, 3
            ),
            3,
            AlwaysTrueMultiplier.ALWAYSTRUE
    ),
    GFS_070(
            Map.of(
                    Corner.NE, Optional.empty(),
                    Corner.SE, Optional.empty()
            ),
            Map.of(
                    Resource.ANIMAL, 5
            ),
            5,
            AlwaysTrueMultiplier.ALWAYSTRUE
    ),
    GFS_071(
            Map.of(
                    Corner.NE, Optional.of(Item.QUILL),
                    Corner.NW, Optional.empty(),
                    Corner.SE, Optional.empty()
            ),
            Map.of(
                    Resource.INSECT, 2,
                    Resource.PLANT, 1
            ),
            1,
            ItemMultiplier.QUILL
    ),
    GFS_072(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.SE, Optional.empty(),
                    Corner.SW, Optional.of(Item.MANUSCRIPT)
            ),
            Map.of(
                    Resource.INSECT, 2,
                    Resource.ANIMAL, 1
            ),
            1,
            ItemMultiplier.MANUSCRIPT
    ),
    GFS_073(
            Map.of(
                    Corner.NE, Optional.empty(),
                    Corner.SW, Optional.empty(),
                    Corner.SE, Optional.of(Item.INKWELL)
                    ),
            Map.of(
                    Resource.INSECT, 2,
                    Resource.FUNGI, 1
            ),
            1,
            ItemMultiplier.INKWELL
    ),
    GFS_074(
            Map.of(
                    Corner.NE, Optional.empty(),
                    Corner.NW, Optional.empty(),
                    Corner.SE, Optional.empty()
            ),
            Map.of(
                    Resource.INSECT, 3,
                    Resource.ANIMAL, 1
            ),
            2,
            CornerMultiplier.CORNER
    ),
    GFS_075(
            Map.of(
                    Corner.NE, Optional.empty(),
                    Corner.NW, Optional.empty(),
                    Corner.SW, Optional.empty()
            ),
            Map.of(
                    Resource.INSECT, 3,
                    Resource.PLANT, 1
            ),
            2,
            CornerMultiplier.CORNER
    ),
    GFS_076(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.SW, Optional.empty(),
                    Corner.SE, Optional.empty()
            ),
            Map.of(
                    Resource.INSECT, 3,
                    Resource.FUNGI, 1
            ),
            2,
            CornerMultiplier.CORNER
    ),
    GFS_077(
            Map.of(
                    Corner.NW, Optional.of(Item.INKWELL),
                    Corner.SW, Optional.empty()

            ),
            Map.of(
                    Resource.INSECT, 3
            ),
            3,
            AlwaysTrueMultiplier.ALWAYSTRUE
    ),
    GFS_078(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.NE, Optional.of(Item.MANUSCRIPT)

            ),
            Map.of(
                    Resource.INSECT, 3
            ),
            3,
            AlwaysTrueMultiplier.ALWAYSTRUE
    ),
    GFS_079(
            Map.of(
                    Corner.SW, Optional.of(Item.QUILL),
                    Corner.SE, Optional.empty()

            ),
            Map.of(
                    Resource.INSECT, 3
            ),
            3,
            AlwaysTrueMultiplier.ALWAYSTRUE
    ),
    GFS_080(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.NE, Optional.empty()

            ),
            Map.of(
                    Resource.INSECT, 5
            ),
            5,
            AlwaysTrueMultiplier.ALWAYSTRUE
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

    /**
     * Gets the conditions.
     *
     * @return the conditions.
     */
    @Override
    public Map<Resource, Integer> getPlacementConditions() {
        return this.conditions;
    }

    @Override
    public GoldCardMultiplier getMultiplier() {
        return this.multiplier;
    }

    public GoldCardMultiplier goldCardMultiplier(){
        return this.multiplier;
    }

    @Override
    public Resource getSeed() {
        // Looking at the last 2 digits(fifth and sixth) of each card ID
        int id = Integer.parseInt(this.name().substring(5));
        id -=40;
        if(id <= 10)
            return Resource.FUNGI;
        if (id <=20)
            return Resource.PLANT;
        if (id <=30)
            return  Resource.ANIMAL;
        return Resource.INSECT;
    }

    @Override
    public int getPoints() {
        return this.points;
    }


}
