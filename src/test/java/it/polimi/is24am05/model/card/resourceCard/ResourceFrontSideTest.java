package it.polimi.is24am05.model.card.resourceCard;

import java.util.*;

import it.polimi.is24am05.model.enums.Corner;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.is24am05.model.enums.element.Resource;

import it.polimi.is24am05.model.exceptions.card.InvalidCornerException;

class ResourceFrontSideTest {

    @Test
    void hasCorner() {
        assertTrue(ResourceFrontSide.RFS_001.hasCorner(Corner.NW));
    }

    @Test
    void hasEmptyCorner() {
        assertTrue(ResourceFrontSide.RFS_001.hasCorner(Corner.NE));
    }

    @Test
    void hasNoCorner() {
        assertFalse(ResourceFrontSide.RFS_001.hasCorner(Corner.SE));
    }

    @Test
    void getCorner() throws InvalidCornerException {
        assertEquals(ResourceFrontSide.RFS_001.getCorner(Corner.NW), List.of(Resource.FUNGI));
    }

    @Test
    void getEmptyCorner() throws InvalidCornerException {
        assertEquals(ResourceFrontSide.RFS_001.getCorner(Corner.NE), List.of());
    }

    @Test
    void getInvalidCorner() {
        assertThrows(InvalidCornerException.class, () -> ResourceFrontSide.RFS_001.getCorner(Corner.SE));
    }
    @Test
    void getSeed(){ assertEquals(Resource.FUNGI, ResourceFrontSide.RFS_001.getSeed());
                    assertEquals(Resource.FUNGI, ResourceFrontSide.RFS_010.getSeed());
                    assertEquals(Resource.PLANT, ResourceFrontSide.RFS_011.getSeed());
                    assertEquals(Resource.PLANT, ResourceFrontSide.RFS_020.getSeed());
                    assertEquals(Resource.ANIMAL, ResourceFrontSide.RFS_021.getSeed());
                    assertEquals(Resource.ANIMAL, ResourceFrontSide.RFS_030.getSeed());
                    assertEquals(Resource.INSECT, ResourceFrontSide.RFS_031.getSeed());
                    assertEquals(Resource.INSECT, ResourceFrontSide.RFS_040.getSeed());
    }
}