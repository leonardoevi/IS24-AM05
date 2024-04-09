package it.polimi.is24am05.model.deck;

import it.polimi.is24am05.model.Player.HandDisplayer;
import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.goldCard.GoldCard;
import it.polimi.is24am05.model.card.resourceCard.ResourceCard;
import it.polimi.is24am05.model.exceptions.deck.EmptyDeckException;
import it.polimi.is24am05.model.exceptions.deck.InvalidVisibleCardException;
import org.junit.jupiter.api.Test;

import java.util.*;

class DeckDisplayerTest {

    @Test
    void deckToStringTest() {
        Deck deck = new Deck(GoldCard.values());

        System.out.println(HandDisplayer.matrixToString(DeckDisplayer.deckToMatrix(deck, true)));

        while (true){
            try {
                deck.drawTop();
            } catch (EmptyDeckException e) {
                break;
            }
            System.out.println(HandDisplayer.matrixToString(DeckDisplayer.deckToMatrix(deck, true)));
        }
        System.out.println(HandDisplayer.matrixToString(DeckDisplayer.deckToMatrix(deck, true)));
    }

    @Test
    void deckToStringTest2() {
        Deck deck = new Deck(ResourceCard.values());

        System.out.println(HandDisplayer.matrixToString(DeckDisplayer.deckToMatrix(deck, false)));

        List<Card> visible;
        while (true){
            visible = new ArrayList<>(deck.getVisible().stream().toList());
            Collections.shuffle(visible);
            Optional<Card> toDraw = visible.stream().findFirst();

            if(toDraw.isEmpty())
                break;

            try {
                System.out.println("Drawing card: " + toDraw.get().getId() + "\n\n");
                deck.drawVisible(toDraw.get());
            } catch (InvalidVisibleCardException e) {
                throw new RuntimeException(e);
            }
            System.out.println(HandDisplayer.matrixToString(DeckDisplayer.deckToMatrix(deck, false)));
        }
    }
}