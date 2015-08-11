package net.adiherzog.jassbot.jasslogic;

import net.adiherzog.jassbot.model.Card;
import net.adiherzog.jassbot.model.Suit;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CardsOnTableTestDataFactory {

    public List<Card> createCardsOnTableThreeSpades() {
        List<Card> cards = new LinkedList<>();

        cards.add(new Card(Suit.SPADES, 12));
        cards.add(new Card(Suit.SPADES, 9));
        cards.add(new Card(Suit.SPADES, 8));

        return cards;
    }

    public List<Card> create(Card... cards) {
        List<Card> cardsOnTable = new LinkedList<>();
        cardsOnTable.addAll(Arrays.asList(cards));
        return cardsOnTable;
    }

}
