package net.adiherzog.jassbot.model;

public class Team {

    private String name;
    private int points;
    private int currentRoundPoints;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getCurrentRoundPoints() {
        return currentRoundPoints;
    }

    public void setCurrentRoundPoints(int currentRoundPoints) {
        this.currentRoundPoints = currentRoundPoints;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", points=" + points +
                ", currentRoundPoints=" + currentRoundPoints +
                '}';
    }

}
