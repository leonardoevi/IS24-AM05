package it.polimi.is24am05.model.card.starterCard;

import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.card.Card;

/**
 * Starter cards.
 */
public enum StarterCard implements Card {
    SC_081(StarterFrontSide.SFS_081, StarterBackSide.SBS_081),
    SC_085(StarterFrontSide.SFS_085, StarterBackSide.SBS_085);

    /**
     * Front side of this card.
     */
    private final StarterFrontSide frontSide;
    /**
     * Back side of this card.
     */
    private final StarterBackSide backSide;

    /**
     * Constructor.
     *
     * @param frontSide the front side of this card.
     * @param backSide the back side of this card.
     */
    StarterCard(StarterFrontSide frontSide, StarterBackSide backSide) {
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
