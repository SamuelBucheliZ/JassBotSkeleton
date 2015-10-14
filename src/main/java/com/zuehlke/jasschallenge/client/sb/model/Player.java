package com.zuehlke.jasschallenge.client.sb.model;

public class Player {
    private final int id;
    private final String name;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("Player{name='%s', id=%d}", name, id);
    }
}
