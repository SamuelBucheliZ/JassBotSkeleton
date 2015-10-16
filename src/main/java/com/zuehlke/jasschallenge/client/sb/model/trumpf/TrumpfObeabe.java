package com.zuehlke.jasschallenge.client.sb.model.trumpf;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.CardNumber;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class TrumpfObeabe extends Trumpf {
    private static Map<CardNumber, Integer> cardValues = new HashMap<>();
    static {
        cardValues.put(CardNumber.SIX, 0);
        cardValues.put(CardNumber.SEVEN, 0);
        cardValues.put(CardNumber.EIGHT, 8);
        cardValues.put(CardNumber.NINE, 0);
        cardValues.put(CardNumber.TEN, 10);
        cardValues.put(CardNumber.JACK, 2);
        cardValues.put(CardNumber.QUEEN, 3);
        cardValues.put(CardNumber.KING, 4);
        cardValues.put(CardNumber.ACE, 11);
    }

    public TrumpfObeabe() {
        super(TrumpfMode.OBEABE);
    }

    @Override
    public int getValueOf(Card card) {
        return cardValues.get(card.getCardNumber());
    }

    @Override
    public Card getWinningCard(List<Card> cardsOnTable) {
        Predicate<Card> hasSameSuitAsFirstCard = card -> cardsOnTable.get(0).getSuit().equals(card.getSuit());
        return cardsOnTable.stream()
                .filter(hasSameSuitAsFirstCard)
                .max(getComparator()).get();
    }

    @Override
    public Comparator<Card> getComparator() {
        return (c1, c2) -> Integer.valueOf(c1.getNumber()).compareTo(c2.getNumber());
    }

    @Override
    public String toString() {
        return String.valueOf(getMode());
    }
}
