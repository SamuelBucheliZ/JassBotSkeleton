package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;

import java.util.Optional;

public class BroadcastTrumpf implements Message {
    private final Trumpf trumpf;

    public BroadcastTrumpf(Trumpf trumpf) {
        this.trumpf = trumpf;
    }

    @Override
    public Optional<Response> dispatch(Game game) {
        game.startGame(trumpf);
        return Optional.empty();
    }

    public Trumpf getTrumpf() {
        return trumpf;
    }
}
