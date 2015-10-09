package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy;

import com.google.common.base.Preconditions;
import com.zuehlke.jasschallenge.client.sb.game.GameState;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfSuit;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;

/**
 * A trivial example of a strategy, the trumpf is chosen randomly (and only as a suit, never obeabe or undenufe),
 * and cards are also chosen randomly.
 */
public class TrivialStrategy implements Strategy {
    private Random rand = new Random();

    @Override
    public Trumpf onRequestTrumpf(Set<Card> myCards) {
        Suit[] suits = { Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS, Suit.SPADES};
        Suit suit = chooseRandomly(suits);
        return new TrumpfSuit(suit);
    }

    @Override
    public Card onRequestCard(GameState gameState) {
        Preconditions.checkNotNull(gameState);
        gameState.checkIsValid();
        Card[] allowedCards = gameState.getAllowedCardsToPlay().toArray(new Card[0]);
        return chooseRandomly(allowedCards);
    }

    private <T> T chooseRandomly(T[] array) {
        int index = rand.nextInt(array.length);
        return array[index];
    }
}
