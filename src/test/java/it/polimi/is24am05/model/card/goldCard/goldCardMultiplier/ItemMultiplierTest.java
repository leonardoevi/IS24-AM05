package it.polimi.is24am05.model.card.goldCard.goldCardMultiplier;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.is24am05.model.card.starterCard.StarterFrontSide;
import it.polimi.is24am05.model.card.resourceCard.ResourceFrontSide;
import it.polimi.is24am05.model.card.goldCard.GoldFrontSide;
import it.polimi.is24am05.model.playArea.PlayArea;
import it.polimi.is24am05.model.playArea.Tuple;

import it.polimi.is24am05.model.exceptions.playArea.PlacementNotAllowedException;
import it.polimi.is24am05.model.exceptions.playArea.InvalidCoordinatesException;
import it.polimi.is24am05.model.exceptions.playArea.NoAdjacentCardException;

class ItemMultiplierTest {

    @Test
    void compute() throws PlacementNotAllowedException, InvalidCoordinatesException, NoAdjacentCardException {
        PlayArea playArea = new PlayArea();

        playArea.playSide(StarterFrontSide.SFS_081, new Tuple(0,0));
        playArea.playSide(ResourceFrontSide.RFS_005, new Tuple(-1,1));
        playArea.playSide(ResourceFrontSide.RFS_001, new Tuple(-1,-1));
        playArea.playSide(ResourceFrontSide.RFS_021, new Tuple(1,-1));
        //System.out.println(new AreaDisplayer(playArea));
        playArea.playSide(GoldFrontSide.GFS_041, new Tuple(1,1));
        assertEquals(ItemMultiplier.QUILL.compute(playArea), 2);
    }
}