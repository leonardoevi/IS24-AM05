package it.polimi.is24am05.client.model;

import it.polimi.is24am05.model.card.side.Side;

/**
 * Local model of the deck, restricted to the player's point of view.
 */
public class DeckPov {
    /**
     * Side at the top of this deck.
     */
    Side top;

    /**
     * Side of the first visible card.
     */
    Side visible1;

    /**
     * Side of the second visible card.
     */
    Side visible2;

    /**
     * Constructor.
     * @param top the side at the top of this deck.
     * @param visible1 the side of the first visible card.
     * @param visible2 the side of the second visible card.
     */
    public DeckPov(Side top, Side visible1, Side visible2) {
        this.top = top;
        this.visible1 = visible1;
        this.visible2 = visible2;
    }
}
