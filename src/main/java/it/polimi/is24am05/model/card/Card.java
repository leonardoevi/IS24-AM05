package it.polimi.is24am05.model.card;

import it.polimi.is24am05.model.card.side.Side;

/**
 * Implemented by resource, gold and starter cards to enable polymorphism.
 */
public interface Card {
    /**
     * Gets the front side of this card.
     *
     * @return the front side of this card.
     */
    Side getFrontSide();

    /**
     * Gets the back side of this card.
     *
     * @return the back side of this card.
     */
    Side getBackSide();

    default int getId(){
        return Integer.valueOf(this.toString().substring(4,6));
    }

   public String[] [] toMatrix();

    public String toString();


}
