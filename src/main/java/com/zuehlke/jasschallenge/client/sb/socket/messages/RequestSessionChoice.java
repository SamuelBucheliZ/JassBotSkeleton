package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.socket.responses.ChooseSession;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;
import com.zuehlke.jasschallenge.client.sb.socket.responses.SessionChoiceData;

import java.util.Optional;

public class RequestSessionChoice implements Message {
    @Override
    public Optional<Response> dispatch(Game game) {
        SessionChoiceData sessionChoice = game.getSessionChoiceData();
        Response response = new ChooseSession(sessionChoice);
        return Optional.of(response);
    }
}
