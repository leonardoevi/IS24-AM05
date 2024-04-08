package it.polimi.is24am05.model.Player;

import it.polimi.is24am05.model.card.Card;
import it.polimi.is24am05.model.card.goldCard.GoldCard;
import it.polimi.is24am05.model.card.resourceCard.ResourceCard;
import it.polimi.is24am05.model.card.starterCard.StarterCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.is24am05.model.Player.HandDisplayer.CardToString;

class HandDisplayerTest {

    @Test
    void starterCardToStringTest() {
        for(StarterCard s : StarterCard.values())
            System.out.println(CardToString(s));
    }

    @Test
    void handToStringTest(){
        List<Card> hand = new ArrayList<>();
        hand.add(ResourceCard.RC_027);
        hand.add(GoldCard.GC_043);
        hand.add(ResourceCard.RC_038);

        System.out.println(HandDisplayer.handToString(hand));
    }

    @Test
    void titleID(){
        for(Card c : ResourceCard.values())
            System.out.println(String.format("%02d", c.getId()));
    }
}