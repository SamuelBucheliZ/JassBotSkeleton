package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;

import java.util.Optional;
import java.util.Set;

public class DealCards implements Message {
    private final Set<Card> cards;

    public DealCards(Set<Card> cards) {
        this.cards = cards;
    }

    @Override
    public Optional<Response> dispatch(Game game) {
        game.cardsDealt(cards);
        return Optional.empty();
    }

    public Set<Card> getCards() {
        return cards;
    }
}
