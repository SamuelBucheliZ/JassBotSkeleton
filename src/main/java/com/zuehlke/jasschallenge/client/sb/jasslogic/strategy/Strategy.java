package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.game.GameState;
import com.zuehlke.jasschallenge.client.sb.game.SessionInfo;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;

import java.util.Set;

public interface Strategy {
    /* on RequestTrumpf */
    Trumpf onRequestTrumpf(GameState state, boolean isGeschoben);

    /* on RequestCard */
    Card onRequestCard(GameState state);

    /* on PlayedCards */
    default void onMoveMade(GameState state) {}

    /* on BroadcastTeam */
    default void onSessionStarted(SessionInfo sessionInfo) {}
    /* on BroadcastWinnerTeam */
    default void onSessionFinished(SessionInfo sessionInfo) {}

    /* on BroadcastTrumpf */
    default void onGameStarted(GameState state) {}
    /* on BroadcastGameFinished */
    default void onGameFinished(GameState state) {}

}
