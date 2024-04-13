package it.polimi.is24am05.model.deck;

import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.exceptions.deck.EmptyDeckException;
import it.polimi.is24am05.model.playArea.AreaDisplayer;
import it.polimi.is24am05.model.playArea.SideDisplayer;
import it.polimi.is24am05.model.playArea.Tuple;

import java.util.List;
import java.util.Set;

import static it.polimi.is24am05.model.Player.HandDisplayer.matrixToString;
import static it.polimi.is24am05.model.Player.HandDisplayer.sideToSide;
import static it.polimi.is24am05.model.playArea.SideDisplayer.BLACK_BOLD_BRIGHT;
import static it.polimi.is24am05.model.playArea.SideDisplayer.RESET;

public class DeckDisplayer {
    /**
     * @return A Matrix of strings representing a Deck. TopCard back side and visible cards front side.
     */
    public static String[][] deckToMatrix(Deck deck, boolean isGold) {
        // Get card side dimensions
        String[][] side = emptyDeckTopSide();
        int rows = side.length;
        int cols = side[0].length;

        Set<Card> visibleCards = deck.getVisible();

        // Allocate the matrix to return
        String[][] toReturn = new String[3*rows +2][cols];
        String[][] spacer = {{" ", "       ", " "}};
        for(int i=0; i< toReturn.length; i++)
            AreaDisplayer.put(spacer, toReturn, i, 0);

        // Draw deck back side
        try {
            side = SideDisplayer.sideToString((Resource) deck.peek(), isGold);
        } catch (EmptyDeckException ignored) {}

        AreaDisplayer.put(side, toReturn, 0,0);

        int k = rows;
        for(Card visible : visibleCards){
            toReturn[k][1] = toReturn[k][2] = "";
            toReturn[k][0] = "CardID:" + String.format("%02d", visible.getId());
            k++;

            side = SideDisplayer.sideToString(visible.getFrontSide());
            /*
            HandDisplayer.editTopEdge(side, visible.getFrontSide());
            HandDisplayer.editBottomEdge(side, visible.getFrontSide());
            HandDisplayer.editCentre(side, visible.getFrontSide());
             */

            AreaDisplayer.put(side, toReturn, k, 0);

            k += rows;
        }

        return toReturn;
    }

    public static String deckToString(Deck deck, boolean isGold){
        return matrixToString(deckToMatrix(deck, isGold));
    }

    public static String deckToString(Deck deck1, boolean isGold1, Deck deck2, boolean isGold2){
        return matrixToString(sideToSide(List.of(deckToMatrix(deck1, isGold1), deckToMatrix(deck2, isGold2) )));
    }

    private static String[][] emptyDeckTopSide(){
        String[][] side = SideDisplayer.emptySideToString(new Tuple(0,0));
        side[1][1] = BLACK_BOLD_BRIGHT + " empty " + RESET;
        return side;
    }
}
