package com.zuehlke.jasschallenge.client.sb.model.trumpf;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.CardNumber;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

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
        trumpfCardValues.put(CardNumber.JACK, 20);
        trumpfCardValues.put(CardNumber.QUEEN, 3);
        trumpfCardValues.put(CardNumber.KING, 4);
        trumpfCardValues.put(CardNumber.ACE, 11);
    }

    public TrumpfSuit(Suit suit) {
        super(TrumpfMode.TRUMPF, suit);
    }

    @Override
    public int getValueOf(Card card) {
        if (getSuit().equals(card.getSuit())) {
            return trumpfCardValues.get(card.getCardNumber());
        } else {
            return regularCardValues.get(card.getCardNumber());
        }
    }

    @Override
    public Card getWinningCard(List<Card> cardsOnTable) {
        Predicate<Card> hasSameSuitAsFirstCardOrIsTrumpf = card -> cardsOnTable.get(0).getSuit().equals(card.getSuit()) || this.getSuit().equals(card.getSuit());
        return cardsOnTable.stream()
                .filter(hasSameSuitAsFirstCardOrIsTrumpf)
                .max(getComparator()).get();
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
        return String.valueOf(TrumpfMode.TRUMPF) + " " + getSuit();
    }
}
