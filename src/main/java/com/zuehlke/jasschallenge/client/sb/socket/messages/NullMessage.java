package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;

import java.util.Optional;

public class NullMessage implements Message {
    @Override
    public Optional<Response> dispatch(Game game) {
        return Optional.empty();
    }
}
