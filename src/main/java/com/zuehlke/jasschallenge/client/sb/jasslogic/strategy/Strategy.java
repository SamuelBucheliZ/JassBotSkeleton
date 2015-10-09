package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy;

import com.zuehlke.jasschallenge.client.sb.game.GameState;
import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;

import java.util.Set;

public interface Strategy {
    /* on RequestTrumpf */
    Trumpf onRequestTrumpf(Set<Card> myCards);

    /* on RequestCard */
    Card onRequestCard(GameState gameState);

    /* on PlayedCards */
    default void onMoveMade() {}

    /* on BroadcastTeam */
    default void onSessionStarted() {}
    /* on BroadcastWinnerTeam */
    default void onSessionFinished() {}

    /* on BroadcastTrumpf */
    default void onGameStarted() {}
    /* on BroadcastGameFinished */
    default void onGameFinished() {}


}
