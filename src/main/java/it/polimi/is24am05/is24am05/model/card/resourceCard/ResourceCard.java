package it.polimi.is24am05.is24am05.model.card.resourceCard;

import it.polimi.is24am05.is24am05.model.card.side.Side;
import it.polimi.is24am05.is24am05.model.card.Card;

/**
 * Resource cards.
 */
public enum ResourceCard implements Card {
    RC_001(ResourceFrontSide.RFS_001, ResourceBackSide.RBS_001),
    RC_015(ResourceFrontSide.RFS_015, ResourceBackSide.RBS_015),
    RC_028(ResourceFrontSide.RFS_028, ResourceBackSide.RBS_028);

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
}
