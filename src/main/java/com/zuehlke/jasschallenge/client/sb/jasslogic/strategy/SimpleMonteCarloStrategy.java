package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy;

import com.google.common.base.Preconditions;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zuehlke.jasschallenge.client.sb.game.GameState;
import com.zuehlke.jasschallenge.client.sb.game.PlayerOrdering;
import com.zuehlke.jasschallenge.client.sb.game.SessionInfo;
import com.zuehlke.jasschallenge.client.sb.jasslogic.rules.AllowedCardsRules;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.common.CardDistribution;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.common.PointsCounter;
import com.zuehlke.jasschallenge.client.sb.model.Stich;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Predicate;

public class SimpleMonteCarloStrategy implements Strategy {
    private final int NUMBER_OF_CARD_DISTRIBUTIONS_FOR_TRUMPF_REQUEST;
    private final int NUMBER_OF_EVALUATIONS_PER_CARD_DISTRIBUTION_FOR_TRUMPF_REQUEST;
    private final int MAX_SIMULATION_DEPTH_FOR_TRUMPF_REQUEST;

    private final int NUMBER_OF_CARD_DISTRIBUTIONS_FOR_CARD_REQUEST;
    private final int NUMBER_OF_EVALUATIONS_PER_CARD_DISTRIBUTION_FOR_CARD_REQUEST;
    private final int MAX_SIMULATION_DEPTH_FOR_CARD_REQUEST;

    private static final Logger logger = LogManager.getLogger(SimpleMonteCarloStrategy.class);

    private PlayerOrdering order;
    private int myId;
    private int partnerId;

    public SimpleMonteCarloStrategy() {
        this(ConfigFactory.load());
    }

    public SimpleMonteCarloStrategy(Config conf) {
        Config config = ConfigFactory.defaultOverrides()
                .withFallback(conf)
                .withFallback(ConfigFactory.load())
                .getConfig("simple-monte-carlo-strategy");

        this.NUMBER_OF_CARD_DISTRIBUTIONS_FOR_TRUMPF_REQUEST = config.getInt("NUMBER_OF_CARD_DISTRIBUTIONS_FOR_TRUMPF_REQUEST");
        this.NUMBER_OF_EVALUATIONS_PER_CARD_DISTRIBUTION_FOR_TRUMPF_REQUEST = config.getInt("NUMBER_OF_EVALUATIONS_PER_CARD_DISTRIBUTION_FOR_TRUMPF_REQUEST");
        this.MAX_SIMULATION_DEPTH_FOR_TRUMPF_REQUEST = config.getInt("MAX_SIMULATION_DEPTH_FOR_TRUMPF_REQUEST");

        this.NUMBER_OF_CARD_DISTRIBUTIONS_FOR_CARD_REQUEST = config.getInt("NUMBER_OF_CARD_DISTRIBUTIONS_FOR_CARD_REQUEST");
        this.NUMBER_OF_EVALUATIONS_PER_CARD_DISTRIBUTION_FOR_CARD_REQUEST = config.getInt("NUMBER_OF_EVALUATIONS_PER_CARD_DISTRIBUTION_FOR_CARD_REQUEST");
        this.MAX_SIMULATION_DEPTH_FOR_CARD_REQUEST = config.getInt("MAX_SIMULATION_DEPTH_FOR_CARD_REQUEST");
    }

    @Override
    public void onSessionStarted(SessionInfo sessionInfo) {
        this.myId = sessionInfo.getPlayerId();
        this.order = sessionInfo.getPlayerOrdering();
        this.partnerId = sessionInfo.getPartnerId();
    }


    @Override
    public Trumpf onRequestTrumpf(GameState state, boolean isGeschoben) {
        Set<Card> myCards = state.getMyCards();

        Set<Trumpf> trumpfs = new HashSet<>();
        for (Suit suit: Suit.values()) {
            trumpfs.add(new TrumpfSuit(suit));
        }
        trumpfs.add(new TrumpfObeabe());
        trumpfs.add(new TrumpfUndeufe());
        // TODO: also handle TrumpfMode SCHIEBE
        /*if (!isGeschoben) {
            trumpfs.add(new TrumpfSchiebe());
        }*/

        Map<Trumpf, PointsCounter> evaluation = new HashMap<>();
        for (Trumpf trumpf: trumpfs) {
            evaluation.put(trumpf, new PointsCounter());
            for (int i = 0; i < NUMBER_OF_CARD_DISTRIBUTIONS_FOR_TRUMPF_REQUEST; i++) {
                CardDistribution cardDistribution = distributeCards(myCards, new ArrayList<>(), EnumSet.noneOf(Card.class), myId);
                for (int j = 0; j < NUMBER_OF_EVALUATIONS_PER_CARD_DISTRIBUTION_FOR_TRUMPF_REQUEST; j++) {
                    CardDistribution cards = new CardDistribution(cardDistribution);
                    int firstPlayer = myId;
                    /*if (isGeschoben) {
                        firstPlayer = partnerId;
                    }*/
                    evaluation.get(trumpf).add(simulateGame(cards, trumpf, new ArrayList<>(), firstPlayer, MAX_SIMULATION_DEPTH_FOR_TRUMPF_REQUEST));
                }
            }
        }


        int maxPoints = evaluation.values().stream().mapToInt(PointsCounter::getOurPoints).max().getAsInt();
        Predicate<Trumpf> isMaxValueTrumpf = trumpf -> evaluation.get(trumpf).getOurPoints() == maxPoints;
        Comparator<Trumpf> compareTheirPoints = (trumpf1, trumpf2) -> Integer.valueOf(evaluation.get(trumpf1).getTheirPoints()).compareTo(evaluation.get(trumpf2).getTheirPoints());
        Trumpf chosenTrumpf = evaluation.keySet().stream()
                .filter(isMaxValueTrumpf)
                .min(compareTheirPoints).get();

        return chosenTrumpf;
    }

