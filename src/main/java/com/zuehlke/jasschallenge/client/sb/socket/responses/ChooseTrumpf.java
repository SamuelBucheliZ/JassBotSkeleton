package com.zuehlke.jasschallenge.client.sb.socket.responses;

import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;

public class ChooseTrumpf implements Response {
    private final ResponseType type = ResponseType.CHOOSE_TRUMPF;
    private final Trumpf data;

    public ChooseTrumpf(Trumpf trumpf) {
        this.data = trumpf;
    }
}
