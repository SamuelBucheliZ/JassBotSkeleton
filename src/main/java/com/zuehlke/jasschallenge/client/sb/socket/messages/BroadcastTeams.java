package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.model.Team;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BroadcastTeams implements Message {
    private final List<Team> teams;

    public BroadcastTeams(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public Optional<Response> dispatch(Game game) {
        game.startSession(teams);
        return Optional.empty();
    }

    public List<Team> getTeams() {
        return new LinkedList<>(this.teams);
    }
}
