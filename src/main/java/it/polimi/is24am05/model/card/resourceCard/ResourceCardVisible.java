package it.polimi.is24am05.model.card.resourceCard;

import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.goldCard.GoldBackSide;
import it.polimi.is24am05.model.card.side.Side;
/**
 * Resource cards visible. ie Resource cards without frontSide.
 */
public enum ResourceCardVisible implements Card {
    RCV_F(ResourceBackSide.RBS_001),
    RCV_P(ResourceBackSide.RBS_011),
    RCV_A(ResourceBackSide.RBS_021),
    RCV_I(ResourceBackSide.RBS_031);
    private ResourceBackSide backSide;
    ResourceCardVisible(ResourceBackSide backside) {
        this.backSide=backside;
    }

    public Side getFrontSide()
    {
        return this.backSide;
    }

    public Side getBackSide() {
        return this.backSide;
    }
}
