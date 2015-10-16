package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy;

class CardEvaluation {
    private int ourPoints = 0;
    private int theirPoints = 0;

    public void addOurPoints(int ourPoints) {
        this.ourPoints += ourPoints;
    }

    public void addTheirPoints(int theirPoints) {
        this.theirPoints += theirPoints;
    }

    public void add(CardEvaluation that) {
        this.ourPoints += that.ourPoints;
        this.theirPoints += that.theirPoints;
    }

    public int getOurPoints() {
        return ourPoints;
    }

    public int getTheirPoints() {
        return theirPoints;
    }
}
