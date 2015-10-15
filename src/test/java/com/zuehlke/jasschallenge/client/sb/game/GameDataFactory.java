package com.zuehlke.jasschallenge.client.sb.game;

import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.Strategy;
import com.zuehlke.jasschallenge.client.sb.model.Player;
import com.zuehlke.jasschallenge.client.sb.model.Team;
import com.zuehlke.jasschallenge.client.sb.socket.responses.SessionChoice;
import com.zuehlke.jasschallenge.client.sb.socket.responses.SessionType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameDataFactory {

    public List<Team> createTeamListWithPlayerOrder2031() {
        Player player0 = new Player(0, "player0");
        Player player1 = new Player(1, "player1");
        Player player2 = new Player(2, "player2");
        Player player3 = new Player(3, "player3");

        Team team0 = new Team("team0", Arrays.asList(player2, player3));
        Team team1 = new Team("team1", Arrays.asList(player0, player1));

        return Arrays.asList(team0, team1);
    }

    public List<Team> createTeamListWithPlayersUsingIdenticalNames() {
        Player player0 = new Player(0, "player");
        Player player1 = new Player(1, "player");
        Player player2 = new Player(2, "otherPlayer");
        Player player3 = new Player(3, "otherPlayer");

        Team team0 = new Team("team0", Arrays.asList(player2, player3));
        Team team1 = new Team("team1", Arrays.asList(player0, player1));

        return Arrays.asList(team0, team1);
    }

    public SessionInfo createSessionInfoForPlayerWithId3() {
        SessionInfo sessionInfo = new SessionInfo("player3", "player3", "someSession", SessionChoice.AUTOJOIN, SessionType.TOURNAMENT);
        sessionInfo.setPlayerId(3);
        return sessionInfo;
    }

    public List<Team> createTeamListWithPlayersUsingIdenticalNamesPermuted() {
        Player player0 = new Player(0, "otherPlayer");
        Player player1 = new Player(1, "otherPlayer");
        Player player2 = new Player(2, "player");
        Player player3 = new Player(3, "player");


        Team team0 = new Team("team0", Arrays.asList(player0, player1));
        Team team1 = new Team("team1", Arrays.asList(player2, player3));

        return Arrays.asList(team0, team1);
    }
}
