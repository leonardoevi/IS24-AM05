package it.polimi.is24am05.model.objective;

import it.polimi.is24am05.model.card.goldCard.GoldBackSide;
import it.polimi.is24am05.model.card.resourceCard.ResourceBackSide;
import it.polimi.is24am05.model.card.starterCard.StarterFrontSide;
import it.polimi.is24am05.model.exceptions.playArea.InvalidCoordinatesException;
import it.polimi.is24am05.model.exceptions.playArea.NoAdjacentCardException;
import it.polimi.is24am05.model.exceptions.playArea.PlacementNotAllowedException;
import it.polimi.is24am05.model.playArea.PlayArea;
import it.polimi.is24am05.model.playArea.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourcesMultiplierTest {
    // Testing compute() method using only the ResourceBackCards and GoldBackCards
    // to make the placement in playArea easier
    @Test
    void compute() throws PlacementNotAllowedException, InvalidCoordinatesException, NoAdjacentCardException {
        // Testing 3 Insect Cards in position: [0,0], [1,1], [2,2]
        // With ObjectiveCard number: 90
        PlayArea playArea = new PlayArea();
        // Testing empty PlayArea
        assertEquals(0, ResourcesMultiplier.OM_090.compute(playArea));
        playArea.playSide(StarterFrontSide.SFS_081, new Tuple(0,0));
        playArea.playSide(ResourceBackSide.RBS_033, new Tuple(1,-1));

        // Testing PlayArea as a matrix smaller than the 3x3 Resource matrix
        assertEquals(0, ResourcesMultiplier.OM_090.compute(playArea));
        playArea.playSide(ResourceBackSide.RBS_032, new Tuple(1,1));
        playArea.playSide(ResourceBackSide.RBS_035, new Tuple(2,0));
        playArea.playSide(ResourceBackSide.RBS_036, new Tuple(3,1));
        playArea.playSide(ResourceBackSide.RBS_037, new Tuple(3,-1));
        // Testing 3 insect cards placed in the right way
        assertEquals(1, ResourcesMultiplier.OM_090.compute(playArea));

        // Changing Seed needed
        assertEquals(0, ResourcesMultiplier.OM_088.compute(playArea));

        playArea.playSide(ResourceBackSide.RBS_038, new Tuple(4,2));
        // Testing 4 insect cards placed in the right way
        assertEquals(1, ResourcesMultiplier.OM_090.compute(playArea));

        playArea.playSide(ResourceBackSide.RBS_039, new Tuple(5,3));
        // Testing 5 insect cards placed in the right way
        assertEquals(1, ResourcesMultiplier.OM_090.compute(playArea));

        playArea.playSide(ResourceBackSide.RBS_040, new Tuple(6,4));
        // Testing 6 insect cards placed in the right way
        assertEquals(2, ResourcesMultiplier.OM_090.compute(playArea));

        playArea.playSide(GoldBackSide.GBS_071, new Tuple(2,2));
        playArea.playSide(GoldBackSide.GBS_072, new Tuple(3,3));
        // Testing 6 insect cards placed in the right way and other 3 placed far away
        assertEquals(3, ResourcesMultiplier.OM_090.compute(playArea));

        // Testing 2 Fungi Card in position : [0,0], [1,0] and a Plant Card in position [2,1]
        // With Objective number: 91
        playArea = new PlayArea();
        // Testing empty PlayArea
        assertEquals(0, ResourcesMultiplier.OM_091.compute(playArea));

        playArea.playSide(StarterFrontSide.SFS_081, new Tuple(0,0));
        playArea.playSide(ResourceBackSide.RBS_003, new Tuple(1,1));
        playArea.playSide(ResourceBackSide.RBS_004, new Tuple(-1,-1));
        playArea.playSide(ResourceBackSide.RBS_005, new Tuple(1,-1));
        playArea.playSide(ResourceBackSide.RBS_006, new Tuple(-1,1));

        // Testing PlayArea as a matrix smaller than the 3x3 Resource matrix
        assertEquals(0, ResourcesMultiplier.OM_091.compute(playArea));

        playArea.playSide(ResourceBackSide.RBS_011, new Tuple(2,2));
        // Testing one condition right
        assertEquals(1, ResourcesMultiplier.OM_091.compute(playArea));

        playArea.playSide(ResourceBackSide.RBS_012, new Tuple(2,0));
        // Testing two conditions right
        assertEquals(2, ResourcesMultiplier.OM_091.compute(playArea));
        // Testing two disposition overlapped and one far away which should return only 2 matches
        playArea.playSide(ResourceBackSide.RBS_007, new Tuple(3,-1));
        playArea.playSide(ResourceBackSide.RBS_013, new Tuple(4,0));
        assertEquals(2, ResourcesMultiplier.OM_091.compute(playArea));
    }
}