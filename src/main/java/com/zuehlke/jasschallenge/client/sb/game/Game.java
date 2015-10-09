package com.zuehlke.jasschallenge.client.sb.game;

import com.zuehlke.jasschallenge.client.sb.model.Stich;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.Strategy;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;

import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfMode;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Set;

public class Game {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private final String playerName;
    private final String sessionName;
    private final Strategy strategy;

    private GameState gameState = new GameState();

    public Game(String playerName, String sessionName, Strategy strategy) {
        this.strategy = strategy;
        logger.info(playerName + ": Created new game for player");
        this.playerName = playerName;
        this.sessionName = sessionName;
    }

    public void startSession() {
        strategy.onSessionStarted();
    }

    public void finishSession() {
        strategy.onSessionFinished();
    }

    public void startGame(Trumpf trumpf) {
        gameState.setTrumpf(trumpf);
        strategy.onGameStarted();
        log(trumpf.toString());
    }

    public void finishGame() {
        gameState.resetAfterGameRound();
        strategy.onGameFinished();
    }

    public Trumpf requestTrumpf() {
        Trumpf trumpf = strategy.onRequestTrumpf(gameState.getMyCards());
        //Trumpf trumpf = new TrumpfChooser().requestTrumpf(gameState.getMyCards());
        if (!trumpf.getMode().equals(TrumpfMode.SCHIEBE)) {
            gameState.setIMadeTrumpf(true);
        }
        return trumpf;
    }

    public Card requestCard(List<Card> cardsOnTable) {
        gameState.setCardsOnTable(cardsOnTable);
        Card card = strategy.onRequestCard(gameState);
        //Card card = cardChooser.chooseCard(gameState);
        gameState.getMyCards().remove(card);
        return card;
    }

    public void cardsPlayed(List<Card> playedCards) {
        strategy.onMoveMade();
    }

    public void cardsDealt(Set<Card> cards) {
        gameState.getMyCards().addAll(cards);
    }

    public void stichMade(Stich stich) {
        logger.info(playerName + ": " + stich);
        gameState.startNextRound();
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
