package com.zuehlke.jasschallenge.client.sb.game;

import com.google.common.base.Preconditions;
import com.zuehlke.jasschallenge.client.sb.model.Stich;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.Strategy;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfMode;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Set;

public class Game {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private final String playerName;
    private final String sessionName;
    private final Strategy strategy;

    private GameState state;

    public Game(String playerName, String sessionName, Strategy strategy) {
        this.strategy = strategy;
        logger.info(playerName + ": Created new game for player");
        this.playerName = playerName;
        this.sessionName = sessionName;
    }

    public void startSession() {
        strategy.onSessionStarted(state);
    }

    public void finishSession() {
        strategy.onSessionFinished(state);
    }

    public void startGame(Trumpf trumpf) {
        state.setTrumpf(trumpf);
        strategy.onGameStarted(state);
        log(trumpf.toString());
    }

    public void finishGame() {
        strategy.onGameFinished(state);
    }

    public Trumpf requestTrumpf(boolean isSchiebenAllowed) {
        Trumpf trumpf = strategy.onRequestTrumpf(state.getMyCards(), isSchiebenAllowed);
        if (!TrumpfMode.SCHIEBE.equals(trumpf.getMode())) {
            state.setIMadeTrumpf();
        }
        return trumpf;
    }

    public Card requestCard(List<Card> cardsOnTable) {
        Preconditions.checkArgument(state.getCardsOnTable().equals(cardsOnTable));
        Card card = strategy.onRequestCard(state);
        state.doPlay(card);
        return card;
    }

    public void cardsPlayed(List<Card> playedCards) {
        int indexOfLastElement = playedCards.size() - 1;
        state.addToTable(playedCards.get(indexOfLastElement));
        strategy.onMoveMade(state);
    }

    public void cardRejected(Card card) {
        state.undoPlay(card);
    }

    public void cardsDealt(Set<Card> cards) {
        state = new GameState(cards);
    }

    public void stichMade(Stich stich) {
        logger.info(playerName + ": " + stich);
        state.startNextRound();
    }

    public void log(String message) {
        logger.info(playerName + ": " + message);
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getSessionName() {
        return sessionName;
    }
}
