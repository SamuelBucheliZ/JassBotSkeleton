package com.zuehlke.jasschallenge.client.sb.model;

import com.google.gson.annotations.SerializedName;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.socket.messages.PointsInformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stich {
    public static final int STICH_SIZE = 4;
    public static final int LAST_STICH_POINTS = 5;

    @SerializedName("name")
    private final String playerName;
    @SerializedName("id")
    private final int playerId;
    private final List<Card> playedCards;
    @SerializedName("teams")
    private final List<PointsInformation> pointsInformation;

    public Stich(String name, int playerId, List<Card> playedCards, List<PointsInformation> pointsInformation) {
        this.playerName = name;
        this.playerId = playerId;
        this.playedCards = new ArrayList<>(playedCards);
        this.pointsInformation = new ArrayList<>(pointsInformation);
    }

    public int getPlayerId() {
        return playerId;
    }

    @Override
    public String toString() {
        return String.format("Stich{playerName='%s', playerId=%d, playedCards=%s, pointsInformation=%s}", playerName, playerId, playedCards, pointsInformation);
    }

    public String getPlayerName() {
        return playerName;
    }

    public List<Card> getPlayedCards() {
        return Collections.unmodifiableList(playedCards);
    }

    public List<PointsInformation> getPointsInformation() {
        return Collections.unmodifiableList(pointsInformation);
    }
}
