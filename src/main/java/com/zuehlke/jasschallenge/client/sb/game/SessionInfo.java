package com.zuehlke.jasschallenge.client.sb.game;


import com.zuehlke.jasschallenge.client.sb.model.Team;
import com.zuehlke.jasschallenge.client.sb.socket.responses.SessionChoice;
import com.zuehlke.jasschallenge.client.sb.socket.responses.SessionType;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class SessionInfo {
    private final String playerName;
    private final String sessionName;
    private final SessionChoice sessionChoice;
    private final SessionType sessionType;

    private Optional<Integer> playerId;
    private Optional<Integer> partnerId;
    private Optional<PlayerOrdering> playerOrdering;

    public SessionInfo(String playerName, String sessionName, SessionChoice sessionChoice, SessionType sessionType) {
        this.playerName = playerName;
        this.sessionName = sessionName;
        this.sessionChoice = sessionChoice;
        this.sessionType = sessionType;
        this.playerId = Optional.empty();
        this.partnerId = Optional.empty();
        this.playerOrdering = Optional.empty();
    }

    public void setPlayerOrderingAndPartnerId(List<Team> teams) {
        this.playerOrdering = Optional.of(PlayerOrdering.fromTeamList(teams));
        Predicate<Team> containsPlayerId = team -> team.getPlayers().stream()
                .filter(player -> player.getId() == playerId.get())
                .findAny().isPresent();
        this.partnerId = Optional.of(teams.stream()
                .filter(containsPlayerId)
                .flatMap(team -> team.getPlayers().stream())
                .filter(player -> player.getId() != playerId.get())
                .findAny().get().getId());
    }

    public void setPlayerId(int playerId) {
        this.playerId = Optional.of(playerId);
    }

    public boolean playerIdIsPresent() {
        return this.playerId.isPresent();
    }

    public int getPlayerId() {
        return playerId.get();
    }

    public int getPartnerId() {
        return partnerId.get();
    }

    public PlayerOrdering getPlayerOrdering() {
        return playerOrdering.get();
    }

    public int getNextPlayerIdFrom(int playerId) {
        return getPlayerOrdering().getNextPlayerIdFrom(playerId);
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getSessionName() {
        return sessionName;
    }

    public SessionChoice getSessionChoice() {
        return sessionChoice;
    }

    public SessionType getSessionType() {
        return sessionType;
    }
}
