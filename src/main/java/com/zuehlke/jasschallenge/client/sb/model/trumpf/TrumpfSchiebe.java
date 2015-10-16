package com.zuehlke.jasschallenge.client.sb.model.trumpf;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;

import java.util.Comparator;
import java.util.List;

public class TrumpfSchiebe extends Trumpf {
    public TrumpfSchiebe() {
        super(TrumpfMode.SCHIEBE);
    }

    @Override
    public int getValueOf(Card card) {
        return 0;
    }

    @Override
    public Card getWinningCard(List<Card> cardsOnTable) {
        return null;
    }

    @Override
    public Comparator<Card> getComparator() {
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(TrumpfMode.SCHIEBE);
    }
}
