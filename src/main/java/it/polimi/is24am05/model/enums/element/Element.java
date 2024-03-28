package it.polimi.is24am05.model.enums.element;


import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Implemented by Resource and Element to enable polymorphism.
 */
public interface Element {
    static Element[] values() {
        return Stream.concat(Arrays.stream(Resource.values()), Arrays.stream(Item.values()))
                .toArray(Element[]::new);
    }
}
