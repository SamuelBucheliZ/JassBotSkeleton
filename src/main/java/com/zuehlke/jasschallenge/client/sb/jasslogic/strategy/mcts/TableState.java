package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.mcts;

import com.zuehlke.jasschallenge.client.sb.game.GameState;
import com.zuehlke.jasschallenge.client.sb.game.SessionInfo;
import com.zuehlke.jasschallenge.client.sb.jasslogic.rules.AllowedCardsRules;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.PointsCounter;
import com.zuehlke.jasschallenge.client.sb.model.Stich;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;

import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Set;

public class TableState {
    private final Trumpf trumpf;
    private final EnumSet<Card> myCards;
    private final EnumSet<Card> otherCards;
    private final LinkedList<Card> cardsOnTable;

    private final SessionInfo sessionInfo;
    private final int nextPlayerId;

    private final PointsCounter evaluation;

    /* Used for creation of game state in a running game, i.e., when trumpf was already chosen. */
    public TableState(GameState state, SessionInfo sessionInfo) {
        trumpf = state.getTrumpf();

        myCards = EnumSet.noneOf(Card.class);
        myCards.addAll(state.getMyCards());

        otherCards = EnumSet.allOf(Card.class);
        otherCards.removeAll(myCards);
        otherCards.removeAll(state.getPlayedCards());

        cardsOnTable = new LinkedList<>(state.getCardsOnTable());

        this.sessionInfo = sessionInfo;

        nextPlayerId = state.getCurrentPlayer();

        evaluation = new PointsCounter();
    }

    /* Used for creation of game state at the very beginning of a game (for evaluating a trumpf). */
    public TableState(Trumpf trumpf, EnumSet<Card> myCards, SessionInfo sessionInfo) {
        this(trumpf, EnumSet.copyOf(myCards), EnumSet.complementOf(myCards), new LinkedList<>(), sessionInfo.getPlayerId(), sessionInfo, new PointsCounter());
    }

    private TableState(TableState other) {
        this(other.trumpf, EnumSet.copyOf(other.myCards), EnumSet.copyOf(other.otherCards), new LinkedList<>(other.cardsOnTable), other.nextPlayerId, other.sessionInfo, other.evaluation.copy());
    }

    private TableState(Trumpf trumpf, EnumSet<Card> myCards, EnumSet<Card> otherCards, LinkedList<Card> cardsOnTable, int nextPlayerId, SessionInfo sessionInfo, PointsCounter evaluation) {
        this.trumpf = trumpf;
        this.myCards = myCards;
        this.otherCards = otherCards;
        this.cardsOnTable = cardsOnTable;
        this.nextPlayerId = nextPlayerId;
        this.sessionInfo = sessionInfo;
        this.evaluation = evaluation;
    }

    public TableState copy() {
        return new TableState(this);
    }

    public TableState playCard(Card card) {
        EnumSet<Card> newMyCards = EnumSet.copyOf(myCards);
        newMyCards.remove(card);
        EnumSet<Card> newOtherCards = EnumSet.copyOf(otherCards);
        newOtherCards.remove(card);

        LinkedList<Card> newCardsOnTable = new LinkedList<>(cardsOnTable);
        newCardsOnTable.add(card);

        int newNextPlayerId = sessionInfo.getNextPlayerIdFrom(nextPlayerId);

        PointsCounter newEvaluation = evaluation.copy();

        if (newCardsOnTable.size() == Stich.STICH_SIZE) {
            Card winningCard = trumpf.getWinningCard(newCardsOnTable);
            int winningCardIndex = newCardsOnTable.indexOf(winningCard);
            newNextPlayerId = sessionInfo.getPlayerOrdering().getPlayerIdFrom(nextPlayerId, winningCardIndex + 1);
            int winningPlayerId = sessionInfo.getPlayerOrdering().getPlayerIdFrom(newNextPlayerId, winningCardIndex);
            int points = trumpf.getValueOf(newCardsOnTable);
            if (newMyCards.isEmpty() && newOtherCards.isEmpty()) {
                points += Stich.LAST_STICH_POINTS;
            }
            if (winningPlayerId == sessionInfo.getPlayerId() || winningPlayerId == sessionInfo.getPartnerId()) {
                newEvaluation.addOurPoints(points);
            } else {
                newEvaluation.addTheirPoints(points);
            }
            newCardsOnTable.clear();
        }

        return new TableState(trumpf, newMyCards, newOtherCards, newCardsOnTable, newNextPlayerId, sessionInfo, newEvaluation);
    }

    public Set<Card> getPotentialCards() {
        if (sessionInfo.getPlayerId() == nextPlayerId) {
            // we can be precise for our own cards
            return AllowedCardsRules.getFor(myCards, trumpf, cardsOnTable).get();
        } else {
            // we use an overapproximation for the other cards
            return otherCards;
        }
    }


    public PointsCounter simulateGame() {
        final int myId = sessionInfo.getPlayerId();

        TableState state = this.copy();

        // we overapproximate by playing the other cards in random order, regardless of illegal moves
        LinkedList<Card> otherCards = new LinkedList<>(state.otherCards);
        Collections.shuffle(otherCards);

        while (!state.myCards.isEmpty() && !otherCards.isEmpty()) {
            Card card;
            if (state.nextPlayerId == myId) {
                LinkedList<Card> allowedCards = new LinkedList<>(state.getPotentialCards());
                int index = new java.util.Random().nextInt(allowedCards.size());
                card = allowedCards.get(index);
            } else {
                card = otherCards.removeFirst();
            }
            state = state.playCard(card);
        }

        return state.evaluation;
    }

    public boolean isOurTeamsTurnNext() {
        return sessionInfo.getPlayerId() == nextPlayerId || sessionInfo.getPartnerId() == nextPlayerId;
    }

    public boolean noCardsLeft() {
        return myCards.isEmpty() && otherCards.isEmpty();
    }

}
