package com.zuehlke.jasschallenge.client.sb.game;

import com.google.common.base.Preconditions;
import com.zuehlke.jasschallenge.client.sb.model.Player;
import com.zuehlke.jasschallenge.client.sb.model.Stich;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.Strategy;
import com.zuehlke.jasschallenge.client.sb.model.Team;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfMode;
import com.zuehlke.jasschallenge.client.sb.socket.messages.PointsInformation;
import com.zuehlke.jasschallenge.client.sb.socket.responses.SessionChoice;
import com.zuehlke.jasschallenge.client.sb.socket.responses.SessionChoiceData;
import com.zuehlke.jasschallenge.client.sb.socket.responses.SessionType;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.*;


public class Game {
    private static final Logger logger = LogManager.getLogger(Game.class);

    private final SessionInfo sessionInfo;
    private final Strategy strategy;

    private GameState state;
    private boolean previousCardChoiceRejected = false;

    public Game(String remotePlayerName, String localPlayerName, String sessionName, SessionChoice sessionChoice, SessionType sessionType, Strategy strategy) {
        logger.info("{}: Created new game for player {} and session {}.", localPlayerName, remotePlayerName, sessionName);
        this.sessionInfo = new SessionInfo(remotePlayerName, localPlayerName, sessionName, sessionChoice, sessionType);
        this.strategy = strategy;
    }

    public void joinSession(Player player) {
        /* WARNING: This is a bit of a hack as the player id is not properly communicated to the player.
         * We assume that BroadcastSessionJoined messages are issued in order, and also reset playerId when sending
         * our session choice (see getSessionChoiceData() ) in case of bots using the same name
         * (which still might be an issue, though...).
         */
        if (!sessionInfo.playerIdIsPresent() && sessionInfo.getRemotePlayerName().equals(player.getName())) {
            sessionInfo.setPlayerId(player.getId());
            logger.info("{}: Joined session {} with playerId {}.", sessionInfo.getLocalPlayerName(), sessionInfo.getSessionName(), sessionInfo.getPlayerId());
        }
    }

    public void startSession(List<Team> teams) {
        this.sessionInfo.setPlayerOrderingAndPartnerId(teams);
        strategy.onSessionStarted(sessionInfo);
        logger.info("{}: Started session with teams {}.", sessionInfo.getLocalPlayerName(), teams);
    }

    public void finishSession(PointsInformation winningTeamPointsInformation) {
        logger.info("{}: Session finished, team {} won with {} points using strategy {}.", sessionInfo.getLocalPlayerName(), winningTeamPointsInformation.getTeamName(), winningTeamPointsInformation.getPoints(), strategy.getClass().getSimpleName());
        strategy.onSessionFinished(sessionInfo);
        sessionInfo.resetPlayerOrderingAndPartnerId();
    }

    public void startGame(Trumpf trumpf) {
        state.setTrumpf(trumpf);
        strategy.onGameStarted(state);
        logger.info("{}: Started game with trumpf {}.", sessionInfo.getLocalPlayerName(), trumpf);
    }

    public void finishGame(List<PointsInformation> pointsInformation) {
        logger.info("{}: Game finished, with {} and {}. ", sessionInfo.getLocalPlayerName(), pointsInformation.get(0), pointsInformation.get(1));
        strategy.onGameFinished(state);
    }

    public Trumpf requestTrumpf(boolean isGeschoben) {
        if (!isGeschoben) {
            state.setCurrentPlayer(sessionInfo.getPlayerId());
        }
        Trumpf trumpf = strategy.onRequestTrumpf(state.getMyCards(), isGeschoben);

        // safeguard against illegal choice of trumpf mode SCHIEBE
        if (isGeschoben && TrumpfMode.SCHIEBE.equals(trumpf.getMode())) {
            logger.warn("{}: Trumpf {} can not be selected by strategy {}, as isGeschoben={}, choosing random, legal trumpf instead.", sessionInfo.getLocalPlayerName(), trumpf, strategy, isGeschoben);
            List<Trumpf> validTrumpfs = Arrays.asList(Trumpf.from(TrumpfMode.UNDEUFE), Trumpf.from(TrumpfMode.OBEABE), Trumpf.from(Suit.CLUBS), Trumpf.from(Suit.DIAMONDS), Trumpf.from(Suit.HEARTS), Trumpf.from(Suit.SPADES));
            int index = (new java.util.Random()).nextInt(validTrumpfs.size());
            trumpf = validTrumpfs.get(index);
        }

        return trumpf;
    }

    public Card requestCard(List<Card> cardsOnTable) {
        Preconditions.checkArgument(state.getCardsOnTable().equals(cardsOnTable));
        state.setCurrentPlayer(sessionInfo.getPlayerId());

        // safeguard against strategies repeatedly selecting invalid cards, thereby effectively blocking the game
        if (previousCardChoiceRejected) {
            logger.warn("{}: The card previously selected by strategy {} was rejected, choosing random card instead.", sessionInfo.getLocalPlayerName(), strategy);
            Set<Card> allowedCards = state.getAllowedCardsToPlay();
            int index = new java.util.Random().nextInt(allowedCards.size());
            previousCardChoiceRejected = false;
            return (new ArrayList<>(allowedCards)).get(index);
        }

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
        logger.warn("{}: Card {} rejected.", sessionInfo.getLocalPlayerName(), card);
        state.undoPlay(card);
        previousCardChoiceRejected = true;
    }

    public void cardsDealt(Collection<Card> cards) {
        state = new GameState(sessionInfo, cards);
    }

    public void stichMade(Stich stich) {
        logger.info("{}: {}", sessionInfo.getLocalPlayerName(), stich.toString());
        state.startNextRound(stich);
    }

    public String getRemotePlayerName() {
        return sessionInfo.getRemotePlayerName();
    }

    public String getLocalPlayerName() {
        return sessionInfo.getLocalPlayerName();
    }

    public SessionChoiceData getSessionChoiceData() {
        // see joinSession() above...
        sessionInfo.resetPlayerId();
        return new SessionChoiceData(sessionInfo.getSessionChoice(), sessionInfo.getSessionName(), sessionInfo.getSessionType());
    }

    public void log(String message) {
        logger.info("{}: {}", sessionInfo.getLocalPlayerName(), message);
    }
}
