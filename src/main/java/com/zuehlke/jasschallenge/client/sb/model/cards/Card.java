package com.zuehlke.jasschallenge.client.sb.model.cards;

import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfMode;

import java.util.HashMap;
import java.util.Map;

import static com.zuehlke.jasschallenge.client.sb.model.cards.CardNumber.*;
import static com.zuehlke.jasschallenge.client.sb.model.cards.Suit.*;


public enum Card {
    HEART_SIX(HEARTS, SIX),
    HEART_SEVEN(HEARTS, SEVEN),
    HEART_EIGHT(HEARTS, EIGHT),
    HEART_NINE(HEARTS, NINE),
    HEART_TEN(HEARTS, TEN),
    HEART_JACK(HEARTS, JACK),
    HEART_QUEEN(HEARTS, QUEEN),
    HEART_KING(HEARTS, KING),
    HEART_ACE(HEARTS, ACE),

    DIAMOND_SIX(DIAMONDS, SIX),
    DIAMOND_SEVEN(DIAMONDS, SEVEN),
    DIAMOND_EIGHT(DIAMONDS, EIGHT),
    DIAMOND_NINE(DIAMONDS, NINE),
    DIAMOND_TEN(DIAMONDS, TEN),
    DIAMOND_JACK(DIAMONDS, JACK),
    DIAMOND_QUEEN(DIAMONDS, QUEEN),
    DIAMOND_KING(DIAMONDS, KING),
    DIAMOND_ACE(DIAMONDS, ACE),

    CLUB_SIX(CLUBS, SIX),
    CLUB_SEVEN(CLUBS, SEVEN),
    CLUB_EIGHT(CLUBS, EIGHT),
    CLUB_NINE(CLUBS, NINE),
    CLUB_TEN(CLUBS, TEN),
    CLUB_JACK(CLUBS, JACK),
    CLUB_QUEEN(CLUBS, QUEEN),
    CLUB_KING(CLUBS, KING),
    CLUB_ACE(CLUBS, ACE),

    SPADE_SIX(SPADES, SIX),
    SPADE_SEVEN(SPADES, SEVEN),
    SPADE_EIGHT(SPADES, EIGHT),
    SPADE_NINE(SPADES, NINE),
    SPADE_TEN(SPADES, TEN),
    SPADE_JACK(SPADES, JACK),
    SPADE_QUEEN(SPADES, QUEEN),
    SPADE_KING(SPADES, KING),
    SPADE_ACE(SPADES, ACE);

    private final Suit suit;
    private final CardNumber cardNumber;

    private static Map<Suit, Map<CardNumber, Card>> values = new HashMap<>();

    static {
        for (Suit suit: Suit.values()) {
            values.put(suit, new HashMap<>());
        }
        for (Card card : Card.values()) {
            values.get(card.getSuit()).put(card.getCardNumber(), card);
        }
    }

    Card(Suit suit, CardNumber cardNumber) {
        this.suit = suit;
        this.cardNumber = cardNumber;
    }

    public static Card valueOf(Suit suit, CardNumber cardNumber) {
        return values.get(suit).get(cardNumber);
    }

    /*@Override
    public String toString() {
        return suit.getUnicodeCharacter() + " " + cardNumber.getNumber();
    }*/

    public CardNumber getCardNumber() { return cardNumber; }

    public int getNumber() { return cardNumber.getNumber(); }

    public Suit getSuit() {
        return suit;
    }

    public boolean beatsCard(Card card, Trumpf trumpf) {
        return trumpf.beatsCard(this, card);
    }

    public boolean isTrumpf(Trumpf trumpf) {
        return trumpf.getMode().equals(TrumpfMode.TRUMPF) && suit.equals(trumpf.getSuit());
    }

}
