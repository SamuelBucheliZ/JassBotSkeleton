package com.zuehlke.jasschallenge.client.sb.game;

import com.google.common.base.Preconditions;
import com.zuehlke.jasschallenge.client.sb.model.Player;
import com.zuehlke.jasschallenge.client.sb.model.Stich;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.Strategy;
import com.zuehlke.jasschallenge.client.sb.model.Team;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.socket.responses.SessionChoice;
import com.zuehlke.jasschallenge.client.sb.socket.responses.SessionChoiceData;
import com.zuehlke.jasschallenge.client.sb.socket.responses.SessionType;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.List;
import java.util.Set;


public class Game {
    private static final Logger logger = LogManager.getLogger(Game.class);

    private final SessionInfo sessionInfo;
    private final Strategy strategy;

    private GameState state;

    public Game(String playerName, String sessionName, SessionChoice sessionChoice, SessionType sessionType, Strategy strategy) {
        logger.info("{}: Created new game for player {}.", playerName, playerName);
        this.sessionInfo = new SessionInfo(playerName, sessionName, sessionChoice, sessionType);
        this.strategy = strategy;
    }

    public void joinSession(Player player) {
        // TODO: Are there any issues if players use the same name?
        if (!sessionInfo.playerIdIsPresent() && sessionInfo.getPlayerName().equals(player.getName())) {
            sessionInfo.setPlayerId(player.getId());
        }
    }

    public void startSession(List<Team> teams) {
        Preconditions.checkState(sessionInfo.playerIdIsPresent());
        this.sessionInfo.setPlayerOrderingAndPartnerId(teams);
        strategy.onSessionStarted(sessionInfo);
    }

    public void finishSession(String message) {
        logger.info("{}: Session finished: {}", sessionInfo.getPlayerName(), message);
        // TODO: Reset player order and session info?
        strategy.onSessionFinished(sessionInfo);
    }

    public void startGame(Trumpf trumpf) {
        state.setTrumpf(trumpf);
        strategy.onGameStarted(state);
    }

    public void finishGame(String message) {
        logger.info("{}: Game finished for player {}.", sessionInfo.getPlayerName(), message);
        strategy.onGameFinished(state);
    }

    public Trumpf requestTrumpf(boolean isGeschoben) {
        if (!isGeschoben) {
            state.setCurrentPlayer(sessionInfo.getPlayerId());
        }
        Trumpf trumpf = strategy.onRequestTrumpf(state.getMyCards(), isGeschoben);
        return trumpf;
    }

    public Card requestCard(List<Card> cardsOnTable) {
        Preconditions.checkArgument(state.getCardsOnTable().equals(cardsOnTable));
        state.setCurrentPlayer(sessionInfo.getPlayerId());
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
        logger.error("{}: Card {} rejected.", sessionInfo.getPlayerName(), card);
        state.undoPlay(card);
    }

    public void cardsDealt(Set<Card> cards) {
        state = new GameState(sessionInfo, cards);
    }

    public void stichMade(Stich stich) {
        logger.debug("{}: {}", sessionInfo.getPlayerName(), stich.toString());
        state.startNextRound(stich);
    }

    public String getPlayerName() {
        return sessionInfo.getPlayerName();
    }

    public String getSessionName() {
        return sessionInfo.getSessionName();
    }

    public SessionChoiceData getSessionChoiceData() {
        return new SessionChoiceData(sessionInfo.getSessionChoice(), sessionInfo.getSessionName(), sessionInfo.getSessionType());
    }

    public void log(String message) {
        logger.info("{}: {}", sessionInfo.getPlayerName(), message);
    }
}
