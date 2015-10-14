package com.zuehlke.jasschallenge.client.sb.socket.responses;

public class ChooseSession implements Response {
    ResponseType type = ResponseType.CHOOSE_SESSION;
    private final SessionChoiceData data;

    public ChooseSession(SessionChoiceData sessionChoice) {
        this.data = sessionChoice;
    }
}
