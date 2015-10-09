package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy;

import com.google.common.base.Preconditions;
import com.zuehlke.jasschallenge.client.sb.game.GameState;
import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.model.card.Suit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfSuit;

import java.util.Set;

public class TrivialStrategy implements Strategy {

    @Override
    public Trumpf onRequestTrumpf(Set<Card> myCards) {
        return new TrumpfSuit(Suit.HEARTS);
    }

    @Override
    public Card onRequestCard(GameState gameState) {
        Preconditions.checkNotNull(gameState);
        gameState.checkIsValid();

        return gameState.getAllowedCardsToPlay().iterator().next();
    }
}
