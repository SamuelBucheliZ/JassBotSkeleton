package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;

import java.util.List;
import java.util.Optional;


public class PlayedCards implements Message {
    private final List<Card> playedCards;

    public PlayedCards(List<Card> playedCards) {
        this.playedCards = playedCards;
    }

    @Override
    public Optional<Response> dispatch(Game game) {
        game.cardsPlayed(playedCards);
        return Optional.empty();
    }
}
