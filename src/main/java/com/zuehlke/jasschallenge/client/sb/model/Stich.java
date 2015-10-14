package com.zuehlke.jasschallenge.client.sb.model;

import com.google.gson.annotations.SerializedName;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stich {
    public static final int STICH_SIZE = 4;

    @SerializedName("name")
    private final String playerName;
    @SerializedName("id")
    private final int playerId;
    private final List<Card> playedCards;
    private final List<Team> teams;

    public Stich(String name, int playerId, List<Card> playedCards, List<Team> teams) {
        this.playerName = name;
        this.playerId = playerId;
        this.playedCards = new ArrayList<>(playedCards);
        this.teams = new ArrayList<>(teams);
    }

    public int getPlayerId() {
        return playerId;
    }

    @Override
    public String toString() {
        return String.format("Stich{playerName='%s', playerId=%d, playedCards=%s, teams=%s}", playerName, playerId, playedCards, teams);
    }

    public String getPlayerName() {
        return playerName;
    }

    public List<Card> getPlayedCards() {
        return Collections.unmodifiableList(playedCards);
    }

    public List<Team> getTeams() {
        return Collections.unmodifiableList(teams);
    }
}
