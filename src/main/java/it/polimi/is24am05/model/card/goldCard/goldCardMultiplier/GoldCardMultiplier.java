package it.polimi.is24am05.model.card.goldCard.goldCardMultiplier;

import it.polimi.is24am05.model.enums.element.Item;
import it.polimi.is24am05.model.playArea.PlayArea;

/**
 * Implemented by CornerMultiplier and ItemMultiplier to enable polymorphism.
 */
public interface GoldCardMultiplier {
    /**
     * Computes the multiplication factor, i.e. the number of times the points conditions are met.
     *
     * @param playArea the play area where the side was placed.
     * @return the multiplication factor, i.e. the number of times the points conditions are met.
     */
    int compute(PlayArea playArea);
}
