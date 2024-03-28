package it.polimi.is24am05.model.card.resourceCard;

import java.util.*;

import it.polimi.is24am05.model.enums.Corner;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.is24am05.model.enums.element.Resource;

class ResourceBackSideTest {

    @Test
    void hasCorner() {
        assertTrue(ResourceBackSide.RBS_001.hasCorner(Corner.CE));
    }

    @Test
    void hasEmptyCorner() {
        assertTrue(ResourceBackSide.RBS_001.hasCorner(Corner.NW));
    }

    @Test
    void getCorner() {
        assertEquals(ResourceBackSide.RBS_001.getCorner(Corner.CE), List.of(Resource.FUNGI));
    }

    @Test
    void getEmptyCorner() {
        assertEquals(ResourceBackSide.RBS_001.getCorner(Corner.NW), List.of());
    }
}