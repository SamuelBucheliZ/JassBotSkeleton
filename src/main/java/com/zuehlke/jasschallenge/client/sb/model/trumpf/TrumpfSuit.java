package com.zuehlke.jasschallenge.client.sb.model.trumpf;

import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.model.card.CardNumber;
import com.zuehlke.jasschallenge.client.sb.model.card.Suit;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TrumpfSuit extends Trumpf {
    private static Map<CardNumber, Integer> regularCardValues = new HashMap<>();
    static {
        regularCardValues.put(CardNumber.SIX, 0);
        regularCardValues.put(CardNumber.SEVEN, 0);
        regularCardValues.put(CardNumber.EIGHT, 0);
        regularCardValues.put(CardNumber.NINE, 0);
        regularCardValues.put(CardNumber.TEN, 10);
        regularCardValues.put(CardNumber.JACK, 2);
        regularCardValues.put(CardNumber.QUEEN, 3);
        regularCardValues.put(CardNumber.KING, 4);
        regularCardValues.put(CardNumber.ACE, 11);
    }

    private static Map<CardNumber, Integer> trumpfCardValues = new HashMap<>();
    static {
        trumpfCardValues.put(CardNumber.SIX, 0);
        trumpfCardValues.put(CardNumber.SEVEN, 0);
        trumpfCardValues.put(CardNumber.EIGHT, 0);
        trumpfCardValues.put(CardNumber.NINE, 14);
        trumpfCardValues.put(CardNumber.TEN, 10);
        trumpfCardValues.put(CardNumber.JACK, 2);
        trumpfCardValues.put(CardNumber.QUEEN, 3);
        trumpfCardValues.put(CardNumber.KING, 4);
        trumpfCardValues.put(CardNumber.ACE, 11);
    }

    private Suit suit;

    public TrumpfSuit(Suit suit) {
        this.suit = suit;
    }

    @Override
    public TrumpfMessage toTrumpfMessage() {
        return new TrumpfMessage(TrumpfMode.TRUMPF, suit);
    }

    @Override
    public Suit getSuit() {
        return suit;
    }

    @Override
    public TrumpfMode getMode() {
        return TrumpfMode.TRUMPF;
    }

    @Override
    public boolean isObenabeOrUndeufe() {
        return false;
    }

    @Override
    public int getValueOf(Card card) {
        if (suit.equals(card.getSuit())) {
            return trumpfCardValues.get(card.getCardNumber());
        } else {
            return regularCardValues.get(card.getCardNumber());
        }
    }

    private Integer getTrumpfOrder(Card card) {
        int order = card.getNumber();
        if (card.isTrumpf(this)) {
            switch(card.getCardNumber()) {
                case JACK:
                    order = 23;
                    break;
                case NINE:
                    order = 22;
                    break;
                case ACE:
                case KING:
                case QUEEN:
                    order += 7;
                    break;
                case TEN:
                    order += 8;
                    break;
                case EIGHT:
                case SEVEN:
                case SIX:
                    order += 9;
                    break;
            }
        }
        return order;
    }

    @Override
    public Comparator<Card> getComparator() {
        return (leftCard, rightCard) -> getTrumpfOrder(leftCard).compareTo(getTrumpfOrder(rightCard));
    }

    @Override
    public String toString() {
        return String.valueOf(TrumpfMode.TRUMPF) + " " + suit;
    }
}
