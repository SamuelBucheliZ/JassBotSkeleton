package com.zuehlke.jasschallenge.client.sb.jasslogic.rules;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.zuehlke.jasschallenge.client.sb.model.cards.Card.CLUB_NINE;
import static com.zuehlke.jasschallenge.client.sb.model.cards.Card.HEART_SIX;

public class CardsOnTableTestDataFactory {

    public List<Card> createCardsOnTableThreeSpades() {
        List<Card> cards = new LinkedList<>();

        cards.add(Card.SPADE_QUEEN);
        cards.add(Card.SPADE_NINE);
        cards.add(Card.SPADE_EIGHT);

        return cards;
    }

    public List<Card> createCardOnTableHeartSixAndClubsNine() {
        List<Card> cards = new LinkedList<>();
        cards.add(HEART_SIX);
        cards.add(CLUB_NINE);
        return cards;
    }

    public List<Card> createCardsOnTable(Card... cards) {
        return Arrays.asList(cards);
    }

    public List<Card> createCardOnTableHeartSix() {
        List<Card> cards = new LinkedList<>();
        cards.add(HEART_SIX);
        return cards;
    }

    public List<Card> createCardsOnTableEmpty() {
        return new LinkedList<>();
    }


}
