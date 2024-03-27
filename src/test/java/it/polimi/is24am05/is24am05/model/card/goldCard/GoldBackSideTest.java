package it.polimi.is24am05.is24am05.model.card.goldCard;

import java.util.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.is24am05.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.is24am05.model.enums.Corner;

class GoldBackSideTest {

    @Test
    void hasCorner() {
        assertTrue(GoldBackSide.GBS_041.hasCorner(Corner.CE));
    }

    @Test
    void hasEmptyCorner() {
        assertTrue(GoldBackSide.GBS_041.hasCorner(Corner.NW));
    }

    @Test
    void getCorner() {
        assertEquals(GoldBackSide.GBS_041.getCorner(Corner.CE), List.of(Resource.FUNGI));
    }

    @Test
    void getEmptyCorner() {
        assertEquals(GoldBackSide.GBS_041.getCorner(Corner.NW), List.of());
    }
}