package com.zuehlke.jasschallenge.client.sb.model.trumpf;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.CardNumber;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TrumpfUndeufe extends Trumpf {
    private static Map<CardNumber, Integer> cardValues = new HashMap<>();
    static {
        cardValues.put(CardNumber.SIX,11);
        cardValues.put(CardNumber.SEVEN, 0);
        cardValues.put(CardNumber.EIGHT, 8);
        cardValues.put(CardNumber.NINE, 0);
        cardValues.put(CardNumber.TEN, 10);
        cardValues.put(CardNumber.JACK, 2);
        cardValues.put(CardNumber.QUEEN, 3);
        cardValues.put(CardNumber.KING, 4);
        cardValues.put(CardNumber.ACE, 0);
    }


    public TrumpfUndeufe() {
        super(TrumpfMode.UNDEUFE);
    }

    @Override
    public boolean isObenabeOrUndeufe() {
        return true;
    }

    @Override
    public int getValueOf(Card card) {
        return cardValues.get(card.getCardNumber());
    }

    @Override
    public Comparator<Card> getComparator() {
        return (c1, c2) -> -Integer.valueOf(c1.getNumber()).compareTo(c2.getNumber());
    }

    @Override
    public String toString() {
        return String.valueOf(getMode());
    }

}
