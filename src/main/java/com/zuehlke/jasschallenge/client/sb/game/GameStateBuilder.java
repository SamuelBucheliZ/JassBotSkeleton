package com.zuehlke.jasschallenge.client.sb.game;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;

import java.util.*;

public class GameStateBuilder {
    private Set<Card> myCards = new HashSet<>();
    private Stack<Card> playedCards = new Stack<>();
    private Optional<Trumpf> trumpf = Optional.empty();
    private List<Card> cardsOnTable = new LinkedList<>();
    private boolean iMadeTrumpf = false;
    private int round = 0;


    public GameStateBuilder setMyCards(Set<Card> myCards) {
        this.myCards = myCards;
        return this;
    }

    public GameStateBuilder setPlayedCards(Stack<Card> playedCards) {
        this.playedCards = playedCards;
        return this;
    }

    public GameStateBuilder setTrumpf(Trumpf trumpf) {
        this.trumpf = Optional.of(trumpf);
        return this;
    }

    public GameStateBuilder setCardsOnTable(List<Card> cardsOnTable) {
        this.cardsOnTable = cardsOnTable;
        return this;
    }

    public GameStateBuilder setIMadeTrumpf(boolean iMadeTrumpf) {
        this.iMadeTrumpf = iMadeTrumpf;
        return this;
    }

    public GameStateBuilder setRound(int round) {
        this.round = round;
        return this;
    }

    public GameState build() {
        return new GameState(myCards, playedCards, trumpf, cardsOnTable, iMadeTrumpf, round);
    }
}
