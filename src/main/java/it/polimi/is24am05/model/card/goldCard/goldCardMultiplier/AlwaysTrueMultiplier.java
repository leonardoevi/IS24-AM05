package it.polimi.is24am05.model.card.goldCard.goldCardMultiplier;

import it.polimi.is24am05.model.playArea.PlayArea;

/**
 *  Gold card multiplier with no points condition, i.e. with a fixed multiplication factor of 1.
 */
public enum AlwaysTrueMultiplier implements GoldCardMultiplier{
    ALWAYSTRUE;

    /**
     * Computes the multiplication factor, i.e. 1.
     *
     * @param playArea the play area where the side was placed.
     * @return the multiplication factor, i.e. 1.
     */
    @Override
    public int compute(PlayArea playArea) {
        return 1;
    }
}
