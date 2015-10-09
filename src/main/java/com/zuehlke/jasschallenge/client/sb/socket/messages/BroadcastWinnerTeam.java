package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;

import java.util.Optional;

public class BroadcastWinnerTeam implements Message {
    @Override
    public Optional<Response> dispatch(Game game) {
        game.finishSession();
        return Optional.empty();
    }
}
