package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;

import java.util.List;
import java.util.Optional;

public class BroadcastGameFinished implements Message {
    private final List<PointsInformation> pointsInformation;

    public BroadcastGameFinished(List<PointsInformation> pointsInformation) {
        this.pointsInformation = pointsInformation;
    }

    @Override
    public Optional<Response> dispatch(Game game) {
        game.finishGame(pointsInformation);
        return Optional.empty();
    }
}
