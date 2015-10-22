package com.zuehlke.jasschallenge.client.sb.jasslogic.rules;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FirstCardIsNotTrumpfButTrumpfOnTable extends AllowedCards {

    FirstCardIsNotTrumpfButTrumpfOnTable(Collection<Card> playerCards, Trumpf trumpf, List<Card> cardsOnTable) {
        super(playerCards, trumpf, cardsOnTable);
    }

    @Override
    public Set<Card> get() {
        Stream<Card> trumpfs = getTrumpfs();

        Card strongestTrumpfOnTable = getStrongestTrumpfOnTable().get();

        Map<Boolean, Set<Card>> trumpfPartition = trumpfs.collect(Collectors.partitioningBy(isStrongerThan(strongestTrumpfOnTable), Collectors.toSet()));
        boolean doesNotUnderTrumpf = true;
        Set<Card> trumpfsThatDoNotUndertrumpf = trumpfPartition.get(doesNotUnderTrumpf);
        Set<Card> trumpfsThatDoUndertrumpf = trumpfPartition.get(!doesNotUnderTrumpf);

        Card firstCardOnTable = getFirstCardOnTable().get();
        Set<Card> cardsMatchingFirstCard = toSet(getCardsMatching(firstCardOnTable.getSuit()));

        Set<Card> cardsMatchingFirstCardAndAllowedTrumpfs = toSet(Stream.concat(cardsMatchingFirstCard.stream(), trumpfsThatDoNotUndertrumpf.stream()));

        if (!cardsMatchingFirstCardAndAllowedTrumpfs.isEmpty()) {
            return cardsMatchingFirstCardAndAllowedTrumpfs;
        }

        Set<Card> myCardsWithoutUndertrumpfs = toSet(getPlayerCards().stream().filter(card -> !trumpfsThatDoUndertrumpf.contains(card)));
        if (!myCardsWithoutUndertrumpfs.isEmpty()) {
            return myCardsWithoutUndertrumpfs;
        }

        return getPlayerCards();
    }
}
