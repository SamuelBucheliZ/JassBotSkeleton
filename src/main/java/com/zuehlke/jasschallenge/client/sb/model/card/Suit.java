package com.zuehlke.jasschallenge.client.sb.model.card;

/**
 * See: https://en.wikipedia.org/wiki/Suit_(cards)
 */
public enum Suit {

    CLUBS("\u2663"),
    SPADES("\u2660"),
    HEARTS("\u2665"),
    DIAMONDS("\u2666");

    private String unicodeCharacter;

    Suit(String unicodeCharacter) {
        this.unicodeCharacter = unicodeCharacter;
    }

    public String getUnicodeCharacter() {
        return unicodeCharacter;
    }

}
