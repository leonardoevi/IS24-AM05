package it.polimi.is24am05.model.card.goldCard;

import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.side.Side;
/**
 * Gold cards visible. ie Gold cards without frontSide.
 */
public enum GoldCardVisible implements Card {
    GCV_F(GoldBackSide.GBS_041),
    GCV_P(GoldBackSide.GBS_051),
    GCV_A(GoldBackSide.GBS_061),
    GCV_I(GoldBackSide.GBS_061);
    private GoldBackSide backSide;
    GoldCardVisible(GoldBackSide backside) {
        this.backSide=backside;
    }

    public Side getFrontSide()
    {
        return null;
    }

    public Side getBackSide() {
        return this.backSide;
    }
}
