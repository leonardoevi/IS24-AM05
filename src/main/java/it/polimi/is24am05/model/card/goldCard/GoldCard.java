package it.polimi.is24am05.model.card.goldCard;

import it.polimi.is24am05.model.Player.HandDisplayer;
import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.card.Card;

/**
 * Gold cards.
 */
public enum GoldCard implements Card {
    GC_041(GoldFrontSide.GFS_041, GoldBackSide.GBS_041),
    GC_042(GoldFrontSide.GFS_042, GoldBackSide.GBS_042),
    GC_043(GoldFrontSide.GFS_043, GoldBackSide.GBS_043),
    GC_044(GoldFrontSide.GFS_044, GoldBackSide.GBS_044),
    GC_045(GoldFrontSide.GFS_045, GoldBackSide.GBS_045),
    GC_046(GoldFrontSide.GFS_046, GoldBackSide.GBS_046),
    GC_047(GoldFrontSide.GFS_047, GoldBackSide.GBS_047),
    GC_048(GoldFrontSide.GFS_048, GoldBackSide.GBS_048),
    GC_049(GoldFrontSide.GFS_049, GoldBackSide.GBS_049),
    GC_050(GoldFrontSide.GFS_050, GoldBackSide.GBS_050),
    GC_051(GoldFrontSide.GFS_051, GoldBackSide.GBS_051),
    GC_052(GoldFrontSide.GFS_052, GoldBackSide.GBS_052),
    GC_053(GoldFrontSide.GFS_053, GoldBackSide.GBS_053),
    GC_054(GoldFrontSide.GFS_054, GoldBackSide.GBS_054),
    GC_055(GoldFrontSide.GFS_055, GoldBackSide.GBS_055),
    GC_056(GoldFrontSide.GFS_056, GoldBackSide.GBS_056),
    GC_057(GoldFrontSide.GFS_057, GoldBackSide.GBS_057),
    GC_058(GoldFrontSide.GFS_058, GoldBackSide.GBS_058),
    GC_059(GoldFrontSide.GFS_059, GoldBackSide.GBS_059),
    GC_060(GoldFrontSide.GFS_060, GoldBackSide.GBS_060),
    GC_061(GoldFrontSide.GFS_061, GoldBackSide.GBS_061),
    GC_062(GoldFrontSide.GFS_062, GoldBackSide.GBS_062),
    GC_063(GoldFrontSide.GFS_063, GoldBackSide.GBS_063),
    GC_064(GoldFrontSide.GFS_064, GoldBackSide.GBS_064),
    GC_065(GoldFrontSide.GFS_065, GoldBackSide.GBS_065),
    GC_066(GoldFrontSide.GFS_066, GoldBackSide.GBS_066),
    GC_067(GoldFrontSide.GFS_067, GoldBackSide.GBS_067),
    GC_068(GoldFrontSide.GFS_068, GoldBackSide.GBS_068),
    GC_069(GoldFrontSide.GFS_069, GoldBackSide.GBS_069),
    GC_070(GoldFrontSide.GFS_070, GoldBackSide.GBS_070),
    GC_071(GoldFrontSide.GFS_071, GoldBackSide.GBS_071),
    GC_072(GoldFrontSide.GFS_072, GoldBackSide.GBS_072),
    GC_073(GoldFrontSide.GFS_073, GoldBackSide.GBS_073),
    GC_074(GoldFrontSide.GFS_074, GoldBackSide.GBS_074),
    GC_075(GoldFrontSide.GFS_075, GoldBackSide.GBS_075),
    GC_076(GoldFrontSide.GFS_076, GoldBackSide.GBS_076),
    GC_077(GoldFrontSide.GFS_077, GoldBackSide.GBS_077),
    GC_078(GoldFrontSide.GFS_078, GoldBackSide.GBS_078),
    GC_079(GoldFrontSide.GFS_079, GoldBackSide.GBS_079),
    GC_080(GoldFrontSide.GFS_080, GoldBackSide.GBS_080);

    /**
     * Front side of this card.
     */
    private final GoldFrontSide frontSide;
    /**
     * Back side of this card.
     */
    private final GoldBackSide backSide;

    /**
     * Constructor.
     *
     * @param frontSide the front side of this card.
     * @param backSide the back side of this card.
     */
    GoldCard(GoldFrontSide frontSide, GoldBackSide backSide) {
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
