package com.zuehlke.jasschallenge.client.sb.model;

import com.google.common.base.Preconditions;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Team {
    public static final int TEAM_SIZE = 2;
    public static final int NUMBER_OF_TEAMS = 2;

    @SerializedName("name")
    private final String teamName;
    private final List<Player> players;

    public Team(String teamName, List<Player> players) {
        Preconditions.checkArgument(TEAM_SIZE == players.size());
        this.teamName = teamName;
        this.players = new LinkedList<>(players);
    }

    public String getTeamName() {
        return teamName;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }


    @Override
    public String toString() {
        return String.format("Team{teamName='%s', players=%s}", teamName, players);
    }
}
