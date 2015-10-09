package com.zuehlke.jasschallenge.client.sb.jasslogic;

import com.zuehlke.jasschallenge.client.sb.model.card.CardNumber;
import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.model.card.Suit;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CardsOnTableTestDataFactory {

    public List<Card> createCardsOnTableThreeSpades() {
        List<Card> cards = new LinkedList<>();

        cards.add(new Card(Suit.SPADES, CardNumber.valueOf(12)));
        cards.add(new Card(Suit.SPADES, CardNumber.valueOf(9)));
        cards.add(new Card(Suit.SPADES, CardNumber.valueOf(8)));

        return cards;
    }

    public List<Card> create(Card... cards) {
        List<Card> cardsOnTable = new LinkedList<>();
        cardsOnTable.addAll(Arrays.asList(cards));
        return cardsOnTable;
    }

}
