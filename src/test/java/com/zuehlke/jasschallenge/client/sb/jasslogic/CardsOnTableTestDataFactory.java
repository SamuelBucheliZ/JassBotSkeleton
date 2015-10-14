package com.zuehlke.jasschallenge.client.sb.jasslogic;

import com.zuehlke.jasschallenge.client.sb.model.cards.CardNumber;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CardsOnTableTestDataFactory {

    public List<Card> createCardsOnTableThreeSpades() {
        List<Card> cards = new LinkedList<>();

        cards.add(Card.SPADE_QUEEN);
        cards.add(Card.SPADE_NINE);
        cards.add(Card.SPADE_EIGHT);

        return cards;
    }

    public List<Card> create(Card... cards) {
        List<Card> cardsOnTable = new LinkedList<>();
        cardsOnTable.addAll(Arrays.asList(cards));
        return cardsOnTable;
    }

}
