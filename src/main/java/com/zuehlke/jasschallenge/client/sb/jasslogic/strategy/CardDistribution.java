package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;

import java.util.*;

public class CardDistribution {
    private Map<Integer, LinkedList<Card>> cards;

    public CardDistribution(List<Integer> playerIds) {
        cards = new HashMap<>();
        for (Integer id: playerIds) {
            cards.put(id, new LinkedList<>());
        }
    }

    public CardDistribution(CardDistribution cardDistribution) {
        cards = new HashMap<>();
        for (Integer id: cardDistribution.cards.keySet()) {
            cards.put(id, new LinkedList<>(cardDistribution.cards.get(id)));
        }
    }

    public boolean isEmpty() {
        return cards.values().stream().allMatch(List<Card>::isEmpty);
    }

    public void add(int id, Card card) {
        this.cards.get(id).add(card);
    }

    public Card removeFirst(int id) {
        return this.cards.get(id).removeFirst();
    }

    public void remove(int id, Card card) {
        this.cards.get(id).remove(card);
    }

    public void shuffle(int id) {
        Collections.shuffle(cards.get(id));
    }

    public Set<Card> get(int id) {
        return new HashSet<>(cards.get(id));
    }
}
