package it.polimi.is24am05.model.deck;

import it.polimi.is24am05.model.card.*;
import it.polimi.is24am05.model.card.resourceCard.*;
import it.polimi.is24am05.model.enums.*;
import it.polimi.is24am05.model.exceptions.card.InvalidCornerException;
import it.polimi.is24am05.model.exceptions.deck.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class DeckTest {

    @Test
    void isEmpty() {
        // Test null parameter to constructor
        Deck d1 = new Deck(null);

        // Create a new deck with only two cards
        Card[] d1Cards = new Card[]{ResourceCard.RC_001, ResourceCard.RC_015};
        d1 = new Deck(d1Cards);
        // Deck should be empty
        assertTrue(d1.isEmpty());

        // Create a new deck with 3 cards
        Deck d2 = new Deck(new ResourceCard[]{ResourceCard.RC_001, ResourceCard.RC_015, ResourceCard.RC_028});
        // Deck should not be empty
        assertFalse(d2.isEmpty());
        // Draw one card
        try {
            d2.drawTop();
        } catch (EmptyDeckException ignored) {}
        // Deck should be empty
        assertTrue(d2.isEmpty());
    }

    @Test
    void getVisible() {
        // Create a new deck with only two cards
        Card[] d1Cards = new Card[]{ResourceCard.RC_001, ResourceCard.RC_015};
        Deck d1 = new Deck(d1Cards);
        // All cards added should be visible
        assertTrue(d1.getVisible().containsAll(List.of(d1Cards)));
        assertTrue(List.of(d1Cards).containsAll(d1.getVisible()));

        // Create a new array of cards
        Card[] d2Cards = ResourceCard.values();

        // Repeat 20 times
        for(int i=0; i<20; i++){
            // Create new deck
            Deck d2 = new Deck(d2Cards);
            // Check if visible cards are part of the initial set of cards
            assertTrue(List.of(d2Cards).containsAll(d2.getVisible()));
        }

        Deck d2 = new Deck(d2Cards);
        // Draw all covered cards
        while(!d2.isEmpty()) {
            try {
                d2.drawTop();
            } catch (EmptyDeckException ignored) {}
            // There should always be 2 visible cards
            assertEquals(2, d2.getVisible().size());
        }

        // Deck should be empty
        assertTrue(d2.isEmpty());

        // Draw one visible card
        try {
            d2.drawVisible(d2.getVisible().stream().findFirst().orElse(null));
        } catch (InvalidVisibleCardException ignored) {}
        // Only 1 visible cards should be left
        assertEquals(1,d2.getVisible().size());
        // Deck should be empty
        assertTrue(d2.isEmpty());

        // Draw one last card
        try {
            d2.drawVisible(d2.getVisible().stream().findFirst().orElse(null));
        } catch (InvalidVisibleCardException ignored) {}
        // No visible cards should be left
        assertEquals(0,d2.getVisible().size());
        // Deck should be empty
        assertTrue(d2.isEmpty());

    }

    @Test
    void drawTop() {
        for(int N=20; N <100; N++) {
            // Create a new array of cards
            Card[] Cards = ResourceCard.values();

            // Create deck of cards
            Deck d1 = new Deck(Cards);

            // Drawing all cards, except for 2, should not throw any Exception
            for (int i = 0; i < ResourceCard.values().length - 2; i++) {
                assertDoesNotThrow(d1::drawTop);
            }
            // Drawing top cards should now generate an EmptyDeckException
            assertThrows(EmptyDeckException.class, d1::drawTop);
        }

        // Try with a deck of only 2 Cards
        Card[] Cards = new Card[]{ResourceCard.RC_028, ResourceCard.RC_015};
        Deck d1 = new Deck(Cards);
        // Drawing cards should generate an EmptyDeckException
        assertThrows(EmptyDeckException.class, d1::drawTop);

    }

    @Test
    void drawVisible() {
        // Create a Deck with no cards
        Deck d1 = new Deck(new Card[]{});
        // Drawing a visible card should generate an exception
        for(int i=0; i<100; i++){
            Card tmpCard = ResourceCard.values()[0];
            assertThrows(InvalidVisibleCardException.class, () -> d1.drawVisible(tmpCard) );
        }
        // Passing a null parameter should generate an InvalidVisibleCardException
        assertThrows(InvalidVisibleCardException.class, () -> d1.drawVisible(null) );

        // Create a Deck of cards
        Deck d2 = new Deck(ResourceCard.values());

        // Draw a visible card
        Card toDraw = d2.getVisible().stream().findFirst().orElse(null);
        try {
            // Card drawn should be returned
            assertEquals(toDraw, d2.drawVisible(toDraw));
            // Card drawn should be removed from visible cards
            assertFalse(d2.getVisible().contains(toDraw));
            // Drawing the same card again should generate an Exception
            Card finalToDraw0 = toDraw;
            assertThrows(InvalidVisibleCardException.class, () -> d2.drawVisible(finalToDraw0));
            // There should still be 2 visible cards
            assertEquals(2,d2.getVisible().size());
        } catch (InvalidVisibleCardException ignored) {}

        // Drawing all cards from the deck, only drawing visible cards, should not generate exceptions
        while (!d2.isEmpty()) {
            toDraw = d2.getVisible().stream().findFirst().orElse(null);
            Card finalToDraw = toDraw;
            Assertions.assertDoesNotThrow(() -> d2.drawVisible(finalToDraw));
        }
        // Only the two visible cards should be left
        assertTrue(d2.isEmpty());
        assertEquals(2, d2.getVisible().size());

        // Drawing one of the visible cards should not generate Exceptions
        toDraw = d2.getVisible().stream().findFirst().orElse(null);
        Card finalToDraw1 = toDraw;
        Assertions.assertDoesNotThrow(() -> d2.drawVisible(finalToDraw1));

        // Only one visible card should be left
        assertEquals(1, d2.getVisible().size());

        // Drawing last visible card should not generate Exceptions
        toDraw = d2.getVisible().stream().findFirst().orElse(null);
        Card finalToDraw2 = toDraw;
        Assertions.assertDoesNotThrow(() -> d2.drawVisible(finalToDraw2));

        // No more visible cards should be left
        assertEquals(0, d2.getVisible().size());

    }

    @Test
    void peek() {
        // Create a Deck of  cards
        Deck d = new Deck(ResourceCard.values());

        // Check if peek return value is correct by drawing the top card
        while (!d.isEmpty()){
            try {
                assertEquals(d.peek(), d.drawTop().getBackSide().getCorner(Corner.CE).getFirst());
            } catch (EmptyDeckException | InvalidCornerException ignored) {}
        }

        // Peek should now throw an EmptyDeckException
        assertThrows(EmptyDeckException.class, d::peek);
    }
}