package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy;

import com.zuehlke.jasschallenge.client.sb.game.GameState;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfMode;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfSchiebe;

import java.util.Random;
import java.util.Set;

/**
 * A trivial example of a strategy, the trumpf is chosen randomly and cards are also chosen randomly.
 */
public class RandomStrategy implements Strategy {
    private Random rand = new Random();

    @Override
    public Trumpf onRequestTrumpf(Set<Card> myCards, boolean isSchiebenAllowed) {
        TrumpfMode mode;
        // WARNING: TrumpfMode.SCHIEBE seems to not be properly supported on the server, currently?
        do {
            mode = chooseRandomly(TrumpfMode.values());
        } while(TrumpfMode.SCHIEBE.equals(mode));
        Suit suit = null;
        if (TrumpfMode.TRUMPF.equals(mode)) {
            suit = chooseRandomly(Suit.values());
        }
        return Trumpf.from(mode, suit);
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
