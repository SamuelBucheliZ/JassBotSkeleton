package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.mcts;

import com.zuehlke.jasschallenge.client.sb.game.GameState;
import com.zuehlke.jasschallenge.client.sb.game.SessionInfo;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.PointsCounter;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfObeabe;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfSuit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfUndeufe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.EnumSet;

public class TrumpfNode extends TreeNode<Trumpf> {
    private EnumSet<Card> myCards;
    private SessionInfo sessionInfo;

    public TrumpfNode(GameState state, SessionInfo sessionInfo) {
        super(null, Arrays.asList(new TrumpfObeabe(), new TrumpfUndeufe(), new TrumpfSuit(Suit.CLUBS), new TrumpfSuit(Suit.DIAMONDS), new TrumpfSuit(Suit.HEARTS), new TrumpfSuit(Suit.SPADES)));
        this.myCards = EnumSet.copyOf(state.getMyCards());
        this.sessionInfo = sessionInfo;
    }

    @Override
    protected CardNode createChild(Trumpf trumpf) {
        TableState state = new TableState(trumpf, myCards, sessionInfo);
        return new CardNode(state, this);
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public PointsCounter rollOut() {
        return new PointsCounter();
    }

}
