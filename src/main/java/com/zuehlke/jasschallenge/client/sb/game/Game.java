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
    public static final int NUMBER_OF_ROUNDS = 9;

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
        /*
         * The first SessionJoined message a player receives is guaranteed to contain the player's own id.
         * Note that player ids getOrCreateChild reassigned in every new session in tournament mode, thus, the player
         * id needs to be reset at finish session.
         */
        if (!sessionInfo.playerIdIsPresent()) {
            sessionInfo.setPlayerId(player.getId());
            logger.info("{}: I, player {}, joined session {} with id {}.", sessionInfo.getLocalPlayerName(), player, sessionInfo.getSessionName(), player.getId());
        } else {
            logger.info("{}: Player {} joined session {} with id {}.", sessionInfo.getLocalPlayerName(), player, sessionInfo.getSessionName(), player.getId());
        }
    }

    public void startSession(List<Team> teams) {
        this.sessionInfo.setPlayerOrderingAndPartnerId(teams);
        try {
            strategy.onSessionStarted(sessionInfo);
        } catch (Exception e) {
            logger.error(String.format("%s: Strategy %s failed in startSession.", sessionInfo.getLocalPlayerName(), strategy.getClass().getSimpleName()),e);
        }

        logger.debug("{}: Received team information, my id is {}, my partner's id is {}.", sessionInfo.getLocalPlayerName(), sessionInfo.getPlayerId(), sessionInfo.getPartnerId());
        logger.debug("{}: Started session with teams {}.", sessionInfo.getLocalPlayerName(), teams);
    }

    public void finishSession(PointsInformation winningTeamPointsInformation) {
        boolean iWon = winningTeamPointsInformation.getTeamName().equals(sessionInfo.getMyTeam().getTeamName());
        String myOrTheOther = iWon ? "my" : "the other";
        logger.info("{}: Session finished, {} team {} won with {} points. I used strategy {}.", sessionInfo.getLocalPlayerName(), myOrTheOther, winningTeamPointsInformation.getTeamName(), winningTeamPointsInformation.getPoints(), strategy.getClass().getSimpleName());
        try {
            strategy.onSessionFinished(sessionInfo);
        } catch (Exception e) {
            logger.error(String.format("%s: Strategy %s failed in finishSession.", sessionInfo.getLocalPlayerName(), strategy.getClass().getSimpleName()), e);
        }
        // remember that new IDs are issued at the beginning of every new session in tournament mode, see also joinSession() and startSession().
        sessionInfo.resetPlayerId();
        sessionInfo.resetPlayerOrderingAndPartnerId();
    }

    public void startGame(Trumpf trumpf) {
        state.setTrumpf(trumpf);
        try {
            strategy.onGameStarted(state);
        } catch (Exception e) {
            logger.error(String.format("%s: Strategy %s failed in startGame.", sessionInfo.getLocalPlayerName(), strategy.getClass().getSimpleName()), e);
        }
        logger.trace("{}: Started game with trumpf {}.", sessionInfo.getLocalPlayerName(), trumpf);
    }

    public void finishGame(List<PointsInformation> pointsInformation) {
        logger.info("{}: Game finished, with {} vs. {}. ", sessionInfo.getLocalPlayerName(), pointsInformation.get(0), pointsInformation.get(1));
        try {
            strategy.onGameFinished(state);
        } catch (Exception e) {
            logger.error(String.format("%s: Strategy %s failed in finishGame.", sessionInfo.getLocalPlayerName(), strategy.getClass().getSimpleName()), e);
        }
    }

    public Trumpf requestTrumpf(boolean isGeschoben) {
        if (!isGeschoben) {
            state.setCurrentPlayer(sessionInfo.getPlayerId());
        } else {
            state.setCurrentPlayer(sessionInfo.getPartnerId());
        }
        logger.debug("{}: Received trumpf request, setting current player to {}.", sessionInfo.getLocalPlayerName(), state.getCurrentPlayer());

        Trumpf trumpf;
        try {
            long startTime = System.nanoTime();
            trumpf = strategy.onRequestTrumpf(state, isGeschoben);
            long endTime = System.nanoTime();
            logger.debug("{}: Choosing trumpf {} took {} milliseconds with strategy {}.", sessionInfo.getLocalPlayerName(), trumpf, (endTime - startTime) / 1000000);
            Preconditions.checkState(!isGeschoben || !TrumpfMode.SCHIEBE.equals(trumpf.getMode()), String.format("%s: Trumpf %s can not be selected by strategy %s, as isGeschoben=%b.", sessionInfo.getLocalPlayerName(), trumpf, strategy, isGeschoben));
        } catch (Exception e) {
            logger.error(String.format("%s: Strategy %s failed in onRequestTrumpf, choosing random trumpf instead.", sessionInfo.getLocalPlayerName(), strategy.getClass().getSimpleName()), e);
            trumpf = chooseRandom(Trumpf.getAllTrumpfsWithoutSchiebe());
        }

        logger.info("{}: Choosing trumpf {} with cards {} using {}.", sessionInfo.getLocalPlayerName(), trumpf, state.getMyCards(), strategy.getClass().getSimpleName());

        return trumpf;
    }

    private <T> T chooseRandom(Collection<T> options) {
        int index = new java.util.Random().nextInt(options.size());
        T choice = (new ArrayList<>(options)).get(index);
        return choice;
    }

    public Card requestCard(List<Card> cardsOnTable) {
        Preconditions.checkArgument(state.getCardsOnTable().equals(cardsOnTable));

        state.setCurrentPlayer(sessionInfo.getPlayerId());
        logger.debug("{}: Received card request, setting current player to {}.", sessionInfo.getLocalPlayerName(), state.getCurrentPlayer());

        Card card;
        try {
            // safeguard against strategies repeatedly selecting invalid cards, thereby effectively blocking the game
            Preconditions.checkState(!previousCardChoiceRejected, String.format("%s: The card previously selected by strategy %s was rejected, choosing random card instead.", sessionInfo.getLocalPlayerName(), strategy.getClass().getSimpleName()));
            long startTime = System.nanoTime();
            card = strategy.onRequestCard(state);
            long endTime = System.nanoTime();
            logger.debug("{}: Choosing card {} took {} milliseconds.", sessionInfo.getLocalPlayerName(), card, (endTime - startTime) / 1000000);
            state.doPlay(card);
        } catch (Exception e) {
            logger.error(String.format("%s: Strategy failed when choosing card, choosing random card instead.", sessionInfo.getLocalPlayerName()), e);
            card = chooseRandom(state.getAllowedCardsToPlay());
            previousCardChoiceRejected = false;
            state.doPlay(card);
        }

        logger.info("{}: Choosing card {} with remaining cards {} using strategy {}.", sessionInfo.getLocalPlayerName(), card, state.getMyCards(), strategy.getClass().getSimpleName());

        return card;
    }

    public void cardsPlayed(List<Card> playedCards) {
        int indexOfLastElement = playedCards.size() - 1;
        state.addToTable(playedCards.get(indexOfLastElement));
        try {
            strategy.onMoveMade(state);
        } catch (Exception e) {
            logger.error(String.format("%s: Strategy failed in onMoveMade.", sessionInfo.getLocalPlayerName()), e);
        }

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
        if (stich.getPlayerId() == this.sessionInfo.getPlayerId()) {
            logger.info("{}: {}", sessionInfo.getLocalPlayerName(), stich.toString());
        }
        state.startNextRound(stich);
    }

    public String getRemotePlayerName() {
        return sessionInfo.getRemotePlayerName();
    }

    public String getLocalPlayerName() {
        return sessionInfo.getLocalPlayerName();
    }

    public int getPlayerId() {
        return sessionInfo.getPlayerId();
    }

    public int getPartnerId() {
        return  sessionInfo.getPartnerId();
    }

    public SessionChoiceData getSessionChoiceData() {
        return new SessionChoiceData(sessionInfo.getSessionChoice(), sessionInfo.getSessionName(), sessionInfo.getSessionType());
    }

    public void log(String message) {
        logger.trace("{}: {}", sessionInfo.getLocalPlayerName(), message);
    }

}
