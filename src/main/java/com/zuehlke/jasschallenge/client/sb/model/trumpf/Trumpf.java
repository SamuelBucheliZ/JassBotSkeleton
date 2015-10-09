package com.zuehlke.jasschallenge.client.sb.model.trumpf;

import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.model.card.Suit;

import java.util.Comparator;

public abstract class Trumpf {

    public abstract TrumpfMessage toTrumpfMessage();

    public abstract Suit getSuit();

    public abstract TrumpfMode getMode();

    @Override
    public abstract String toString();

    public abstract boolean isObenabeOrUndeufe();

    public abstract int getValueOf(Card card);

    public boolean beatsCard(Card leftCard, Card rightCard) {
        Comparator<Card> comparator = getComparator();
        return comparator.compare(leftCard, rightCard) > 0;
    }

    public abstract Comparator<Card> getComparator();
}
