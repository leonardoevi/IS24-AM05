package it.polimi.is24am05.model.card;

import it.polimi.is24am05.model.card.goldCard.GoldCard;
import it.polimi.is24am05.model.card.resourceCard.ResourceCard;
import it.polimi.is24am05.model.card.side.Side;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

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

    /**
     * Allow to retrieve a card by specifying its id.
     * @param id Card id []
     * @return  The card that corresponds to the specified id
     * @throws NoSuchElementException if no card has that id
     */
    static Card getCard(int id) throws NoSuchElementException {
        Card[] resource = ResourceCard.values();
        Card[] gold = GoldCard.values();

        return Stream.concat(Stream.of(resource), Stream.of(gold))
                .filter(c -> c.getId() == id).findFirst().orElseThrow(NoSuchElementException::new);
    }
}
