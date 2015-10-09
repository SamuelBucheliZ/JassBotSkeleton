package com.zuehlke.jasschallenge.client.sb.jasslogic.rules;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;

import java.util.*;
import java.util.stream.Stream;

public class FirstCardIsNotTrumpfButTrumpfOnTable extends AllowedCards {

    FirstCardIsNotTrumpfButTrumpfOnTable(Set<Card> playerCards, Trumpf trumpf, List<Card> cardsOnTable) {
        super(playerCards, trumpf, cardsOnTable);
    }

    @Override
    public Set<Card> get() {
        Stream<Card> trumpfs = getTrumpfs();
        Card strongestTrumpfOnTable = getStrongestTrumpfOnTable().get();
        Stream<Card> trumpfsThatDoNotUndertrumpf = trumpfs.filter(isStrongerThan(strongestTrumpfOnTable));

        Card firstCardOnTable = getFirstCardOnTable().get();
        Stream<Card> cardsMatchingFirstCard = getCardsMatching(firstCardOnTable.getSuit());

        Set<Card> cardsMatchingFirstCardAndAllowedTrumpfs = toSet(Stream.concat(cardsMatchingFirstCard, trumpfsThatDoNotUndertrumpf));

        if (!cardsMatchingFirstCardAndAllowedTrumpfs.isEmpty()) {
            return cardsMatchingFirstCardAndAllowedTrumpfs;
        }

        return toSet(getNonTrumpfs());
    }
}
