package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;

import java.util.Optional;


public class BroadCastTournamentRankingTable implements Message {
    private final String message;

    public BroadCastTournamentRankingTable(String message) {
        this.message = message;
    }

    @Override
    public Optional<Response> dispatch(Game game) {
        game.log(message);
        return Optional.empty();
    }
}
