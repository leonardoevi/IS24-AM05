package it.polimi.is24am05.model.card.goldCard.goldCardMultiplier;

import it.polimi.is24am05.model.playArea.PlayArea;

public enum AlwaysTrueMultiplier implements GoldCardMultiplier{
    ALWAYSTRUE;

    @Override
    public int compute(PlayArea playArea) {
        return 1;
    }
}
