package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;
import com.zuehlke.jasschallenge.client.sb.socket.responses.ChooseTrumpf;

import java.util.Optional;

public class RequestTrumpf implements Message {
    private final boolean isSchiebenAllowed;

    public RequestTrumpf(boolean isSchiebenAllowed) {
        this.isSchiebenAllowed = isSchiebenAllowed;
    }

    @Override
    public Optional<Response> dispatch(Game game) {
        Trumpf trumpf = game.requestTrumpf(isSchiebenAllowed);
        Response response = new ChooseTrumpf(trumpf);
        return Optional.of(response);
    }
}
