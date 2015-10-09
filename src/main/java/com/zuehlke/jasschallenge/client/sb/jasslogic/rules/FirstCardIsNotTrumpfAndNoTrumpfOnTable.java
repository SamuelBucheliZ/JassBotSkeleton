package com.zuehlke.jasschallenge.client.sb.jasslogic.rules;

import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class FirstCardIsNotTrumpfAndNoTrumpfOnTable extends AllowedCards {

    FirstCardIsNotTrumpfAndNoTrumpfOnTable(Set<Card> playerCards, Trumpf trumpf, List<Card> cardsOnTable) {
        super(playerCards, trumpf, cardsOnTable);
    }

    @Override
    public Set<Card> get() {
        Card firstCardOnTable = getFirstCardOnTable().get();

        Stream<Card> trumpfs = getTrumpfs();

        Stream<Card> cardsMatchingFirstCard = getCardsMatching(firstCardOnTable.getSuit());

        Set<Card> cardsMatchingFirstCardAndAllowedTrumpfs = toSet(Stream.concat(cardsMatchingFirstCard, trumpfs));

        if (!cardsMatchingFirstCardAndAllowedTrumpfs.isEmpty()) {
            return cardsMatchingFirstCardAndAllowedTrumpfs;
        }

        return getPlayerCards();
    }
}
