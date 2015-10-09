package com.zuehlke.jasschallenge.client.sb.game;

import com.google.common.base.Preconditions;
import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.jasslogic.rules.AllowedCards;
import com.zuehlke.jasschallenge.client.sb.jasslogic.rules.AllowedCardsRules;

import java.util.*;
import org.apache.log4j.Logger;

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

    public Stream<Card> getSafeTricks() {
        if(getCardsOnTable().size() == 3) {
            return getMyAllowedCardsThatCanBeatCurrentCardsOnTable();
        }

        // TODO implement more logic

        return Stream.empty();
    }

    /**
     * Returns the card on the table that would make the trick if the round would finish now
     */
    private Optional<Card> getStrongestCardOnTable() {
        return getCardsOnTableThatAreAllowedToTrick().max(Card.getComperatorForTrumpf(trumpf));
    }

    /**
     * Only the cards that could actually trick if they were the highest. E.g. let's assume the first card is
     * SPADES and trumpf is HEARTS. Then HEARTS and SPADES cards that follow are allowed to trick, but CLUBS
     * and DIAMONDS are not allowed to trick, no matter how high they are.
     */
    private Stream getCardsOnTableThatAreAllowedToTrick() {
        if(cardsOnTable.size() == 0) {
            return Stream.empty();
        } else if (cardsOnTable.size() == 1) {
            return cardsOnTable.stream();
        } else {
            Stream<Card> cardsOnTableThatMatchSuitOfFirstCard = cardsOnTable.stream().filter(card -> card.getSuit().equals(cardsOnTable.get(0).getSuit()));
            Stream<Card> trumpfsOnTable = cardsOnTable.stream().filter(card -> card.isTrumpf(trumpf));
            return Stream.concat(cardsOnTableThatMatchSuitOfFirstCard, trumpfsOnTable);
        }
    }

    /**
     * Returns all cards of my hand that can beat the cards currently on the table.
     * This does not mean that I make a trick in the end, if some other player can play after me.
     */
    public Stream<Card> getMyAllowedCardsThatCanBeatCurrentCardsOnTable() {
        Optional<Card> strongestCardOnTable = getStrongestCardOnTable();
        if(!strongestCardOnTable.isPresent()) {
            return myCards.stream();
        }
        return getAllowedCardsToPlay().stream().filter(card -> card.beatsCard(strongestCardOnTable.get(), trumpf));
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
