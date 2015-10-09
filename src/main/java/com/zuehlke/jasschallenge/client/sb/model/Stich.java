package com.zuehlke.jasschallenge.client.sb.model;

import com.zuehlke.jasschallenge.client.sb.model.card.Card;

import java.util.Arrays;

public class Stich {

    private String name;
    private int id;
    private Card[] playedCards;
    private Team[] teams;

    @Override
    public String toString() {
        return "Stich{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", playedCards=" + Arrays.toString(playedCards) +
                ", teams=" + Arrays.toString(teams) +
                '}';
    }

}
