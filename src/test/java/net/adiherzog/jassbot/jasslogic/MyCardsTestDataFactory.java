package net.adiherzog.jassbot.jasslogic;

import net.adiherzog.jassbot.model.Card;
import net.adiherzog.jassbot.model.Suit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MyCardsTestDataFactory {

    public Set<Card> createMyCardsOneHeartThreeSpadesOneClubs() {
        Set<Card> cards = new HashSet<>();

        cards.add(new Card(Suit.SPADES, 6));
        cards.add(new Card(Suit.SPADES, 11));
        cards.add(new Card(Suit.CLUBS, 13));
        cards.add(new Card(Suit.SPADES, 7));
        cards.add(new Card(Suit.HEARTS, 10));

        return cards;
    }

    public Set<Card> create(Card... cards) {
        Set<Card> myCards = new HashSet<>();
        myCards.addAll(Arrays.asList(cards));
        return myCards;
    }

}
