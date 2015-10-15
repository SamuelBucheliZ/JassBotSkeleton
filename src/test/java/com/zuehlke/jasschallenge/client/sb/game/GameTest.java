package com.zuehlke.jasschallenge.client.sb.game;

import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.Strategy;
import com.zuehlke.jasschallenge.client.sb.model.Player;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfMode;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfObeabe;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfSchiebe;
import com.zuehlke.jasschallenge.client.sb.socket.responses.SessionChoice;
import com.zuehlke.jasschallenge.client.sb.socket.responses.SessionType;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {
    GameDataFactory gameDataFactory = new GameDataFactory();

    @Test
    public void game_doesNotAllow_illegalTrumpfChoice() {
        Strategy badStrategy = mock(Strategy.class);
        when(badStrategy.onRequestTrumpf(anySetOf(Card.class), eq(true))).thenReturn(new TrumpfSchiebe());

        Game game = new Game("player2", "player2", "session2", SessionChoice.AUTOJOIN, SessionType.SINGLE_GAME, badStrategy);
        game.joinSession(new Player(2, "player2"));
        game.startSession(gameDataFactory.createTeamListWithPlayerOrder2031());
        game.cardsDealt(Collections.emptySet());

        boolean isGeschoben = true;
        Trumpf trumpf = game.requestTrumpf(isGeschoben);

        assertThat(trumpf.getMode(), is(not(TrumpfMode.SCHIEBE)));
    }

    @Test
    public void game_doesNotAllow_illegalCardChoice() {
        Strategy badStrategy = mock(Strategy.class);
        when(badStrategy.onRequestCard(any(GameState.class))).thenReturn(Card.CLUB_ACE);

        Game game = new Game("player0", "player0", "session1", SessionChoice.AUTOJOIN, SessionType.SINGLE_GAME, badStrategy);
        game.joinSession(new Player(0, "player0"));
        game.startSession(gameDataFactory.createTeamListWithPlayerOrder2031());
        game.cardsDealt(Arrays.asList(Card.CLUB_ACE, Card.DIAMOND_JACK));
        game.startGame(new TrumpfObeabe());
        List<Card> cardsOnTable = Arrays.asList(Card.DIAMOND_NINE);
        game.cardsPlayed(cardsOnTable);

        Card badCard = game.requestCard(cardsOnTable);

        game.cardRejected(badCard);

        Card tryAgainCard = game.requestCard(cardsOnTable);

        assertThat(tryAgainCard, is(not(badCard)));
    }


}
