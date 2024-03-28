package it.polimi.is24am05.model.card.goldCard;

import java.util.*;

import it.polimi.is24am05.model.enums.Corner;
import it.polimi.is24am05.model.enums.element.Item;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.is24am05.model.exceptions.card.InvalidCornerException;

class GoldFrontSideTest {

    @Test
    void hasCorner() {
        assertTrue(GoldFrontSide.GFS_041.hasCorner(Corner.SE));
    }

    @Test
    void hasEmptyCorner() {
        assertTrue(GoldFrontSide.GFS_041.hasCorner(Corner.NE));
    }

    @Test
    void hasNoCorner() {
        assertFalse(GoldFrontSide.GFS_041.hasCorner(Corner.NW));
    }

    @Test
    void getCorner() throws InvalidCornerException {
        assertEquals(GoldFrontSide.GFS_041.getCorner(Corner.SE), List.of(Item.QUILL));
    }

    @Test
    void getEmptyCorner() throws InvalidCornerException {
        assertEquals(GoldFrontSide.GFS_041.getCorner(Corner.NE), List.of());
    }

    @Test
    void getInvalidCorner() {
        assertThrows(InvalidCornerException.class, () -> GoldFrontSide.GFS_041.getCorner(Corner.NW));
    }
}