package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.google.gson.annotations.SerializedName;

public class PointsInformation {

    @SerializedName("name")
    private final String teamName;
    private final int currentRoundPoints;
    private final int points;


    public PointsInformation(String teamName, int currentRoundPoints, int points) {
        this.teamName = teamName;
        this.currentRoundPoints = currentRoundPoints;
        this.points = points;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getPoints() {
        return points;
    }

    public int getCurrentRoundPoints() {
        return currentRoundPoints;
    }

    @Override
    public String toString() {
        return String.format("PointsInformation{teamName='%s', points=%d, currentRoundPoints=%d}", teamName, points, currentRoundPoints);
    }
}
