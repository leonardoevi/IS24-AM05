package it.polimi.is24am05.is24am05.model.enums;

import org.apache.commons.lang3.ArrayUtils;

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
    static Corner[] getEdges() {
        return ArrayUtils.removeElement(Corner.values(), Corner.CE);
    }
}
