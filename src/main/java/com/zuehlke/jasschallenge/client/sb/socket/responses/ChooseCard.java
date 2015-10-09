package com.zuehlke.jasschallenge.client.sb.socket.responses;

import com.zuehlke.jasschallenge.client.sb.model.card.Card;

public class ChooseCard implements Response {
    private final ResponseType type = ResponseType.CHOOSE_CARD;
    private final Card data;

    public ChooseCard(Card card) {
        this.data = card;
    }
}
