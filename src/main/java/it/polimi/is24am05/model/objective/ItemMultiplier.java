package it.polimi.is24am05.model.objective;

import it.polimi.is24am05.model.enums.element.Element;
import it.polimi.is24am05.model.enums.element.Item;
import it.polimi.is24am05.model.enums.element.Resource;
import it.polimi.is24am05.model.playArea.PlayArea;

import java.util.HashMap;
import java.util.Map;

public enum ItemMultiplier implements ObjectiveMultiplier {
    /**
     * The ItemMultiplier enum declares the 8 Objective
     * multiplier which are characterized by their point
     * condition. Each condition can be represented in a
     * Map where the keys are the Resource/Items needed
     * and the values are how many times those Elements
     * need to be found in the PlayerArea.
     */
    IM_095(Map.of(Resource.FUNGI, 3)),
    IM_096(Map.of(Resource.PLANT, 3)),
    IM_097(Map.of(Resource.ANIMAL, 3)),
    IM_098(Map.of(Resource.INSECT, 3)),
    IM_099(Map.of(Item.QUILL, 1,
                  Item.MANUSCRIPT, 1,
                  Item.INKWELL, 1)),
    IM_100(Map.of(Item.MANUSCRIPT, 2)),
    IM_101(Map.of(Item.INKWELL, 2)),
    IM_102(Map.of(Item.QUILL, 2));
    /**
     * The attribute objects is a Map where the keys are the elements
     * needed to be in the PlayerArea and the values are how many times each element
     * needs to be in the PlayerArea
     */
    private final Map<Element, Integer> objects;

    /**
     * Initializes an ItemMultiplier
     * @param objects attribute for the constructor
     */
    ItemMultiplier(Map<Element, Integer> objects) {
        this.objects = objects;
    }

    /**
     * Counts how many times the points condition is satisfied
     * @param playArea is the map returned from the method getElements()
     * @return how many times the points condition is satisfied
     */
    @Override
    public int compute(PlayArea playArea) {
        Map<Element, Integer> parameter = playArea.getVisibleElements();

        Map<Element, Integer> copy = new HashMap<>();
        int n;
        for(Element el: objects.keySet()){
            if (parameter.get(el)== null) {
                    n = 0;
            }else {
                    n = parameter.get(el);
            }

            copy.put(el, n / objects.get(el));

            }
        return copy.values()
                    .stream()
                    .min(Integer::compareTo)
                    .orElse(0);

    }
}
