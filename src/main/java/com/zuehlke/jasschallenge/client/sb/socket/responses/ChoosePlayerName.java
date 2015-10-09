package com.zuehlke.jasschallenge.client.sb.socket.responses;

public class ChoosePlayerName implements Response {
    private final ResponseType type = ResponseType.CHOOSE_PLAYER_NAME;
    private final String data;

    public ChoosePlayerName(String playerName) {
        this.data = playerName;
    }
}
