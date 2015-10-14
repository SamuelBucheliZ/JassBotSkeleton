package com.zuehlke.jasschallenge.client.sb.game;

import com.zuehlke.jasschallenge.client.sb.model.Team;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PlayerOrderingTest {
    GameDataFactory gameDataFactory = new GameDataFactory();

    @Test
    public void givenTeamList_isCorrectlyConvertedToPlayerOrdering() {
        List<Team> teams = gameDataFactory.createTeamList();

        PlayerOrdering order = PlayerOrdering.fromTeamList(teams);

        List<Integer> playerIdsInOrder = order.playerIdsInOrder;

        assertThat(playerIdsInOrder.get(0), is(2));
        assertThat(playerIdsInOrder.get(1), is(0));
        assertThat(playerIdsInOrder.get(2), is(3));
        assertThat(playerIdsInOrder.get(3), is(1));
    }

    @Test
    public void givenPlayerIdsInOrder_returnsCorrectNextPlayers() {
        List<Integer> playerIdsInOrder = Arrays.asList(3,2,1,0);

        PlayerOrdering order = new PlayerOrdering(playerIdsInOrder);

        assertThat(order.getNextPlayerIdFrom(1), is(0));
        assertThat(order.getNextPlayerIdFrom(0), is(3));
        assertThat(order.getNextPlayerIdFrom(2), is(1));
        assertThat(order.getNextPlayerIdFrom(3), is(2));

        assertThat(order.getPlayerIdFrom(2,3), is(3));
        assertThat(order.getPlayerIdFrom(1,2), is(3));
        assertThat(order.getPlayerIdFrom(3,3), is(0));
    }


}
