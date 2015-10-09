package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;

import java.util.Optional;

public class BroadcastTeams implements Message {
    private final String message;

    public BroadcastTeams(String message) {
        this.message = message;
    }

    @Override
    public Optional<Response> dispatch(Game game) {
        game.startSession();
        game.log("Message from server: " + message);
        return Optional.empty();
    }
}
