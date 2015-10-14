package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;

import java.util.Optional;

public class BroadcastWinnerTeam implements Message {
    private final String message;

    public BroadcastWinnerTeam(String message) {
        this.message = message;
    }

    @Override
    public Optional<Response> dispatch(Game game) {
        game.finishSession(message);
        return Optional.empty();
    }
}