    @Override
    public Card onRequestCard(GameState state) {
        Preconditions.checkArgument(state.getCurrentPlayer() == myId);

        Map<Card, PointsCounter> evaluation = new HashMap<>();

        Set<Card> allowedCards = state.getAllowedCardsToPlay();
        Set<Card> myCards = state.getMyCards();
        List<Card> cardsOnTable = state.getCardsOnTable();
        Set<Card> playedCards = state.getPlayedCards();
        Trumpf trumpf = state.getTrumpf();

        for (Card card: allowedCards) {
            evaluation.put(card, new PointsCounter());
            for (int i = 0; i < NUMBER_OF_CARD_DISTRIBUTIONS_FOR_CARD_REQUEST; i++) {
                CardDistribution cardDistribution = distributeCards(myCards, cardsOnTable, playedCards, myId);
                for (int j = 0; j < NUMBER_OF_EVALUATIONS_PER_CARD_DISTRIBUTION_FOR_CARD_REQUEST; j++) {
                    CardDistribution cards = new CardDistribution(cardDistribution);
                    cards.remove(myId, card);
                    List<Card> newCardsOnTable = new ArrayList<>(state.getCardsOnTable());
                    newCardsOnTable.add(card);
                    int nextPlayer = order.getNextPlayerIdFrom(myId);
                    evaluation.get(card).add(simulateGame(cards, trumpf, newCardsOnTable, nextPlayer, MAX_SIMULATION_DEPTH_FOR_CARD_REQUEST));
                }
            }
        }

        int maxPoints = evaluation.values().stream().mapToInt(PointsCounter::getOurPoints).max().getAsInt();
        Predicate<Map.Entry<Card, PointsCounter>> isMaxValueCard = entry -> entry.getValue().getOurPoints() == maxPoints;
        Comparator<Card> compareTheirPoints = (card1, card2) -> Integer.valueOf(evaluation.get(card1).getTheirPoints()).compareTo(evaluation.get(card2).getTheirPoints());
        Card chosenCard = evaluation.entrySet().stream()
                .filter(isMaxValueCard)
                .map(Map.Entry::getKey)
                .min(compareTheirPoints)
                .get();
        return chosenCard;
    }

    private CardDistribution distributeCards(Set<Card> playerCards, List<Card> cardsOnTable, Set<Card> playedCards, int firstPlayerId) {
        EnumSet<Card> playedCardsEnumSet;
        if (playedCards.isEmpty()) {
            playedCardsEnumSet = EnumSet.noneOf(Card.class);
        } else {
            playedCardsEnumSet = EnumSet.copyOf(playedCards);
        }
        EnumSet<Card> otherCardsSet =  EnumSet.complementOf(EnumSet.copyOf(playerCards));
        otherCardsSet.removeAll(playedCardsEnumSet);

        LinkedList<Card> myCards = new LinkedList<>(playerCards);
        LinkedList<Card> otherCards = new LinkedList<>(otherCardsSet);
        Collections.shuffle(otherCards);

        Preconditions.checkState(myCards.size() + otherCards.size() + playedCards.size() == Card.values().length);

        CardDistribution cards = new CardDistribution(order.getPlayerIdsInOrder());

        int remainingCardsInRound = Stich.STICH_SIZE - cardsOnTable.size();
        int currentPlayer = firstPlayerId;
        while (!myCards.isEmpty()) {
            for (int i=0; i < remainingCardsInRound; i++) {
                Card card;
                if (currentPlayer == myId) {
                    card = myCards.removeFirst();

                } else {
                    card = otherCards.removeFirst();
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

    private PointsCounter simulateGame(CardDistribution cards, Trumpf trumpf, List<Card> cardsOnTable, int firstPlayerId, int maxSimulationDepth) {
        int currentPlayer = firstPlayerId;
        PointsCounter evaluation = new PointsCounter();

        int iteration = 0;

        while(!cards.isEmpty() && iteration < maxSimulationDepth) {
            // simulate one round
            while(cardsOnTable.size() < Stich.STICH_SIZE) {
                LinkedList<Card> allowedCards = new LinkedList<>(AllowedCardsRules.getFor(cards.get(currentPlayer), trumpf, cardsOnTable).get());
                Collections.shuffle(allowedCards);
                Card card = allowedCards.getFirst();

                cards.remove(currentPlayer, card);
                cardsOnTable.add(card);
                currentPlayer = order.getNextPlayerIdFrom(currentPlayer);
            }

            // find winner and award points
            Card winningCard = trumpf.getWinningCard(cardsOnTable);
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
            iteration++;
        }
        return evaluation;
    }

}
