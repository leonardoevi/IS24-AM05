package it.polimi.is24am05.model.deck;

import it.polimi.is24am05.model.card.goldCard.GoldCard;
import it.polimi.is24am05.model.enums.*;
import it.polimi.is24am05.model.card.*;
import it.polimi.is24am05.model.enums.element.*;
import it.polimi.is24am05.model.exceptions.card.InvalidCornerException;
import it.polimi.is24am05.model.exceptions.deck.*;

import java.io.Serializable;
import java.util.*;

public class Deck implements Serializable {
    private final List<Card> deck;
    private final Set<Card> visible;

    /**
     * Initializes the deck with the array of cards provided as parameter,
     * Shuffles the deck,
     * Reveals the top two cards,
     @param values array of cards to add in the deck
     */
    public Deck(Card[] values) {
        this.deck = new LinkedList<>();
        this.visible = new HashSet<>();

        // For good measure
        if(values == null)
            values = new Card[]{};

        // Filling up the list with provided values
        this.deck.addAll(List.of(values));
        Collections.shuffle(deck);

        // Revealing first two cards
        for(int i=0; i < 2; i++) {
            try {
                Card toReveal = this.drawTop();
                this.deck.remove(toReveal);
                visible.add(toReveal);
            } catch (EmptyDeckException ignored) {}
        }
    }

    /**
     * Ignores the presence of visible cards
     * @return True if there are no more cards in the deck
     */
    public boolean isEmpty(){
        return this.deck.isEmpty();
    }

    /**
     * @return the set of visible cards
     */
    public Set<Card> getVisible() {
        return visible;
    }

    /**
     * Removes and returns the top card in the deck
     * @return the top card in the deck
     * @throws EmptyDeckException if no more cards are left in the deck
     */
    public Card drawTop() throws EmptyDeckException {
        //check if there are still cards in the deck
        if (this.isEmpty())
            throw new EmptyDeckException();

        //select and return top card
        Card top = deck.getFirst();
        this.deck.remove(top);
        return top;
    }

    /**
     * Removes the chosen cards from the set of visible cards.
     * Reveals a new card if any is left in the deck.
     * @param choice card to be drawn from the set of visible cards
     * @return the same card that was chosen
     * @throws InvalidVisibleCardException if the chosen card is not a visible card
     */
    public Card drawVisible(Card choice) throws InvalidVisibleCardException {
        // Check if chosen card is visible
        if(!visible.contains(choice))
            throw new InvalidVisibleCardException();

        // Remove choice from visible cards
        this.visible.remove(choice);

        // Reveal next card
        if(!this.isEmpty())
            try {
                visible.add(this.drawTop());
            } catch (EmptyDeckException ignored) {}

        // Return drawn card (which corresponds to the parameter)
        return choice;
    }

    /**
     * @return the "seed" (Resource) on the back of the top card in the deck
     * @throws EmptyDeckException if there are no more cards in the deck
     */
    public Element peek() throws EmptyDeckException {
        if(this.isEmpty())
            throw new EmptyDeckException();
        try {
            return this.deck.getFirst().getBackSide().getCorner(Corner.CE).getFirst();
        } catch (InvalidCornerException ignored) { return null; }
    }
    public String[][] toMatrix()
    {
        boolean isGold;
        if(this.getVisible().getClass().equals(GoldCard.class))
             isGold=true;
        else
            isGold=false;
        return DeckDisplayer.deckToMatrix(this, isGold);
    }
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for(String[] row : this.toMatrix()){
            for (String s : row){
                sb.append(s);
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    }

