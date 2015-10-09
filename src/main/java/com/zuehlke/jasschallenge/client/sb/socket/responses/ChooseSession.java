package com.zuehlke.jasschallenge.client.sb.socket.responses;

import com.zuehlke.jasschallenge.client.sb.socket.messages.SessionChoice;

public class ChooseSession implements Response {
    ResponseType type = ResponseType.CHOOSE_SESSION;
    private final SessionChoice data;

    public ChooseSession(SessionChoice sessionChoice) {
        this.data = sessionChoice;
    }
}
