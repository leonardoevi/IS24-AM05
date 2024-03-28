package it.polimi.is24am05.model.objective;

import it.polimi.is24am05.model.card.resourceCard.ResourceBackSide;
import it.polimi.is24am05.model.card.resourceCard.ResourceFrontSide;
import it.polimi.is24am05.model.card.starterCard.StarterFrontSide;
import it.polimi.is24am05.model.exceptions.playArea.InvalidCoordinatesException;
import it.polimi.is24am05.model.exceptions.playArea.NoAdjacentCardException;
import it.polimi.is24am05.model.exceptions.playArea.PlacementNotAllowedException;
import it.polimi.is24am05.model.playArea.PlayArea;
import it.polimi.is24am05.model.playArea.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemMultiplierTest {

    @Test
    void compute() throws PlacementNotAllowedException, InvalidCoordinatesException, NoAdjacentCardException {
        // Testing the presence of 3 FUNGI
        // With ObjectiveCard number: 95
        PlayArea playArea = new PlayArea();
        // Testing empty PlayArea
        assertEquals(0, ItemMultiplier.IM_095.compute(playArea));

        playArea.playSide(StarterFrontSide.SFS_081, new Tuple(0,0));
        playArea.playSide(ResourceBackSide.RBS_003, new Tuple(1,1));
        playArea.playSide(ResourceBackSide.RBS_004, new Tuple(-1,-1));
        //Testing only 2 FUNGI
        assertEquals(0, ItemMultiplier.IM_095.compute(playArea));
        playArea.playSide(ResourceBackSide.RBS_005, new Tuple(1,-1));
        playArea.playSide(ResourceBackSide.RBS_006, new Tuple(-1,1));
        //Testing 4 FUNGI
        assertEquals(1, ItemMultiplier.IM_095.compute(playArea));
        playArea.playSide(ResourceBackSide.RBS_005, new Tuple(2,2));
        playArea.playSide(ResourceBackSide.RBS_006, new Tuple(3,3));
        //Testing 6 FUNGI
        assertEquals(2, ItemMultiplier.IM_095.compute(playArea));

        playArea = new PlayArea();
        playArea.playSide(StarterFrontSide.SFS_081, new Tuple(0,0));

        playArea.playSide(ResourceFrontSide.RFS_001, new Tuple(-1,-1));
        playArea.playSide(ResourceFrontSide.RFS_002, new Tuple(-1,1));
        // Testing 4 FUNGI with 2 ResourceFront Cards
        assertEquals(1, ItemMultiplier.IM_095.compute(playArea));
        playArea.playSide(ResourceFrontSide.RFS_003, new Tuple(1,-1));
        // Testing 6 FUNGI with 3 ResourceFront Cards
        assertEquals(2, ItemMultiplier.IM_095.compute(playArea));

        // Testing the presence of a Quill, an Inkwell and a Manuscript
        // with ObjectiveCard number: 99
        playArea = new PlayArea();
        assertEquals(0, ItemMultiplier.IM_099.compute(playArea));
        playArea.playSide(StarterFrontSide.SFS_081, new Tuple(0,0));
        playArea.playSide(ResourceFrontSide.RFS_005, new Tuple(1,1));
        playArea.playSide(ResourceFrontSide.RFS_006, new Tuple(-1,1));
        playArea.playSide(ResourceFrontSide.RFS_007, new Tuple(1,-1));
        // Testing a playArea with one presence for each Item
        assertEquals(1, ItemMultiplier.IM_099.compute(playArea));
        playArea.playSide(ResourceFrontSide.RFS_015, new Tuple(-1,-1));
        // Testing 2 Quill, 1 Manuscript, 1 Inkwell
        assertEquals(1, ItemMultiplier.IM_099.compute(playArea));
    }
}