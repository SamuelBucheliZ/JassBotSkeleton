package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;

import java.util.Optional;

public class BadMessage implements Message {
    private final String message;

    public BadMessage(String message) {
        this.message = message;
    }

    @Override
    public Optional<Response> dispatch(Game game) {
        System.out.println(message);
        return Optional.empty();
    }
}
