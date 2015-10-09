package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.socket.responses.ChooseSession;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;

import java.util.Optional;

public class RequestSessionChoice implements Message {
    @Override
    public Optional<Response> dispatch(Game game) {
        SessionChoice sessionChoice = new SessionChoice(SessionChoiceType.JOIN_EXISTING, game.getSessionName());
        Response response = new ChooseSession(sessionChoice);
        return Optional.of(response);
    }
}
