package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy;

public class PointsCounter {
    public static final int POINTS_PER_GAME = 157;
    private int ourPoints = 0;
    private int theirPoints = 0;

    public PointsCounter copy() {
        PointsCounter other = new PointsCounter();
        other.add(this);
        return other;
    }

    public void addOurPoints(int ourPoints) {
        this.ourPoints += ourPoints;
    }

    public void addTheirPoints(int theirPoints) {
        this.theirPoints += theirPoints;
    }

    public void add(PointsCounter that) {
        this.ourPoints += that.ourPoints;
        this.theirPoints += that.theirPoints;
    }

    public int getOurPoints() {
        return ourPoints;
    }

    public int getTheirPoints() {
        return theirPoints;
    }

    public boolean weWin() {
        return ourPoints > theirPoints;
    }

    @Override
    public String toString() {
        return String.format("PointsCounter{ourPoints=%d, theirPoints=%d}", ourPoints, theirPoints);
    }
}
