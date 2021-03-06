package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.model.Stich;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;

import java.util.Optional;

public class BroadcastStich implements Message {
    private final Stich stich;

    public BroadcastStich(Stich stich) {
        this.stich = stich;
    }

    @Override
    public Optional<Response> dispatch(Game game) {
        game.stichMade(stich);
        return Optional.empty();
    }

    public Stich getStich() {
        return stich;
    }
}
