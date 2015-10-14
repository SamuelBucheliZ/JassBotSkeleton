package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy;

import com.google.common.base.Preconditions;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zuehlke.jasschallenge.client.sb.game.GameState;
import com.zuehlke.jasschallenge.client.sb.game.PlayerOrdering;
import com.zuehlke.jasschallenge.client.sb.game.SessionInfo;
import com.zuehlke.jasschallenge.client.sb.jasslogic.rules.AllowedCardsRules;
import com.zuehlke.jasschallenge.client.sb.model.Stich;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class MonteCarloStrategy implements Strategy {
    private static final Config conf = ConfigFactory.load().getConfig("monte-carlo-strategy");
    private static final int NUMBER_OF_CARD_DISTRIBUTIONS = conf.getInt("NUMBER_OF_CARD_DISTRIBUTIONS");
    private static final int NUMBER_OF_EVALUATIONS_PER_CARD_DISTRIBUTION = conf.getInt("NUMBER_OF_EVALUATIONS_PER_CARD_DISTRIBUTION");

    private static final Logger logger = LogManager.getLogger(MonteCarloStrategy.class);

    private Random rand = new Random();
    private PlayerOrdering order;
    private int myId;
    private int partnerId;

    @Override
    public void onSessionStarted(SessionInfo sessionInfo) {
        this.myId = sessionInfo.getPlayerId();
        this.partnerId = sessionInfo.getPartnerId();
        this.order = sessionInfo.getPlayerOrdering();
    }

    @Override
    public Trumpf onRequestTrumpf(Set<Card> myCards, boolean isGeschoben) {
        Set<Trumpf> trumpfs = new HashSet<>();
        for (Suit suit: Suit.values()) {
            trumpfs.add(new TrumpfSuit(suit));
        }
        trumpfs.add(new TrumpfObeabe());
        trumpfs.add(new TrumpfUndeufe());
        // TODO: also evaluate TrumpfMode SCHIEBE
        /*if (!isGeschoben) {
            trumpfs.add(new TrumpfSchiebe());
        }*/
        Map<Trumpf, CardEvaluation> evaluation = new HashMap<>();
        for (Trumpf trumpf: trumpfs) {
            evaluation.put(trumpf, new CardEvaluation());
            for (int i = 0; i < NUMBER_OF_CARD_DISTRIBUTIONS; i++) {
                CardDistribution cardDistribution = distributeCards(myCards, new ArrayList<>(), EnumSet.noneOf(Card.class), myId);
                for (int j = 0; j < NUMBER_OF_EVALUATIONS_PER_CARD_DISTRIBUTION; j++) {
                    CardDistribution cards = new CardDistribution(cardDistribution);
                    int firstPlayer = myId;
                    if (isGeschoben) {
                        firstPlayer = partnerId;
                    }
                    evaluation.get(trumpf).add(evaluateCardChoice(cards, trumpf, new ArrayList<>(), firstPlayer));
                }
            }
        }

        int maxPoints = evaluation.values().stream().mapToInt(CardEvaluation::getOurPoints).max().getAsInt();
        Trumpf chosenTrumpf = evaluation.keySet().stream()
                .filter(trumpf -> evaluation.get(trumpf).getOurPoints() == maxPoints)
                .min((trumpf1, trumpf2) -> Integer.valueOf(evaluation.get(trumpf1).getTheirPoints()).compareTo(evaluation.get(trumpf2).getTheirPoints())).get();
        return chosenTrumpf;
    }

    private CardDistribution distributeCards(Set<Card> playerCards, List<Card> cardsOnTable, EnumSet<Card> playedCards, int firstPlayerId) {
        EnumSet<Card> myCards = EnumSet.copyOf(playerCards);
        EnumSet<Card> otherCards =  EnumSet.complementOf(EnumSet.copyOf(myCards));
        otherCards.removeAll(playedCards);

        Preconditions.checkState(myCards.size() + otherCards.size() + playedCards.size() == Card.values().length);

        CardDistribution cards = new CardDistribution(order.getPlayerIdsInOrder());

        int remainingCardsInRound = Stich.STICH_SIZE - cardsOnTable.size();
        int currentPlayer = firstPlayerId;
        while (!myCards.isEmpty()) {
            for (int i=0; i < remainingCardsInRound; i++) {
                Card card;
                if (currentPlayer == myId) {
                    card = myCards.iterator().next();
                    myCards.remove(card);
                } else {
                    card = selectAndRemoveRandomCard(otherCards);
                }
                cards.add(currentPlayer, card);
                currentPlayer = order.getNextPlayerIdFrom(currentPlayer);
            }
            remainingCardsInRound = Stich.STICH_SIZE;
        }

        Preconditions.checkState(myCards.isEmpty());
        Preconditions.checkState(otherCards.isEmpty());

        return cards;
    }

    private static class CardEvaluation {
        private int ourPoints = 0;
        private int theirPoints = 0;

        public void addOurPoints(int ourPoints) {
            this.ourPoints += ourPoints;
        }

        public void addTheirPoints(int theirPoints) {
            this.theirPoints += theirPoints;
        }

        public void add(CardEvaluation that) {
            this.ourPoints += that.ourPoints;
            this.theirPoints += that.theirPoints;
        }

        public int getOurPoints() {
            return ourPoints;
        }

        public int getTheirPoints() {
            return theirPoints;
        }
    }

    public CardEvaluation evaluateCardChoice(CardDistribution cards, Trumpf trumpf, List<Card> cardsOnTable, int firstPlayerId) {
        int currentPlayer = firstPlayerId;
        CardEvaluation evaluation = new CardEvaluation();

        while(!cards.isEmpty()) {
            // simulate one round
            while(cardsOnTable.size() < Stich.STICH_SIZE) {
                EnumSet<Card> allowedCards = EnumSet.copyOf(AllowedCardsRules.getFor(cards.get(currentPlayer), trumpf, cardsOnTable).get());

                Card card = selectRandomCard(allowedCards);
                cards.get(currentPlayer).remove(card);
                cardsOnTable.add(card);
                currentPlayer = order.getNextPlayerIdFrom(currentPlayer);
            }

            // find winner and award points
            Card winningCard = cardsOnTable.stream().max(trumpf.getComparator()).get();
            int winningCardIndex = cardsOnTable.indexOf(winningCard);
            int winningPlayerId = order.getPlayerIdFrom(currentPlayer, winningCardIndex);
            int points = trumpf.getValueOf(cardsOnTable);
            if (winningPlayerId == myId || winningPlayerId == partnerId) {
                evaluation.addOurPoints(points);
            } else {
                evaluation.addTheirPoints(points);
            }

            // start next round
            currentPlayer = winningPlayerId;
            cardsOnTable.clear();
        }
        return evaluation;
    }

    @Override
    public Card onRequestCard(GameState state) {

        Preconditions.checkArgument(state.getCurrentPlayer() == myId);

        Map<Card, CardEvaluation> evaluation = new HashMap<>();

        Set<Card> allowedCards = state.getAllowedCardsToPlay();
        Set<Card> playedCards = state.getPlayedCards();
        EnumSet<Card> playedCardsEnumSet;
        if (playedCards.isEmpty()) {
            playedCardsEnumSet = EnumSet.noneOf(Card.class);
        } else {
            playedCardsEnumSet = EnumSet.copyOf(playedCards);
        }


        for (Card card: allowedCards) {
            evaluation.put(card, new CardEvaluation());
            for (int i = 0; i < NUMBER_OF_CARD_DISTRIBUTIONS; i++) {
                CardDistribution cardDistribution = distributeCards(state.getMyCards(), state.getCardsOnTable(), playedCardsEnumSet, state.getCurrentPlayer());
                for (int j = 0; j < NUMBER_OF_EVALUATIONS_PER_CARD_DISTRIBUTION; j++) {
                    CardDistribution cards = new CardDistribution(cardDistribution);
                    cards.get(myId).remove(card);
                    List<Card> cardsOnTable = new ArrayList<>(state.getCardsOnTable());
                    cardsOnTable.add(card);
                    int nextPlayer = order.getNextPlayerIdFrom(myId);
                    evaluation.get(card).add(evaluateCardChoice(cards, state.getTrumpf(), cardsOnTable, nextPlayer));
                }
            }
        }

        int maxPoints = evaluation.values().stream().mapToInt(CardEvaluation::getOurPoints).max().getAsInt();
        Card chosenCard = evaluation.entrySet().stream().filter(entry -> entry.getValue().getOurPoints() == maxPoints).map(Map.Entry::getKey)
                .min((trumpf1, trumpf2) -> Integer.valueOf(evaluation.get(trumpf1).getTheirPoints()).compareTo(evaluation.get(trumpf2).getTheirPoints())).get();

        return chosenCard;
    }

    private Card selectRandomCard(EnumSet<Card> cards) {
        int index = rand.nextInt(cards.size());
        Card card = new ArrayList<>(cards).get(index);
        return card;
    }

    private Card selectAndRemoveRandomCard(EnumSet<Card> cards) {
        // TODO: Any elegant way for random selection from enum set?
        Card card = selectRandomCard(cards);
        cards.remove(card);
        return card;
    }

}
