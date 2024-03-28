package it.polimi.is24am05.model.card.starterCard;

import java.util.*;

import it.polimi.is24am05.model.enums.Corner;
import it.polimi.is24am05.model.enums.element.Resource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.is24am05.model.exceptions.card.InvalidCornerException;

class StarterBackSideTest {

    @Test
    void hasCorner() {
        assertTrue(StarterBackSide.SBS_081.hasCorner(Corner.NW));
    }

    @Test
    void hasNoCorner() {
        assertFalse(StarterBackSide.SBS_081.hasCorner(Corner.CE));
    }

    @Test
    void getCorner() throws InvalidCornerException {
        assertEquals(StarterBackSide.SBS_081.getCorner(Corner.NW), List.of(Resource.FUNGI));
    }

    @Test
    void getInvalidCorner() {
        assertThrows(InvalidCornerException.class, () -> StarterBackSide.SBS_081.getCorner(Corner.CE));
    }
}