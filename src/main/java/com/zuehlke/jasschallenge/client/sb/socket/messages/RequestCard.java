package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.socket.responses.ChooseCard;
import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;

import java.util.List;
import java.util.Optional;

public class RequestCard implements Message {
    private final List<Card> cardsOnTable;

    public RequestCard(List<Card> cardsOnTable) {
        this.cardsOnTable = cardsOnTable;
    }

    @Override
    public Optional<Response> dispatch(Game game) {
        Card card = game.requestCard(cardsOnTable);
        Response response = new ChooseCard(card);
        return Optional.of(response);
    }
}
