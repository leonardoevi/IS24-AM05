package it.polimi.is24am05.model.card.resourceCard;

import java.util.*;

import it.polimi.is24am05.model.enums.element.Element;
import it.polimi.is24am05.model.enums.Corner;

import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.enums.element.Item;
import it.polimi.is24am05.model.enums.element.Resource;

import it.polimi.is24am05.model.exceptions.card.InvalidCornerException;

import it.polimi.is24am05.model.playArea.PlayArea;

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
    RFS_002(
            Map.of(
                    Corner.NW, Optional.of(Resource.FUNGI),
                    Corner.NE, Optional.of(Resource.FUNGI),
                    Corner.SE, Optional.empty()
            ), 0
    ),
    RFS_003(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.SE, Optional.of(Resource.FUNGI),
                    Corner.SW, Optional.of(Resource.FUNGI)
            ), 0
    ),
    RFS_004(
            Map.of(
                    Corner.NE, Optional.of(Resource.FUNGI),
                    Corner.SE, Optional.of(Resource.FUNGI),
                    Corner.SW, Optional.empty()
            ), 0
    ),

    RFS_005(
            Map.of(
                    Corner.NE, Optional.of(Item.QUILL),
                    Corner.SE, Optional.of(Resource.FUNGI),
                    Corner.SW, Optional.of(Resource.PLANT)
            ), 0
    ),
    RFS_006(
            Map.of(
                    Corner.NW, Optional.of(Item.INKWELL),
                    Corner.NE, Optional.of(Resource.FUNGI),
                    Corner.SE, Optional.of(Resource.ANIMAL)
            ), 0
    ),
    RFS_007(
            Map.of(
                    Corner.NW, Optional.of(Resource.FUNGI),
                    Corner.NE, Optional.of(Resource.INSECT),
                    Corner.SE, Optional.empty(),
                    Corner.SW, Optional.of(Item.MANUSCRIPT)
            ), 0
    ),
    RFS_008(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.NE, Optional.of(Resource.FUNGI),
                    Corner.SW, Optional.empty()

            ), 1
    ),
    RFS_009(
            Map.of(
                    Corner.NW, Optional.of(Resource.FUNGI),
                    Corner.SW, Optional.empty(),
                    Corner.SE, Optional.empty()

            ), 1
    ),
    RFS_010(
            Map.of(
                    Corner.NE, Optional.empty(),
                    Corner.SW, Optional.of(Resource.FUNGI),
                    Corner.SE, Optional.empty()

            ), 1
    ),
    RFS_011(
            Map.of(
                    Corner.NW, Optional.of(Resource.PLANT),
                    Corner.NE, Optional.empty(),
                    Corner.SW, Optional.of(Resource.PLANT)

            ), 0
    ),
    RFS_012(
            Map.of(
                    Corner.NW, Optional.of(Resource.PLANT),
                    Corner.NE, Optional.of(Resource.PLANT),
                    Corner.SE, Optional.empty()

            ), 0
    ),
    RFS_013(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.SW, Optional.of(Resource.PLANT),
                    Corner.SE, Optional.of(Resource.PLANT)

            ), 0
    ),
    RFS_014(
            Map.of(
                    Corner.NE, Optional.of(Resource.PLANT),
                    Corner.SW, Optional.empty(),
                    Corner.SE, Optional.of(Resource.PLANT)

            ), 0
    ),
    RFS_015(
        Map.of(
            Corner.NE, Optional.of(Resource.INSECT),
            Corner.SE, Optional.of(Resource.PLANT),
            Corner.SW, Optional.of(Item.QUILL)
        ), 0
    ),
    RFS_016(
            Map.of(
                    Corner.NW, Optional.of(Resource.FUNGI),
                    Corner.NE, Optional.of(Resource.PLANT),
                    Corner.SE, Optional.of(Item.INKWELL)
            ), 0
    ),
    RFS_017(
            Map.of(
                    Corner.NW, Optional.of(Item.MANUSCRIPT),
                    Corner.SW, Optional.of(Resource.PLANT),
                    Corner.SE, Optional.of(Resource.ANIMAL)
            ), 0
    ),
    RFS_018(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.NE, Optional.empty(),
                    Corner.SW, Optional.of(Resource.PLANT)
            ), 1
    ),
    RFS_019(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.NE, Optional.empty(),
                    Corner.SE, Optional.of(Resource.PLANT)
            ), 1
    ),
    RFS_020(
            Map.of(
                    Corner.NE, Optional.of(Resource.PLANT),
                    Corner.SE, Optional.empty(),
                    Corner.SW, Optional.empty()
            ), 1
    ),
    RFS_021(
            Map.of(
                    Corner.NE, Optional.of(Resource.ANIMAL),
                    Corner.NW, Optional.of(Resource.ANIMAL),
                    Corner.SW, Optional.empty()
            ), 0
    ),
    RFS_022(
            Map.of(
                    Corner.NE, Optional.empty(),
                    Corner.SE, Optional.of(Resource.ANIMAL),
                    Corner.SW, Optional.of(Resource.ANIMAL)
            ), 0
    ),
    RFS_023(
            Map.of(
                    Corner.NW, Optional.of(Resource.ANIMAL),
                    Corner.SE, Optional.empty(),
                    Corner.SW, Optional.of(Resource.ANIMAL)
            ), 0
    ),
    RFS_024(
            Map.of(
                    Corner.NE, Optional.of(Resource.ANIMAL),
                    Corner.NW, Optional.empty(),
                    Corner.SE, Optional.of(Resource.ANIMAL)
            ), 0
    ),
    RFS_025(
            Map.of(
                    Corner.NE, Optional.of(Resource.INSECT),
                    Corner.SW, Optional.of(Item.INKWELL),
                    Corner.SE, Optional.of(Resource.ANIMAL)
            ), 0
    ),
    RFS_026(
            Map.of(
                    Corner.NW, Optional.of(Resource.PLANT),
                    Corner.NE, Optional.of(Resource.ANIMAL),
                    Corner.SE, Optional.of(Item.MANUSCRIPT)
            ), 0
    ),
    RFS_027(
            Map.of(
                    Corner.NW, Optional.of(Item.QUILL),
                    Corner.SW, Optional.of(Resource.ANIMAL),
                    Corner.SE, Optional.of(Resource.FUNGI)
            ), 0
    ),
    RFS_028(
            Map.of(
                    Corner.NE, Optional.empty(),
                    Corner.SE, Optional.empty(),
                    Corner.SW, Optional.of(Resource.ANIMAL)
            ), 1
    ),
    RFS_029(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.SW, Optional.empty(),
                    Corner.SE, Optional.of(Resource.ANIMAL)
            ), 1
    ),
    RFS_030(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.NE, Optional.of(Resource.ANIMAL),
                    Corner.SW, Optional.empty()
            ), 1
    ),
    RFS_031(
            Map.of(
                    Corner.NW, Optional.of(Resource.INSECT),
                    Corner.NE, Optional.of(Resource.INSECT),
                    Corner.SW, Optional.empty()
            ), 0
    ),
    RFS_032(
            Map.of(
                    Corner.NW, Optional.empty(),
                    Corner.SE, Optional.of(Resource.INSECT),
                    Corner.SW, Optional.of(Resource.INSECT)
            ), 0
    ),
    RFS_033(
            Map.of(
                    Corner.NW, Optional.of(Resource.INSECT),
                    Corner.SE, Optional.empty(),
                    Corner.SW, Optional.of(Resource.INSECT)
            ), 0
    ),
    RFS_034(
            Map.of(
                    Corner.NE, Optional.of(Resource.INSECT),
                    Corner.NW, Optional.empty(),
                    Corner.SE, Optional.of(Resource.INSECT)
            ), 0
    ),
    RFS_035(
            Map.of(
                    Corner.NE, Optional.of(Item.QUILL),
                    Corner.SW, Optional.of(Resource.ANIMAL),
                    Corner.SE, Optional.of(Resource.INSECT)
            ), 0
    ),
    RFS_036(
            Map.of(
                    Corner.NE, Optional.of(Resource.INSECT),
                    Corner.NW, Optional.of(Item.MANUSCRIPT),
                    Corner.SE, Optional.of(Resource.FUNGI)
            ), 0
    ),
    RFS_037(
            Map.of(
                    Corner.NE, Optional.of(Resource.PLANT),
                    Corner.NW, Optional.of(Resource.INSECT),
                    Corner.SW, Optional.of(Item.INKWELL)
            ), 0
    ),
    RFS_038(
            Map.of(
                    Corner.NE, Optional.of(Resource.PLANT),
                    Corner.SE, Optional.empty(),
                    Corner.SW, Optional.empty()
            ), 1
    ),
    RFS_039(
            Map.of(
                    Corner.NE, Optional.empty(),
                    Corner.NW, Optional.empty(),
                    Corner.SE, Optional.of(Resource.INSECT)
            ), 1
    ),
    RFS_040(
        Map.of(
            Corner.NE, Optional.of(Resource.INSECT),
            Corner.SE, Optional.empty(),
            Corner.SW, Optional.empty()
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

    @Override
    public Resource getSeed() {
        // Looking at the last 2 digits(fifth and sixth) of each card ID
        int id = Integer.parseInt(this.name().substring(5));
        if(id <= 10)
            return Resource.FUNGI;
        if (id <=20)
            return Resource.PLANT;
        if (id <=30)
            return  Resource.ANIMAL;
        return Resource.INSECT;
    }
}
