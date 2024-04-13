package it.polimi.is24am05.model.card.goldCard.goldCardMultiplier;

import it.polimi.is24am05.model.card.side.PlacedSide;
import it.polimi.is24am05.model.exceptions.playArea.InvalidCoordinatesException;
import it.polimi.is24am05.model.playArea.PlayArea;
import it.polimi.is24am05.model.playArea.Tuple;

import java.util.Arrays;
import java.util.Map;

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
        // Find last placed card
        Map<Tuple, PlacedSide> placed = playArea.getPlayArea();
        Tuple lastCoords = null;
        int maxTurn = 0;
        for(Tuple t : playArea.getPlayArea().keySet()){
            int currTurn = placed.get(t).getTurnCount();
            if(currTurn > maxTurn){
                maxTurn = currTurn;
                lastCoords = t;
            }
        }

        // Compute the number of covered neighbouring cards
        Tuple[] neighbours;
        try {
            neighbours = PlayArea.getNeighbours(lastCoords);
        } catch (InvalidCoordinatesException e) {
            // Should never happen since Coordinates come directly from the playArea
            throw new RuntimeException(e);
        }

        return (int) Arrays.stream(neighbours).filter(playArea::isOccupied).count();
    }
}
