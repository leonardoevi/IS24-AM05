package it.polimi.is24am05.model.card.goldCard;

import java.util.*;

import it.polimi.is24am05.model.enums.Corner;
import it.polimi.is24am05.model.enums.element.Item;
import it.polimi.is24am05.model.enums.element.Resource;
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
    @Test
    void getSeed(){ assertEquals(Resource.FUNGI, GoldFrontSide.GFS_041.getSeed());
                    assertEquals(Resource.FUNGI, GoldFrontSide.GFS_050.getSeed());
                    assertEquals(Resource.PLANT, GoldFrontSide.GFS_051.getSeed());
                    assertEquals(Resource.PLANT, GoldFrontSide.GFS_060.getSeed());
                    assertEquals(Resource.ANIMAL, GoldFrontSide.GFS_061.getSeed());
                    assertEquals(Resource.ANIMAL, GoldFrontSide.GFS_070.getSeed());
                    assertEquals(Resource.INSECT, GoldFrontSide.GFS_071.getSeed());
                    assertEquals(Resource.INSECT, GoldFrontSide.GFS_080.getSeed());
    }
}