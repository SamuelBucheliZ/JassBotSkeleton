package com.zuehlke.jasschallenge.client.sb.model;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;

import java.util.Optional;

public class Move {
    private final Optional<Integer> playerId;
    private final Card card;

    public Move(Optional<Integer> playerId, Card card) {
        this.playerId = playerId;
        this.card = card;
    }

    public Optional<Integer> getPlayerId() {
        return playerId;
    }

    public Card getCard() {
        return card;
    }
}
