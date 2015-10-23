package com.zuehlke.jasschallenge.client.sb.model.trumpf;

import com.google.common.base.Preconditions;
import com.google.gson.annotations.SerializedName;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;

import java.util.*;

public abstract class Trumpf {
    public static Trumpf from(TrumpfMode mode, Suit suit) {
        Trumpf trumpf;
        switch(mode) {
            case TRUMPF:
                Preconditions.checkNotNull(suit);
                trumpf = new TrumpfSuit(suit);
                break;
            case OBEABE:
                Preconditions.checkArgument(Objects.isNull(suit));
                trumpf = new TrumpfObeabe();
                break;
            case UNDEUFE:
                Preconditions.checkArgument(Objects.isNull(suit));
                trumpf = new TrumpfUndeufe();
                break;
            case SCHIEBE:
                Preconditions.checkArgument(Objects.isNull(suit));
                trumpf = new TrumpfSchiebe();
                break;
            default:
                throw new IllegalArgumentException("Unknown mode: " + mode);
        }
        return trumpf;
    }

    public static Trumpf from(TrumpfMode mode) {
        Preconditions.checkArgument(!TrumpfMode.TRUMPF.equals(mode));
        return from(mode, null);
    }

    public static Trumpf from(Suit suit) {
        return from(TrumpfMode.TRUMPF, suit);
    }

    public static Collection<Trumpf> getAllTrumpfsWithoutSchiebe() {
        Set<Trumpf> trumpfs =new HashSet<>();
        trumpfs.add(new TrumpfUndeufe());
        trumpfs.add(new TrumpfObeabe());
        for (Suit suit: Suit.values()) {
            trumpfs.add(new TrumpfSuit(suit));
        }
        return trumpfs;
    }

    private final TrumpfMode mode;

    @SerializedName("trumpfColor")
    private final Suit suit;

    Trumpf(TrumpfMode mode, Suit suit) {
        Preconditions.checkArgument(TrumpfMode.TRUMPF.equals(mode));
        this.mode = mode;
        this.suit = suit;
    }

    Trumpf(TrumpfMode mode) {
        Preconditions.checkArgument(!TrumpfMode.TRUMPF.equals(mode));
        this.mode = mode;
        this.suit = null;
    }

    public abstract int getValueOf(Card card);

    public abstract Card getWinningCard(List<Card> cardsOnTable);

    public abstract Comparator<Card> getComparator();

    public boolean beatsCard(Card leftCard, Card rightCard) {
        Comparator<Card> comparator = getComparator();
        return comparator.compare(leftCard, rightCard) > 0;
    }

    public int getValueOf(Collection<Card> cards) {
        int points = 0;
        for (Card card: cards) {
            points += getValueOf(card);
        }
        return points;
    }

    public TrumpfMode getMode() {
        return mode;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public abstract String toString();
}
