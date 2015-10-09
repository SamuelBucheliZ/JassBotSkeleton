package com.zuehlke.jasschallenge.client.sb.game;

import com.google.common.base.Preconditions;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.jasslogic.rules.AllowedCards;
import com.zuehlke.jasschallenge.client.sb.jasslogic.rules.AllowedCardsRules;

import java.util.*;

public class GameState {

    private final Set<Card> myCards = new HashSet<>();
    private final Stack<Card> playedCards = new Stack<>();
    private Optional<Trumpf> trumpf = Optional.empty();
    private List<Card> cardsOnTable = new LinkedList<>();
    private boolean iMadeTrumpf = false;
    private int round = 0;

    public GameState(Set<Card> myCards) {
        this.myCards.addAll(myCards);
    }

    GameState(Set<Card> myCards, Stack<Card> playedCards, Optional<Trumpf> trumpf, List<Card> cardsOnTable, boolean iMadeTrumpf, int round) {
        this.myCards.addAll(myCards);
        this.playedCards.addAll(playedCards);
        this.trumpf = trumpf;
        this.cardsOnTable = new ArrayList<>(cardsOnTable);
        this.iMadeTrumpf = iMadeTrumpf;
        this.round = round;
    }

    public Set<Card> getMyCards() {
        return new HashSet<>(myCards);
    }

    public void undoPlay(Card card) {
        Preconditions.checkArgument(this.playedCards.peek().equals(card));
        this.playedCards.pop();
        this.myCards.add(card);
    }

    public void doPlay(Card card) {
        Preconditions.checkArgument(this.myCards.contains(card));
        this.myCards.remove(card);
        this.playedCards.push(card);
    }

    public Trumpf getTrumpf() {
        return trumpf.get();
    }

    public void setTrumpf(Trumpf trumpf) {
        Preconditions.checkState(!this.trumpf.isPresent());
        this.trumpf = Optional.of(trumpf);
    }

    public List<Card> getCardsOnTable() {
        return new LinkedList<>(cardsOnTable);
    }

    public void addToTable(Card card) {
        this.cardsOnTable.add(card);
    }

    public boolean isIMadeTrumpf() {
        return iMadeTrumpf;
    }

    public void setIMadeTrumpf() {
        Preconditions.checkState(!this.iMadeTrumpf);
        this.iMadeTrumpf = true;
    }

    public void startNextRound() {
        this.cardsOnTable.clear();
        round++;
    }

    public int getRound() {
        return round;
    }

    public Set<Card> getAllowedCardsToPlay() {
        AllowedCards allowedCards = AllowedCardsRules.getFor(getMyCards(), getTrumpf(), getCardsOnTable());
        return allowedCards.get();
    }

    @Override
    public String toString() {
        return "GameState " + System.identityHashCode(this) + " {" +
                "myCards=" + myCards +
                ", trumpf=" + trumpf +
                ", cardsOnTable=" + cardsOnTable +
                ", iMadeTrumpf=" + iMadeTrumpf +
                ", round=" + round +
                '}';
    }

}
