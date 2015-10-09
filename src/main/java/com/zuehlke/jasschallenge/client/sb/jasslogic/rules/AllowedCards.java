package com.zuehlke.jasschallenge.client.sb.jasslogic.rules;

import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.model.card.Suit;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AllowedCards {

    protected static Set<Card> toSet(Stream<Card> cardStream) {
        return cardStream.collect(Collectors.toSet());
    }

    private final Set<Card> playerCards;
    private final Trumpf trumpf;
    private final List<Card> cardsOnTable;

    private final Map<Boolean, Set<Card>> trumpfPartition;

    AllowedCards(Set<Card> playerCards, Trumpf trumpf, List<Card> cardsOnTable) {
        this.playerCards = playerCards;
        this.trumpf = trumpf;
        this.cardsOnTable = cardsOnTable;

        this.trumpfPartition = playerCards.stream().collect(Collectors.partitioningBy(isTrumpf(), Collectors.toSet()));
    }

    public abstract Set<Card> get();

    Predicate<Card> isStrongerThan(Card card) {
        return c -> c.beatsCard(card, trumpf);
    }

    Predicate<Card> matchesSuit(Suit suit) {
        return c -> c.getSuit().equals(suit);
    }

    Predicate<Card> isTrumpf() {
        return c -> c.isTrumpf(trumpf);
    }

    Comparator<Card> getTrumpfComparator() {
        return Card.getComperatorForTrumpf(trumpf);
    }

    Optional<Card> getFirstCardOnTable() {
        return cardsOnTable.stream().findFirst();
    }

    Optional<Card> getStrongestTrumpfOnTable() {
        return cardsOnTable.stream().filter(isTrumpf()).max(getTrumpfComparator());
    }

    Stream<Card> getCardsMatching(Suit suit) {
        return playerCards.stream().filter(matchesSuit(suit));
    }

    Stream<Card> getTrumpfs() {
        boolean isTrumpf = true;
        return trumpfPartition.get(isTrumpf).stream();
    }

    Stream<Card> getNonTrumpfs() {
        boolean isTrumpf = false;
        return trumpfPartition.get(isTrumpf).stream();
    }

    Set<Card> getPlayerCards() {
        return playerCards;
    }
}
