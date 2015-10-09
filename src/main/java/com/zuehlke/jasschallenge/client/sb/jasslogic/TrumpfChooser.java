package com.zuehlke.jasschallenge.client.sb.jasslogic;

import com.google.common.base.Preconditions;
import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.model.card.CardNumber;
import com.zuehlke.jasschallenge.client.sb.model.card.Suit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfSuit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TrumpfChooser {
    private static final int JACK_BONUS = 10;
    private static final int NINE_BONUS = 7;
    private static final int ACE_BONUS = 5;

    public Trumpf chooseTrumpf(Set<Card> myCards) {
        Preconditions.checkNotNull(myCards);
        Preconditions.checkArgument(myCards.size() == 9);
        for (Card card : myCards) {
            Preconditions.checkNotNull(card);
            Preconditions.checkNotNull(card.getSuit());
            Preconditions.checkArgument(card.getNumber() >= 6);
            Preconditions.checkArgument(card.getNumber() <= 14);
        }

        Map<Suit, List<Card>> cardsBySuit = myCards.stream().collect(Collectors.groupingBy(Card::getSuit));
        Map<Suit, Integer> evaluation = new HashMap<>();

        for (Suit s : cardsBySuit.keySet()) {
            List<Card> cards = cardsBySuit.get(s);
            int value = cards.size();

            Card jack = new Card(s, CardNumber.valueOf(11));
            if (cards.contains(jack)) {
                value += JACK_BONUS;
            }
            Card nine = new Card(s, CardNumber.valueOf(9));
            if (cards.contains(nine)) {
                value += NINE_BONUS;
            }
            Card ace = new Card(s, CardNumber.valueOf(14));
            if (cards.contains(ace)) {
                value += ACE_BONUS;
            }
            evaluation.put(s, value);
        }

        Suit chosen = evaluation.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();

        return new TrumpfSuit(chosen);
    }

}
