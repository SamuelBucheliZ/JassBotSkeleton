package com.zuehlke.jasschallenge.client.sb.game;

import com.zuehlke.jasschallenge.client.sb.jasslogic.rules.MyCardsTestDataFactory;
import com.zuehlke.jasschallenge.client.sb.model.Stich;
import com.zuehlke.jasschallenge.client.sb.model.Team;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfSuit;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.zuehlke.jasschallenge.client.sb.model.cards.Card.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GameStateTest {
    private GameDataFactory gameDataFactory = new GameDataFactory();
    private MyCardsTestDataFactory myCardsTestDataFactory = new MyCardsTestDataFactory();

    @Test
    public void gameState_evolvesCorrectly() {
        SessionInfo sessionInfo = gameDataFactory.createSessionInfoForPlayerWithId3();
        List<Team> teamList = gameDataFactory.createTeamListWithPlayerOrder2031();
        sessionInfo.setPlayerOrderingAndPartnerId(teamList);
        Set<Card> myCards = myCardsTestDataFactory.createMyCardsWithOneHeart();
        GameState gameState = new GameState(sessionInfo, myCards);
        gameState.setTrumpf(new TrumpfSuit(Suit.HEARTS));

        // player2
        gameState.addToTable(HEART_NINE);
        // player0
        gameState.addToTable(HEART_EIGHT);

        // player3 --- "our" player
        gameState.setCurrentPlayer(sessionInfo.getPlayerId());
        assertThat(gameState.getCurrentPlayer(), is(sessionInfo.getPlayerId()));
        gameState.doPlay(HEART_JACK);
        gameState.addToTable(HEART_JACK);

        // player1
        gameState.addToTable(HEART_SEVEN);

        assertThat(gameState.getCardsOnTable(), is(Arrays.asList(HEART_NINE, HEART_EIGHT, HEART_JACK, HEART_SEVEN)));

        Stich stich = new Stich(sessionInfo.getPlayerName(), sessionInfo.getPlayerId(), Arrays.asList(HEART_NINE, HEART_EIGHT, HEART_JACK, HEART_SEVEN), teamList);

        gameState.startNextRound(stich);

        assertThat(gameState.getCurrentPlayer(), is(sessionInfo.getPlayerId()));
        assertThat(gameState.getRound(), is(1));
        assertThat(gameState.getCardsOnTable(), is(Collections.EMPTY_LIST));
    }
}
