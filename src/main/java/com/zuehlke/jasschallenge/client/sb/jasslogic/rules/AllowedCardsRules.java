package com.zuehlke.jasschallenge.client.sb.jasslogic.rules;

import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;

import java.util.List;
import java.util.Set;

public class AllowedCardsRules {
    public static AllowedCards getFor(Set<Card> playerCards, Trumpf trumpf, List<Card> cardsOnTable) {
        if (thereAreNoCardsOnTheTable(cardsOnTable)) {
            return new NoCardsPlayedYet(playerCards, trumpf, cardsOnTable);
        }

        if (theFirstCardOnTheTableIsTrumpf(trumpf, cardsOnTable)) {
            return new FirstCardIsTrumpf(playerCards, trumpf, cardsOnTable);
        }

        if (thereIsATrumpfOnTheTable(trumpf, cardsOnTable)) {
            return new FirstCardIsNotTrumpfButTrumpfOnTable(playerCards, trumpf, cardsOnTable);
        }

        return new FirstCardIsNotTrumpfAndNoTrumpfOnTable(playerCards, trumpf, cardsOnTable);
    }

    private static boolean thereIsATrumpfOnTheTable(Trumpf trumpf, List<Card> cardsOnTable) {
        return cardsOnTable.stream().anyMatch(card -> card.getSuit().equals(trumpf.getSuit()));
    }

    private static boolean theFirstCardOnTheTableIsTrumpf(Trumpf trumpf, List<Card> cardsOnTable) {
        return cardsOnTable.get(0).getSuit().equals(trumpf.getSuit());
    }

    private static boolean thereAreNoCardsOnTheTable(List<Card> cardsOnTable) {
        return cardsOnTable.size() == 0;
    }
}
