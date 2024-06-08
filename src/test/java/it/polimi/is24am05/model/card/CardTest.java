package it.polimi.is24am05.model.card;

import it.polimi.is24am05.model.card.goldCard.GoldCard;
import it.polimi.is24am05.model.card.resourceCard.ResourceCard;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    // Test numbers are correctly mapped to the corresponding card
    @Test
    void getCard() {
        Card[] allCards = Stream.concat(Stream.of(ResourceCard.values()), Stream.of(GoldCard.values())).toArray(Card[]::new);

        for(Card card : allCards){
            assertEquals(card, Card.getCard(card.getId()));
        }

        assertThrows(NoSuchElementException.class, () -> Card.getCard(0));
        assertThrows(NoSuchElementException.class, () -> Card.getCard(81));
    }
}