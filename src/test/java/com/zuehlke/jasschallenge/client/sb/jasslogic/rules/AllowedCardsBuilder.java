package com.zuehlke.jasschallenge.client.sb.jasslogic.rules;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;

import java.util.*;

// This class was introduced as some convenience glue as the original tests operated directly on the game state, but the logic has now been moved to a separate class.
public class AllowedCardsBuilder {

    private Set<Card> myCards;

    private List<Card> cardsOnTable;
    private Optional<Trumpf> trumpf;


    AllowedCardsBuilder() {
        this.myCards = new HashSet<>();
        this.cardsOnTable = new LinkedList<>();
        this.trumpf = Optional.empty();
    }

    public AllowedCardsBuilder setMyCards(Collection<Card> myCards) {
        this.myCards = new HashSet<>(myCards);
        return this;
    }

    public AllowedCardsBuilder setTrumpf(Trumpf trumpf) {
        this.trumpf = Optional.of(trumpf);
        return this;
    }

    public AllowedCardsBuilder setCardsOnTable(List<Card> cardsOnTable) {
        this.cardsOnTable = new ArrayList<>(cardsOnTable);
        return this;
    }


    public Set<Card> getAllowedCards() {
        return AllowedCardsRules.getFor(myCards, trumpf.get(), cardsOnTable).get();
    }

    public Set<Card> getMyCards() {
        return myCards;
    }

    public Trumpf getTrumpf() {
        return trumpf.get();
    }
}
