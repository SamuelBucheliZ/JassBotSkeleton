package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;

import java.util.Optional;

public class BroadcastWinnerTeam implements Message {
    private final PointsInformation winningTeamPointsInformation;

    public BroadcastWinnerTeam(PointsInformation winningTeamPointsInformation) {
        this.winningTeamPointsInformation = winningTeamPointsInformation;
    }

    @Override
    public Optional<Response> dispatch(Game game) {
        game.finishSession(winningTeamPointsInformation);
        return Optional.empty();
    }
}
