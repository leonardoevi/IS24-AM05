package it.polimi.is24am05.model.card.starterCard;

import java.util.*;

import it.polimi.is24am05.model.enums.Corner;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.is24am05.model.enums.element.Resource;

import it.polimi.is24am05.model.exceptions.card.InvalidCornerException;

class StarterFrontSideTest {

    @Test
    void hasCorner() {
        assertTrue(StarterFrontSide.SFS_081.hasCorner(Corner.CE));
    }

    @Test
    void hasEmptyCorner() {
        assertTrue(StarterFrontSide.SFS_081.hasCorner(Corner.NW));
    }

    @Test
    void hasNoCorner() {
        assertFalse(StarterFrontSide.SFS_085.hasCorner(Corner.SE));
    }

    @Test
    void getCorner() throws InvalidCornerException {
        assertEquals(StarterFrontSide.SFS_081.getCorner(Corner.CE), List.of(Resource.INSECT));
    }

    @Test
    void getEmptyCorner() throws InvalidCornerException {
        assertEquals(StarterFrontSide.SFS_081.getCorner(Corner.SE), List.of());
    }

    @Test
    void getInvalidCorner() {
        assertThrows(InvalidCornerException.class, () -> StarterFrontSide.SFS_085.getCorner(Corner.SE));
    }
}