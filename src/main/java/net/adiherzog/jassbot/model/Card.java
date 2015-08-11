package net.adiherzog.jassbot.model;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class Card implements Comparable {

    private int number;
    @SerializedName("color")
    private Suit suit;

    public Card(Suit suit, int number) {
        this.suit = suit;
        this.number = number;
    }

    @Override
    public String toString() {
        return suit.getUnicodeCharacter() + " " + number;
    }

    public int getNumber() {
        return number;
    }

    public int getNumberTrumpfOrder() {
        if (number == 11) {
            return 16;
        } else if (number == 9) {
            return 15;
        } else {
            return number;
        }
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Card)) {
            throw new IllegalArgumentException("Expected card but was " + o.getClass().getName());
        }
        Card other = (Card) o;

        return Integer.valueOf(number).compareTo(other.getNumber());
    }

    public int compareToTrumpfOrder(Object o) {
        if (!(o instanceof Card)) {
            throw new IllegalArgumentException("Expected card but was " + o.getClass().getName());
        }
        Card other = (Card) o;

        return Integer.valueOf(getNumberTrumpfOrder()).compareTo(other.getNumberTrumpfOrder());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(number, card.number)
                .append(suit, card.suit)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
                .append(number)
                .append(suit)
                .toHashCode();
    }

    public boolean isSuitEqualTo(Suit suit) {
        return suit.equals(suit);
    }

    public boolean beatsCard(Card card, Trumpf trumpf) {
        if (TrumpfMode.TRUMPF.equals(trumpf.getMode())) {
            return beatsCardTrumpf(card, trumpf);
        } else if (TrumpfMode.OBEABE.equals(trumpf.getMode())) {
            return beatsCardObeabe(card);
        } else if (TrumpfMode.UNDEUFE.equals(trumpf.getMode())) {
            return beatsCardUndeufe(card);
        } else {
            throw new IllegalArgumentException("Unexpected " + trumpf.getMode());
        }
    }

    private boolean beatsCardUndeufe(Card card) {
        return this.compareTo(card) < 0;
    }

    private boolean beatsCardObeabe(Card card) {
        return this.compareTo(card) > 0;
    }

    private boolean beatsCardTrumpf(Card card, Trumpf trumpf) {
        if (suit.equals(trumpf.getSuit())) {
            // This card is trumpf
            if (card.getSuit().equals(trumpf.getSuit())) {
                // Comparing two trumpfs
                return this.compareToTrumpfOrder(card) > 0;
            } else {
                // Trumpf always beats non-trumpf
                return true;
            }
        } else {
            // This card is not trumpf
            if (card.getSuit().equals(trumpf.getSuit())) {
                // Other card is trumpf
                return false;
            } else {
                // Both cards are non-trumpf
                return this.compareTo(card) > 0;
            }
        }
    }

    /**
     * Allows to sort cards by their strength given a certain trumpf.
     */
    public static Comparator<Card> getComperatorForTrumpf(Trumpf trumpf) {
        return new Comparator<Card>() {
            @Override
            public int compare(Card leftCard, Card rightCard) {
                return leftCard.beatsCard(rightCard, trumpf) ? 1 : -1;
            }
        };
    }

    public boolean isTrumpf(Trumpf trumpf) {
        return trumpf.getMode().equals(TrumpfMode.TRUMPF) && suit.equals(trumpf.getSuit());
    }

}
