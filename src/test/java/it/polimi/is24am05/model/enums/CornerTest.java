package it.polimi.is24am05.model.enums;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class CornerTest {

    @Test
    void getEdges() {
        assertArrayEquals(Corner.getEdges(), new Corner[] {Corner.NW, Corner.NE, Corner.SE, Corner.SW});
    }
}