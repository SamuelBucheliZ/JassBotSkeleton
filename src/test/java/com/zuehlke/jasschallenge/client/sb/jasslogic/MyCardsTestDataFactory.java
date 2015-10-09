package com.zuehlke.jasschallenge.client.sb.jasslogic;

import com.zuehlke.jasschallenge.client.sb.model.card.CardNumber;
import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.model.card.Suit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MyCardsTestDataFactory {

    public Set<Card> createMyCardsOneHeartThreeSpadesOneClubs() {
        Set<Card> cards = new HashSet<>();

        cards.add(new Card(Suit.SPADES, CardNumber.valueOf(6)));
        cards.add(new Card(Suit.SPADES, CardNumber.valueOf(11)));
        cards.add(new Card(Suit.CLUBS, CardNumber.valueOf(13)));
        cards.add(new Card(Suit.SPADES, CardNumber.valueOf(7)));
        cards.add(new Card(Suit.HEARTS, CardNumber.valueOf(10)));

        return cards;
    }

    public Set<Card> create(Card... cards) {
        Set<Card> myCards = new HashSet<>();
        myCards.addAll(Arrays.asList(cards));
        return myCards;
    }

}
