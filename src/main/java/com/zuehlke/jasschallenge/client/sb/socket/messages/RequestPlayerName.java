package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.socket.responses.ChoosePlayerName;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;

import java.util.Optional;

public class RequestPlayerName implements Message {
    @Override
    public Optional<Response> dispatch(Game game) {
        Response response = new ChoosePlayerName(game.getRemotePlayerName());
        return Optional.of(response);
    }
}
