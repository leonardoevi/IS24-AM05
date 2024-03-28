package it.polimi.is24am05.model.enums;

import java.util.Arrays;

/**
 * Corners of the card, following cardinal direction notation. The center (CE) is included.
 */
public enum Corner {
    NW,
    NE,
    SE,
    SW,
    CE;

    /**
     * Gets the actual corners of the card, i.e. it removes the center.
     *
     * @return the array of actual corners of the card, i.e. removing the center.
     */
    public static Corner[] getEdges() {
        return Arrays.stream(Corner.values()).filter(x -> x != CE).toArray(Corner[]::new);
    }
}
