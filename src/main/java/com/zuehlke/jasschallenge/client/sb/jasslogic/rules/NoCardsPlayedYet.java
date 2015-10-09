package com.zuehlke.jasschallenge.client.sb.jasslogic.rules;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;

import java.util.List;
import java.util.Set;

public class NoCardsPlayedYet extends AllowedCards {

    NoCardsPlayedYet(Set<Card> playerCards, Trumpf trumpf, List<Card> cardsOnTable) {
        super(playerCards, trumpf, cardsOnTable);
    }

    @Override
    public Set<Card> get() {
        return getPlayerCards();
    }
}
