package com.zuehlke.jasschallenge.client.sb.model;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;

import java.util.Arrays;
import java.util.List;

public class Stich {
    public static final int STICH_SIZE = 4;

    private String name;
    private int id;
    private List<Card> playedCards;
    private List<Team> teams;

    @Override
    public String toString() {
        return String.format("Stich{name='%s', id=%d, playedCards=%s, teams=%s}", name, id, playedCards, teams);
    }

    public int getId() {
        return id;
    }

}
