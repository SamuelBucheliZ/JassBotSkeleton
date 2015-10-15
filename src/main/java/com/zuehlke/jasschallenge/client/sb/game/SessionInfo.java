package com.zuehlke.jasschallenge.client.sb.game;


import com.google.common.base.Preconditions;
import com.zuehlke.jasschallenge.client.sb.model.Player;
import com.zuehlke.jasschallenge.client.sb.model.Team;
import com.zuehlke.jasschallenge.client.sb.socket.responses.SessionChoice;
import com.zuehlke.jasschallenge.client.sb.socket.responses.SessionType;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class SessionInfo {
    private final String remotePlayerName;
    private final String localPlayerName;
    private final String sessionName;
    private final SessionChoice sessionChoice;
    private final SessionType sessionType;

    private Optional<Integer> playerId;
    private Optional<Integer> partnerId;
    private List<Team> teams;
    private Optional<PlayerOrdering> playerOrdering;

    public SessionInfo(String remotePlayerName, String localPlayerName, String sessionName, SessionChoice sessionChoice, SessionType sessionType) {
        this.remotePlayerName = remotePlayerName;
        this.localPlayerName = localPlayerName;
        this.sessionName = sessionName;
        this.sessionChoice = sessionChoice;
        this.sessionType = sessionType;
        this.playerId = Optional.empty();
        this.partnerId = Optional.empty();
        this.playerOrdering = Optional.empty();
    }

    private Team getMyTeam() {
        Predicate<Team> containsPlayerName = team -> team.getPlayers().stream()
                .filter(player -> player.getId() == playerId.get())
                .findAny().isPresent();
        Team team = teams.stream()
                .filter(containsPlayerName).findAny().get();
        return team;
    }

    public void setPlayerOrderingAndPartnerId(List<Team> teams) {
        this.teams = teams;
        this.playerOrdering = Optional.of(PlayerOrdering.fromTeamList(teams));
        int partnerId = getMyTeam().getPlayers().stream()
                .filter(player -> player.getId() != playerId.get())
                .findAny().get().getId();
        this.partnerId = Optional.of(partnerId);
    }

    public void resetPlayerOrderingAndPartnerId() {
        this.partnerId = Optional.empty();
        this.playerOrdering = Optional.empty();
    }

    public void setPlayerId(int playerId) {
        this.playerId = Optional.of(playerId);
    }

    public void resetPlayerId() {
        this.playerId = Optional.empty();
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

    public String getRemotePlayerName() {
        return remotePlayerName;
    }

    public String getLocalPlayerName() {
        return localPlayerName;
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

    public Player getFirstPlayer() {
        return getMyTeam().getPlayers().get(0);
    }

    public Player getSecondPlayer() {
        return getMyTeam().getPlayers().get(1);    }
}
