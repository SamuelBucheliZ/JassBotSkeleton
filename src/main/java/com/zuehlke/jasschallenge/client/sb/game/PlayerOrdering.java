package com.zuehlke.jasschallenge.client.sb.game;

import com.google.common.base.Preconditions;
import com.zuehlke.jasschallenge.client.sb.model.Team;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlayerOrdering {
    List<Integer> playerIdsInOrder;

    public static PlayerOrdering fromTeamList(List<Team> teams) {
        Preconditions.checkArgument(teams.size() == Team.NUMBER_OF_TEAMS);
        Preconditions.checkArgument(teams.get(0).getPlayers().size() == Team.TEAM_SIZE);
        Preconditions.checkArgument(teams.get(0).getPlayers().size() == teams.get(1).getPlayers().size());
        // TODO: Is player ordering always as given in BroadCastTeams?
        // TODO: Is there any less involved way to merge streams in order?
        List<Integer> playerIdsInOrder = IntStream.range(0, teams.get(0).getPlayers().size())
                .mapToObj(i -> Arrays.asList(teams.get(0).getPlayers().get(i).getId(), teams.get(1).getPlayers().get(i).getId()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return new PlayerOrdering(playerIdsInOrder);
    }

    public PlayerOrdering(List<Integer> playerIdsInOrder) {
        this.playerIdsInOrder = new ArrayList<>(playerIdsInOrder);
    }

    public int getNextPlayerIdFrom(int playerId) {
        return getPlayerIdFrom(playerId, 1);
    }

    public List<Integer> getPlayerIdsInOrder() {
        return Collections.unmodifiableList(playerIdsInOrder);
    }

    public int getPlayerIdFrom(int playerId, int offset) {
        Preconditions.checkArgument(offset >= 0);
        int index = playerIdsInOrder.indexOf(playerId);
        int nextIndex = (index + offset) % playerIdsInOrder.size();
        return playerIdsInOrder.get(nextIndex);
    }
}
