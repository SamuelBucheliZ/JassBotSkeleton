package com.zuehlke.jasschallenge.client.sb.model.cards;

import java.util.HashMap;
import java.util.Map;

public enum CardNumber {

    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(11),
    QUEEN(12),
    KING(13),
    ACE(14);

    private int number;

    private static Map<Integer, CardNumber> values = new HashMap<>();

    static {
        for (CardNumber value : CardNumber.values()) {
            values.put(value.getNumber(), value);
        }
    }

    CardNumber(int number) { this.number = number; }

    public static CardNumber valueOf(int number) {
        return values.get(number);
    }

    public int getNumber() { return number; }
}
