package com.zuehlke.jasschallenge.client.sb.game;

import com.google.common.base.Preconditions;
import com.zuehlke.jasschallenge.client.sb.jasslogic.rules.AllowedCards;
import com.zuehlke.jasschallenge.client.sb.jasslogic.rules.AllowedCardsRules;
import com.zuehlke.jasschallenge.client.sb.model.Move;
import com.zuehlke.jasschallenge.client.sb.model.Stich;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;

import java.util.*;

public class GameState {
    private final SessionInfo sessionInfo;

    private Set<Card> myCards = new HashSet<>();
    private Optional<Trumpf> trumpf = Optional.empty();
    private List<Move> moves = new ArrayList<>();
    private List<Card> cardsOnTable = new LinkedList<>();
    private Optional<Integer> currentPlayer = Optional.empty();
    private int round = 0;


    public GameState(SessionInfo sessionInfo, Set<Card> myCards) {
        this.sessionInfo = sessionInfo;
        this.myCards = new HashSet<>(myCards);
    }


    public void undoPlay(Card card) {
        this.myCards.add(card);
    }

    public void doPlay(Card card) {
        Preconditions.checkArgument(this.myCards.contains(card));
        this.myCards.remove(card);
    }


    public Set<Card> getMyCards() {
        return Collections.unmodifiableSet(myCards);
    }

    public Trumpf getTrumpf() {
        return trumpf.get();
    }

    public void setTrumpf(Trumpf trumpf) {
        Preconditions.checkState(!this.trumpf.isPresent());
        this.trumpf = Optional.of(trumpf);
    }

    public List<Card> getCardsOnTable() {
        return Collections.unmodifiableList(cardsOnTable);
    }

    public void addToTable(Card card) {
        this.moves.add(new Move(currentPlayer, card));
        this.cardsOnTable.add(card);
        if (currentPlayer.isPresent()) {
            currentPlayer = Optional.of(sessionInfo.getNextPlayerIdFrom(currentPlayer.get()));
        }
    }

    public void startNextRound(Stich lastStich) {
        Preconditions.checkState(cardsOnTable.size() == Stich.STICH_SIZE);
        cardsOnTable.clear();
        currentPlayer = Optional.of(lastStich.getPlayerId());
        round++;
    }

    public void setCurrentPlayer(int playerId) {
        if (!this.currentPlayer.isPresent()) {
            this.currentPlayer = Optional.of(playerId);
        }
    }

    public Set<Card> getAllowedCardsToPlay() {
        AllowedCards allowedCards = AllowedCardsRules.getFor(getMyCards(), getTrumpf(), getCardsOnTable());
        return allowedCards.get();
    }

    public int getCurrentPlayer() {
        return currentPlayer.get();
    }

    public Set<Card> getPlayedCards() {
        Set<Card> playedCards = new HashSet<>();
        for (Move move: moves) {
            playedCards.add(move.getCard());
        }
        return playedCards;
    }

    @Override
    public String toString() {
        return String.format("GameState{myCards=%s, trumpf=%s, cardsOnTable=%s, round=%d}", myCards, trumpf, cardsOnTable, round);
    }

}
