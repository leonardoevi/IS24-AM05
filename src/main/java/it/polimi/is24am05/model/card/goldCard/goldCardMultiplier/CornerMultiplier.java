package it.polimi.is24am05.model.card.goldCard.goldCardMultiplier;

import it.polimi.is24am05.model.playArea.PlayArea;

/**
 * Gold card multiplier that counts the covered corners of the side.
 */
public enum CornerMultiplier implements GoldCardMultiplier {
    CORNER;

    /**
     * Computes the multiplication factor, i.e. the number of covered corners of the side.
     *
     * @param playArea the play area where the side was placed.
     * @return the multiplication factor, i.e. the number of covered corners of the side.
     */
    @Override
    public int compute(PlayArea playArea) {
        return 1;
    }
}
