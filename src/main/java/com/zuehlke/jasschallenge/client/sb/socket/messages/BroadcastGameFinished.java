package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;

import java.util.Optional;

public class BroadcastGameFinished implements Message {
    private final String message;

    public BroadcastGameFinished(String message) {
        this.message = message;
    }

    @Override
    public Optional<Response> dispatch(Game game) {
        game.finishGame(message);
        return Optional.empty();
    }
}
