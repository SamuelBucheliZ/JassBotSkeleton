package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy;

import com.zuehlke.jasschallenge.client.sb.game.GameState;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfMode;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

/**
 * A trivial example of a strategy, the trumpf is chosen randomly and cards are also chosen randomly.
 */
public class RandomStrategy implements Strategy {
    private Random rand = new Random();

    @Override
    public Trumpf onRequestTrumpf(GameState state, boolean isGeschoben) {
        Trumpf trumpf;
        // TODO: ignore SCHIEBE for now
        Collection<Trumpf> allTrumpfsWithoutSchiebe = Trumpf.getAllTrumpfsWithoutSchiebe();
        trumpf = chooseRandomly(allTrumpfsWithoutSchiebe.toArray(new Trumpf[allTrumpfsWithoutSchiebe.size()]));
        return trumpf;
    }

    @Override
    public Card onRequestCard(GameState gameState) {
        Set<Card> var = gameState.getAllowedCardsToPlay();
        Card[] allowedCards = var.toArray(new Card[var.size()]);
        return chooseRandomly(allowedCards);
    }

    private <T> T chooseRandomly(T[] array) {
        int index = rand.nextInt(array.length);
        return array[index];
    }
}
