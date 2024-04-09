package it.polimi.is24am05.model.card.goldCard.goldCardMultiplier;

import it.polimi.is24am05.model.enums.element.Item;

import it.polimi.is24am05.model.playArea.PlayArea;
//ITEM WILL BE USED IN COMPUTE METHOD, TO BE IMPLEMENTED
/**
 * Gold card multipliers that count the number of occurrences of an item on the play area.
 */
public enum ItemMultiplier implements GoldCardMultiplier {
    QUILL(Item.QUILL),
    INKWELL(Item.INKWELL),
    MANUSCRIPT(Item.MANUSCRIPT);

    /**
     * Item to count.
     */
    private final Item item;

    /**
     * Constructor.
     *
     * @param item item to count.
     */
    ItemMultiplier(Item item) {
        this.item = item;
    }

    /**
     * Computes the multiplication factor, i.e. the number of occurrences of an item on the play area.
     *
     * @param playArea the play area where the side was placed.
     * @return the multiplication factor, i.e. the number of occurrences of an item on the play area.
     */
    @Override
    public int compute(PlayArea playArea) {
        return 1;
    }

    public Item getItem() {
        return this.item;
    }


}
