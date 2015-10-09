package com.zuehlke.jasschallenge.client.sb.model.cards;

import com.google.common.base.Preconditions;
import com.google.gson.annotations.SerializedName;
import com.sun.istack.internal.NotNull;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfMode;

import java.util.Comparator;

public class Card implements Comparable {

    private CardNumber cardNumber;
    @SerializedName("color")
    private Suit suit;

    public Card(Suit suit, CardNumber cardNumber) {
        Preconditions.checkNotNull(suit);
        Preconditions.checkNotNull(cardNumber);
        this.suit = suit;
        this.cardNumber = cardNumber;
    }

    @Override
    public String toString() {
        return suit.getUnicodeCharacter() + " " + cardNumber.getNumber();
    }

    public CardNumber getCardNumber() { return cardNumber; }

    public int getNumber() { return cardNumber.getNumber(); }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if (!(o instanceof Card)) {
            throw new IllegalArgumentException("Expected card but was " + o.getClass().getName());
        }
        Card other = (Card) o;

        return Integer.valueOf(this.getNumber()).compareTo(other.getNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(suit, card.suit)
                .append(cardNumber, card.cardNumber)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
                .append(suit)
                .append(cardNumber)
                .toHashCode();
    }

    public boolean beatsCard(Card card, Trumpf trumpf) {
        return trumpf.beatsCard(this, card);
    }

    /**
     * Allows to sort cards by their strength given a certain trumpf.
     */
    public static Comparator<Card> getComperatorForTrumpf(Trumpf trumpf) {
        return trumpf.getComparator();
    }

    public boolean isTrumpf(Trumpf trumpf) {
        return trumpf.getMode().equals(TrumpfMode.TRUMPF) && suit.equals(trumpf.getSuit());
    }

}
