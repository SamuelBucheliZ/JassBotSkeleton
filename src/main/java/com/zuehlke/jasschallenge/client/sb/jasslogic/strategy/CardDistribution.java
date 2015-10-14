package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;

import java.util.*;

public class CardDistribution {
    private Map<Integer, EnumSet<Card>> cards;

    public CardDistribution(List<Integer> playerIds) {
        cards = new HashMap<>();
        for (Integer id: playerIds) {
            cards.put(id, EnumSet.noneOf(Card.class));
        }
    }

    public CardDistribution(CardDistribution cardDistribution) {
        cards = new HashMap<>();
        for (Integer id: cardDistribution.cards.keySet()) {
            cards.put(id, EnumSet.copyOf(cardDistribution.cards.get(id)));
        }
    }

    public boolean isEmpty() {
        boolean empty = true;
        for (Integer id : cards.keySet()) {
            empty = empty && cards.get(id).isEmpty();
        }
        return empty;
    }

    public void add(int id, Card card) {
        this.cards.get(id).add(card);
    }

    public EnumSet<Card> get(int nextPlayer) {
        return cards.get(nextPlayer);
    }
}
