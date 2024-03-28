package it.polimi.is24am05.model.card.goldCard;

import java.util.*;

import it.polimi.is24am05.model.enums.element.Element;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.enums.Corner;

import it.polimi.is24am05.model.card.side.Side;

/**
 * Gold back sides.
 */
public enum GoldBackSide implements Side {
    GBS_041(Resource.FUNGI),
    GBS_042(Resource.FUNGI),
    GBS_043(Resource.FUNGI),
    GBS_044(Resource.FUNGI),
    GBS_045(Resource.FUNGI),
    GBS_046(Resource.FUNGI),
    GBS_047(Resource.FUNGI),
    GBS_048(Resource.FUNGI),
    GBS_049(Resource.FUNGI),
    GBS_050(Resource.FUNGI),
    GBS_051(Resource.PLANT),
    GBS_052(Resource.PLANT),
    GBS_053(Resource.PLANT),
    GBS_054(Resource.PLANT),
    GBS_055(Resource.PLANT),
    GBS_056(Resource.PLANT),
    GBS_057(Resource.PLANT),
    GBS_058(Resource.PLANT),
    GBS_059(Resource.PLANT),
    GBS_060(Resource.PLANT),
    GBS_061(Resource.ANIMAL),
    GBS_062(Resource.ANIMAL),
    GBS_063(Resource.ANIMAL),
    GBS_064(Resource.ANIMAL),
    GBS_065(Resource.ANIMAL),
    GBS_066(Resource.ANIMAL),
    GBS_067(Resource.ANIMAL),
    GBS_068(Resource.ANIMAL),
    GBS_069(Resource.ANIMAL),
    GBS_070(Resource.ANIMAL),
    GBS_071(Resource.INSECT),
    GBS_072(Resource.INSECT),
    GBS_073(Resource.INSECT),
    GBS_074(Resource.INSECT),
    GBS_075(Resource.INSECT),
    GBS_076(Resource.INSECT),
    GBS_077(Resource.INSECT),
    GBS_078(Resource.INSECT),
    GBS_079(Resource.INSECT),
    GBS_080(Resource.INSECT);

    /**
     * Resource at the center of this side.
     */
    private final Resource center;

    /**
     * Constructor.
     *
     * @param center resource at the center of this side.
     */
    GoldBackSide(Resource center) {
        this.center = center;
    }

    /**
     * Checks if a corner is present on this side, i.e. a card can cover the corner.
     * <p>
     * As all gold back sides present all corners, it always returns true.
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
     * As all gold back sides present all corners, no exception is ever thrown.
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
}
