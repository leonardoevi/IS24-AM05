package it.polimi.is24am05.model.card.resourceCard;

import java.util.*;

import it.polimi.is24am05.model.enums.element.Element;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.enums.Corner;

import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.playArea.SideDisplayer;

/**
 * Resource back sides of the game.
 */
public enum ResourceBackSide implements Side {
    RBS_001(Resource.FUNGI),
    RBS_002(Resource.FUNGI),
    RBS_003(Resource.FUNGI),
    RBS_004(Resource.FUNGI),
    RBS_005(Resource.FUNGI),
    RBS_006(Resource.FUNGI),
    RBS_007(Resource.FUNGI),
    RBS_008(Resource.FUNGI),
    RBS_009(Resource.FUNGI),
    RBS_010(Resource.FUNGI),
    RBS_011(Resource.PLANT),
    RBS_012(Resource.PLANT),
    RBS_013(Resource.PLANT),
    RBS_014(Resource.PLANT),
    RBS_015(Resource.PLANT),
    RBS_016(Resource.PLANT),
    RBS_017(Resource.PLANT),
    RBS_018(Resource.PLANT),
    RBS_019(Resource.PLANT),
    RBS_020(Resource.PLANT),
    RBS_021(Resource.ANIMAL),
    RBS_022(Resource.ANIMAL),
    RBS_023(Resource.ANIMAL),
    RBS_024(Resource.ANIMAL),
    RBS_025(Resource.ANIMAL),
    RBS_026(Resource.ANIMAL),
    RBS_027(Resource.ANIMAL),
    RBS_028(Resource.ANIMAL),
    RBS_029(Resource.ANIMAL),
    RBS_030(Resource.ANIMAL),
    RBS_031(Resource.INSECT),
    RBS_032(Resource.INSECT),
    RBS_033(Resource.INSECT),
    RBS_034(Resource.INSECT),
    RBS_035(Resource.INSECT),
    RBS_036(Resource.INSECT),
    RBS_037(Resource.INSECT),
    RBS_038(Resource.INSECT),
    RBS_039(Resource.INSECT),
    RBS_040(Resource.INSECT);

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
    @Override
    public  String[][] toMatrix()
    {
        return SideDisplayer.sideToString(this);
    }
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for(String[] row : this.toMatrix()){
            for (String s : row){
                sb.append(s);
            }
            sb.append("\n");
        }
        return sb.toString();
    }


}
