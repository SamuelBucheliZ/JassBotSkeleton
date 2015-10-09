package com.zuehlke.jasschallenge.client.sb.game;

import com.google.common.base.Preconditions;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.jasslogic.rules.AllowedCards;
import com.zuehlke.jasschallenge.client.sb.jasslogic.rules.AllowedCardsRules;

import java.util.*;

import java.util.stream.Stream;

public class GameState {

    private Set<Card> myCards = new HashSet<>();
    private Trumpf trumpf;
    private List<Card> cardsOnTable;
    private boolean iMadeTrumpf = false;
    private int round = 0;

    public Set<Card> getMyCards() {
        return myCards;
    }

    public void setMyCards(Set<Card> myCards) {
        this.myCards = myCards;
    }

    public Trumpf getTrumpf() {
        return trumpf;
    }

    public void setTrumpf(Trumpf trumpf) {
        this.trumpf = trumpf;
    }

    public List<Card> getCardsOnTable() {
        return cardsOnTable;
    }

    public void setCardsOnTable(List<Card> cardsOnTable) {
        this.cardsOnTable = cardsOnTable;
    }

    public boolean isIMadeTrumpf() {
        return iMadeTrumpf;
    }

    public void setIMadeTrumpf(boolean iMadeTrumpf) {
        this.iMadeTrumpf = iMadeTrumpf;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void startNextRound() {
        round++;
    }

    public void checkIsValid() {
        Preconditions.checkNotNull(cardsOnTable);
        Preconditions.checkNotNull(myCards);
        Preconditions.checkNotNull(trumpf);
        Preconditions.checkArgument(round >= 0 && round <= 8);
    }

    public void resetAfterGameRound() {
        myCards = new HashSet<>();
        trumpf = null;
        cardsOnTable = null;
        iMadeTrumpf = false;
        round = 0;
    }

    public Set<Card> getAllowedCardsToPlay() {
        AllowedCards allowedCards = AllowedCardsRules.getFor(myCards, trumpf, cardsOnTable);
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
