package com.zuehlke.jasschallenge.client.sb.game;

import com.google.common.base.Preconditions;
import com.zuehlke.jasschallenge.client.sb.jasslogic.rules.AllowedCards;
import com.zuehlke.jasschallenge.client.sb.jasslogic.rules.AllowedCardsRules;
import com.zuehlke.jasschallenge.client.sb.model.Move;
import com.zuehlke.jasschallenge.client.sb.model.Stich;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class GameState {
    private static final Logger logger = LogManager.getLogger(GameState.class);

    private final SessionInfo sessionInfo;

    private Set<Card> myCards = new HashSet<>();
    private Optional<Trumpf> trumpf = Optional.empty();
    private List<Move> moves = new ArrayList<>();
    private List<Card> cardsOnTable = new LinkedList<>();
    private Optional<Integer> currentPlayer = Optional.empty();
    private int round = 0;

    public GameState(SessionInfo sessionInfo, Collection<Card> myCards) {
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
            logger.trace("{}: Added card {} to table, current player is now {}.", sessionInfo.getLocalPlayerName(), card, currentPlayer.get());
        }
    }

    public Move getLastMove() {
        int lastIndex = this.moves.size()-1;
        return this.moves.get(lastIndex);
    }

    public void startNextRound(Stich lastStich) {
        Preconditions.checkState(cardsOnTable.size() == Stich.STICH_SIZE);
        cardsOnTable.clear();
        currentPlayer = Optional.of(lastStich.getPlayerId());
        logger.trace("{}: Starting next round, current player is now {}.", sessionInfo.getLocalPlayerName(), currentPlayer.get());
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
        Set<Card> playedCards = moves.stream().map(Move::getCard).collect(Collectors.toSet());
        return playedCards;
    }

    public int getRound() {
        return round;
    }

    public int getPlayerId() {
        return sessionInfo.getPlayerId();
    }

    public int getPartnerId() {
        return sessionInfo.getPartnerId();
    }

    public PlayerOrdering getPlayerOrdering() {
        return sessionInfo.getPlayerOrdering();
    }

    @Override
    public String toString() {
        return String.format("GameState{myCards=%s, trumpf=%s, cardsOnTable=%s, round=%d}", myCards, trumpf, cardsOnTable, round);
    }

    public boolean isLastRound() {
        return this.round == Game.NUMBER_OF_ROUNDS;
    }
}
