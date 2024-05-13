package it.polimi.is24am05.model.card.resourceCard;

import it.polimi.is24am05.model.Player.HandDisplayer;
import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.card.Card;

/**
 * Resource cards.
 */
public enum ResourceCard implements Card {
    RC_001(ResourceFrontSide.RFS_001, ResourceBackSide.RBS_001),
    RC_002(ResourceFrontSide.RFS_002, ResourceBackSide.RBS_002),
    RC_003(ResourceFrontSide.RFS_003, ResourceBackSide.RBS_003),
    RC_004(ResourceFrontSide.RFS_004, ResourceBackSide.RBS_004),
    RC_005(ResourceFrontSide.RFS_005, ResourceBackSide.RBS_005),
    RC_006(ResourceFrontSide.RFS_006, ResourceBackSide.RBS_006),
    RC_007(ResourceFrontSide.RFS_007, ResourceBackSide.RBS_007),
    RC_008(ResourceFrontSide.RFS_008, ResourceBackSide.RBS_008),
    RC_009(ResourceFrontSide.RFS_009, ResourceBackSide.RBS_009),
    RC_010(ResourceFrontSide.RFS_010, ResourceBackSide.RBS_010),
    RC_011(ResourceFrontSide.RFS_011, ResourceBackSide.RBS_011),
    RC_012(ResourceFrontSide.RFS_012, ResourceBackSide.RBS_012),
    RC_013(ResourceFrontSide.RFS_013, ResourceBackSide.RBS_013),
    RC_014(ResourceFrontSide.RFS_014, ResourceBackSide.RBS_014),
    RC_015(ResourceFrontSide.RFS_015, ResourceBackSide.RBS_015),
    RC_016(ResourceFrontSide.RFS_016, ResourceBackSide.RBS_016),
    RC_017(ResourceFrontSide.RFS_017, ResourceBackSide.RBS_017),
    RC_018(ResourceFrontSide.RFS_018, ResourceBackSide.RBS_018),
    RC_019(ResourceFrontSide.RFS_019, ResourceBackSide.RBS_019),
    RC_020(ResourceFrontSide.RFS_020, ResourceBackSide.RBS_020),
    RC_021(ResourceFrontSide.RFS_021, ResourceBackSide.RBS_021),
    RC_022(ResourceFrontSide.RFS_022, ResourceBackSide.RBS_022),
    RC_023(ResourceFrontSide.RFS_023, ResourceBackSide.RBS_023),
    RC_024(ResourceFrontSide.RFS_024, ResourceBackSide.RBS_024),
    RC_025(ResourceFrontSide.RFS_025, ResourceBackSide.RBS_025),
    RC_026(ResourceFrontSide.RFS_026, ResourceBackSide.RBS_026),
    RC_027(ResourceFrontSide.RFS_027, ResourceBackSide.RBS_027),
    RC_028(ResourceFrontSide.RFS_028, ResourceBackSide.RBS_028),
    RC_029(ResourceFrontSide.RFS_029, ResourceBackSide.RBS_029),
    RC_030(ResourceFrontSide.RFS_030, ResourceBackSide.RBS_030),
    RC_031(ResourceFrontSide.RFS_031, ResourceBackSide.RBS_031),
    RC_032(ResourceFrontSide.RFS_032, ResourceBackSide.RBS_032),
    RC_033(ResourceFrontSide.RFS_033, ResourceBackSide.RBS_033),
    RC_034(ResourceFrontSide.RFS_034, ResourceBackSide.RBS_034),
    RC_035(ResourceFrontSide.RFS_035, ResourceBackSide.RBS_035),
    RC_036(ResourceFrontSide.RFS_036, ResourceBackSide.RBS_036),
    RC_037(ResourceFrontSide.RFS_037, ResourceBackSide.RBS_037),
    RC_038(ResourceFrontSide.RFS_038, ResourceBackSide.RBS_038),
    RC_039(ResourceFrontSide.RFS_039, ResourceBackSide.RBS_039),
    RC_040(ResourceFrontSide.RFS_040, ResourceBackSide.RBS_040);

    /**
     * Front side of this card.
     */
    private final ResourceFrontSide frontSide;
    /**
     * Back side of this card.
     */
    private final ResourceBackSide backSide;

    /**
     * Constructor.
     *
     * @param frontSide the front side of this card.
     * @param backSide the back side of this card.
     */
    ResourceCard(ResourceFrontSide frontSide, ResourceBackSide backSide) {
        this.frontSide = frontSide;
        this.backSide = backSide;
    }

    /**
     * Gets the front side of this card.
     *
     * @return the front side of this card.
     */
    @Override
    public Side getFrontSide() {
        return frontSide;
    }

    /**
     * Gets the back side of this card.
     *
     * @return the back side of this card.
     */
    @Override
    public Side getBackSide() {
        return backSide;
    }

    public String[][] toMatrix()
    {
        return HandDisplayer.CardToMatrix(this);
    }
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
