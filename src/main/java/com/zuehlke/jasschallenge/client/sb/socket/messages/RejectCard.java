package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;

import java.util.Optional;

public class RejectCard implements Message {
    private final Card rejectedCard;

    public RejectCard(Card rejectedCard) {
        this.rejectedCard = rejectedCard;
    }

    @Override
    public Optional<Response> dispatch(Game game) {
        game.log("Card rejected: " + rejectedCard);
        return Optional.empty();
    }
}
