package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;

import java.util.Optional;

public class BroadcastSessionJoined implements Message {
    private final SessionJoinedData data;

    public BroadcastSessionJoined(SessionJoinedData data) {
        this.data = data;
    }

    @Override
    public Optional<Response> dispatch(Game game) {
        game.joinSession(data.getPlayer());
        return Optional.empty();
    }
}
