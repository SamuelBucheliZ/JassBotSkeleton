package com.zuehlke.jasschallenge.client.sb.model.trumpf;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;

import java.util.Comparator;

public class TrumpfSchiebe extends Trumpf {
    public TrumpfSchiebe() {
        super(TrumpfMode.SCHIEBE);
    }

    @Override
    public boolean isObenabeOrUndeufe() {
        return false;
    }

    @Override
    public int getValueOf(Card card) {
        return 0;
    }

    @Override
    public Comparator<Card> getComparator() {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }
}
