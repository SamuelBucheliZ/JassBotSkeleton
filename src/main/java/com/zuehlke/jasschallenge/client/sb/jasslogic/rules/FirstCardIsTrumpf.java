package com.zuehlke.jasschallenge.client.sb.jasslogic.rules;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FirstCardIsTrumpf extends AllowedCards {

    FirstCardIsTrumpf(Collection<Card> playerCards, Trumpf trumpf, List<Card> cardsOnTable) {
        super(playerCards, trumpf, cardsOnTable);
    }

    @Override
    public Set<Card> get() {
        Set<Card> trumpfs = toSet(getTrumpfs());

        if (!trumpfs.isEmpty()) {
            return trumpfs;
        }

        return getPlayerCards();
    }

}
