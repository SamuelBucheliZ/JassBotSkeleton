package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.zuehlke.jasschallenge.client.sb.model.Player;

import java.util.List;

public class SessionJoinedData {
    private final String sessionName;
    private final Player player;
    private final List<Player> playersInSession;

    public SessionJoinedData(String sessionName, Player player, List<Player> playersInSession) {
        this.sessionName = sessionName;
        this.player = player;
        this.playersInSession = playersInSession;
    }

    public Player getPlayer() {
        return this.player;
    }
}
