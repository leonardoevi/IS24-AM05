package it.polimi.is24am05.model.card.goldCard;

import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.card.Card;

/**
 * Gold cards.
 */
public enum GoldCard implements Card {
    GC_041(GoldFrontSide.GFS_041, GoldBackSide.GBS_041),
    GC_054(GoldFrontSide.GFS_054, GoldBackSide.GBS_054);

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
}
