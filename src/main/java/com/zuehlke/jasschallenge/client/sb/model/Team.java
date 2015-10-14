package com.zuehlke.jasschallenge.client.sb.model;

import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Team {
    public static final int TEAM_SIZE = 2;
    public static final int NUMBER_OF_TEAMS = 2;

    private final String name;
    private final List<Player> players;
    private final int points;
    private final int currentRoundPoints;

    public Team(String name, List<Player> players) {
        Preconditions.checkArgument(TEAM_SIZE == players.size());
        this.name = name;
        this.players = new LinkedList<>(players);
        this.points = 0;
        this.currentRoundPoints = 0;
    }

    public String getName() {
        return name;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public int getPoints() {
        return points;
    }

    public int getCurrentRoundPoints() {
        return currentRoundPoints;
    }

    @Override
    public String toString() {
        return String.format("Team{name='%s', points=%d, currentRoundPoints=%d}", name, points, currentRoundPoints);
    }
}
