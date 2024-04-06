package it.polimi.is24am05.model.playArea;

import it.polimi.is24am05.model.card.goldCard.GoldBackSide;
import it.polimi.is24am05.model.card.resourceCard.*;
import it.polimi.is24am05.model.card.side.EmptyPlacedSide;
import it.polimi.is24am05.model.card.side.PlacedSide;
import it.polimi.is24am05.model.card.side.Side;
import it.polimi.is24am05.model.card.starterCard.StarterFrontSide;
import it.polimi.is24am05.model.enums.Corner;
import it.polimi.is24am05.model.enums.element.*;
import it.polimi.is24am05.model.exceptions.card.InvalidCornerException;
import it.polimi.is24am05.model.exceptions.playArea.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayAreaTest {

    @Test
    void getCoveredCorner() {
        // Create a matrix of Tuples
        Tuple[][] coords = new Tuple[10][10];

        // Fill up the matrix
        for(int i=0; i<10; i++){
            for(int j = 0; j < 10; j++){
                coords[i][j] = new Tuple(i,j);
            }
        }

        try {
            Assertions.assertEquals(Corner.SE, PlayArea.getCoveredCorner(coords[0][0], coords[1][1]));
            Assertions.assertEquals(Corner.NW, PlayArea.getCoveredCorner(coords[1][1], coords[0][0]));
            Assertions.assertEquals(Corner.NE, PlayArea.getCoveredCorner(coords[2][0], coords[1][1]));
            Assertions.assertEquals(Corner.SW, PlayArea.getCoveredCorner(coords[1][1], coords[2][0]));

            Assertions.assertEquals(Corner.SE, PlayArea.getCoveredCorner(new Tuple(-1,-1), coords[0][0]));
        } catch (InvalidCoordinatesException ignored) {}


        assertThrows(InvalidCoordinatesException.class, () -> PlayArea.getCoveredCorner(coords[0][0],coords[2][2]));
    }

    @Test
    void playSide() throws PlacementNotAllowedException, InvalidCoordinatesException, NoAdjacentCardException {
        PlayArea playArea = new PlayArea();

        // Try placing a card outside the frontier
        assertThrows(NoAdjacentCardException.class, () -> playArea.playSide(ResourceBackSide.RBS_001, new Tuple(1,1)));

        // Try placing a non Starter card
        assertThrows(PlacementNotAllowedException.class, () -> playArea.playSide(ResourceBackSide.RBS_001, new Tuple(0,0)));

        // Place a card in (0 0)
        assertEquals(0,playArea.playSide(StarterFrontSide.SFS_081, new Tuple(0,0)));

        // Place a card in a "black" position
        assertThrows(InvalidCoordinatesException.class, () -> playArea.playSide(ResourceFrontSide.RFS_003, new Tuple(0,-1)));

        // Place a card in (1, -1)
        assertEquals(0,playArea.playSide(ResourceFrontSide.RFS_001, new Tuple(1,-1)));

        // Place a card in a blocked position (2 0)
        assertThrows(PlacementNotAllowedException.class, () -> playArea.playSide(ResourceFrontSide.RFS_003, new Tuple(2,0)));

        // Verify visible elements
        Map<Element, Integer> visibleElements = playArea.getVisibleElements();
        for(Item I : Item.values())
            assertEquals(0, visibleElements.get(I));
        assertEquals(0, visibleElements.get(Resource.ANIMAL));
        assertEquals(2, visibleElements.get(Resource.FUNGI));
        assertEquals(1, visibleElements.get(Resource.INSECT));
        assertEquals(1, visibleElements.get(Resource.PLANT));

        // Place a card in (0, -2)
        assertEquals(1,playArea.playSide(ResourceFrontSide.RFS_028, new Tuple(0,-2)));

    }

    @Test
    void isFree() throws PlacementNotAllowedException, InvalidCoordinatesException, NoAdjacentCardException {
        PlayArea playArea = new PlayArea();

        // Place a card in (0 0)
        assertEquals(0,playArea.playSide(StarterFrontSide.SFS_081, new Tuple(0,0)));
        // Place a card in (-1, -1)
        assertEquals(0,playArea.playSide(ResourceFrontSide.RFS_003, new Tuple(-1,-1)));
        // Place a card in (-1, 1)
        assertEquals(0,playArea.playSide(ResourceFrontSide.RFS_001, new Tuple(-1,1)));

        // Cells where there is a card should not be free
        assertFalse(playArea.isFree(new Tuple(0,0)));
        assertFalse(playArea.isFree(new Tuple(-1,-1)));
        assertFalse(playArea.isFree(new Tuple(-1,1)));

        // Cells adjacent to a card that doesn't have the corner should not be free
        assertFalse(playArea.isFree(new Tuple(0,2)));
        assertFalse(playArea.isFree(new Tuple(-2,0)));

        // Verify frontier is free
        assertTrue(playArea.isFree(new Tuple(-2, 2)));
        assertTrue(playArea.isFree(new Tuple(1, -1)));
        assertTrue(playArea.isFree(new Tuple(1, -1)));
        assertTrue(playArea.isFree(new Tuple(0, -2)));
        assertTrue(playArea.isFree(new Tuple(-2, -2)));
    }

    @Test
    void getMatrixPlayArea() throws PlacementNotAllowedException, InvalidCoordinatesException, NoAdjacentCardException {
        PlayArea playArea = new PlayArea();

        PlacedSide[][] matrix = playArea.getMatrixPlayArea();

        assertEquals(5, matrix.length);
        assertEquals(5, matrix[0].length);

            // Place three cards

        // Place a card in (0 0)
        assertEquals(0,playArea.playSide(StarterFrontSide.SFS_081, new Tuple(0,0)));

        // Verify the correctness of matrix dimensions
        matrix = playArea.getMatrixPlayArea();
        assertEquals(5, matrix.length);
        assertEquals(5, matrix[0].length);

        // Place a card in (-1, -1)
        assertEquals(0,playArea.playSide(ResourceFrontSide.RFS_003, new Tuple(-1,-1)));
        // Place a card in (-1, 1)
        assertEquals(0,playArea.playSide(ResourceFrontSide.RFS_001, new Tuple(-1,1)));

        matrix = playArea.getMatrixPlayArea();

        // Verify all three cards are in the matrix
        Set<Side> set = new HashSet<>();
        for(PlacedSide[] row : matrix){
            for (PlacedSide p : row){
                if(p != null && !(p instanceof EmptyPlacedSide))
                    set.add(p.getSide());
            }
        }
        assertEquals(3, set.size());
        assertTrue(set.contains(StarterFrontSide.SFS_081));
        assertTrue(set.contains(ResourceFrontSide.RFS_003));
        assertTrue(set.contains(ResourceFrontSide.RFS_001));

        // Create a new PlayaArea
        PlayArea playArea1 = new PlayArea();

        // Add a starting Card and 201 Cards in a "straight" line to the right
        playArea1.playSide(StarterFrontSide.SFS_081, new Tuple(0,0));
        playArea1.playSide(ResourceBackSide.RBS_001, new Tuple(1,1));
        int i = 0;
        int j = 2;
        for(int k=0; k<100; k++){
            playArea1.playSide(GoldBackSide.GBS_054, new Tuple(i,j));
            i = i+1;
            j = j+1;
            playArea1.playSide(GoldBackSide.GBS_054, new Tuple(i,j));
            i = 0;
            j = j+1;
        }

        matrix = playArea1.getMatrixPlayArea();

        // Verify the correctness of matrix dimensions
        assertEquals(6, matrix.length);
        assertEquals(206, matrix[0].length);

        // Create a new PlayaArea
        PlayArea playArea2 = new PlayArea();
        playArea2.playSide(StarterFrontSide.SFS_084, new Tuple(0,0));

        // Add cards randomly
        for(i=0; i<100; i++){
            Tuple coord = PlayAreaTest.getRandomElement(playArea2.getFrontier());
            playArea2.playSide(GoldBackSide.GBS_041, coord);
        }

        matrix = playArea2.getMatrixPlayArea();

        // Verify all 100 cards have been placed
        long num = Arrays.stream(matrix).flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .filter(p ->! (p instanceof EmptyPlacedSide))
                .count();
        assertEquals(101, num);

        // Verify visibility of elements in the matrix too
        for(Element element : Element.values())
            assertEquals((long) playArea2.getVisibleElements().get(element), matrixCount(matrix, element));
    }

    // Helpful functions

    /**
     * @param set Set to choose an element from, must contain at least one element
     * @return A random element from the set
     */
    private static Tuple getRandomElement(Set<Tuple> set){
        Tuple retVal = null;

        int randomIndex = new Random().nextInt(set.size());
        int i = 0;
        for (Tuple element : set) {
            if (i == randomIndex) {
                retVal = element;
                break;
            }
            i++;
        }
        return retVal;
    }

    /**
     * @param matrix Matrix to count Element from
     * @param toCount Element to count
     * @return The number of times the specified element is visible at the CENTER of a card that is present in the matrix
     */
    private static long matrixCount(PlacedSide[][] matrix, Element toCount){
        return Arrays.stream(matrix).flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .filter(p ->! (p instanceof EmptyPlacedSide))
                .map(PlacedSide::getSide)
                .flatMap(side -> {
                    try {
                        return side.getCorner(Corner.CE).stream();
                    } catch (InvalidCornerException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(x -> x == toCount)
                .count();
    }
}