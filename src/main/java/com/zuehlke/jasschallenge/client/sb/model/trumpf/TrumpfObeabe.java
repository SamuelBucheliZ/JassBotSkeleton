package com.zuehlke.jasschallenge.client.sb.model.trumpf;

import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.model.card.CardNumber;
import com.zuehlke.jasschallenge.client.sb.model.card.Suit;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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

    private TrumpfMode mode = TrumpfMode.OBEABE;

    @Override
    public TrumpfMessage toTrumpfMessage() {
        return new TrumpfMessage(mode);
    }

    @Override
    public Suit getSuit() {
        return null;
    }

    @Override
    public TrumpfMode getMode() {
        return mode;
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
        return Card::compareTo;
    }

    @Override
    public String toString() {
        return String.valueOf(mode);
    }
}
