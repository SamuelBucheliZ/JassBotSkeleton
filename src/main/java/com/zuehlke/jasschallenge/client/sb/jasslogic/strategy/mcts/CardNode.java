package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.mcts;

import com.zuehlke.jasschallenge.client.sb.game.GameState;
import com.zuehlke.jasschallenge.client.sb.game.SessionInfo;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.common.PointsCounter;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CardNode extends TreeNode<Card> {
    private static final Logger logger = LogManager.getLogger(CardNode.class);

    private TableState state;

    public CardNode(GameState state, SessionInfo sessionInfo) {
        this(new TableState(state, sessionInfo), null);
    }

    public CardNode(TableState state, TreeNode<?> parent) {
        super(parent, state.getPotentialCards());
        this.state = state;
    }

    @Override
    protected CardNode createChild(Card card) {
        TableState newState = state.playCard(card);
        return new CardNode(newState, this);
    }

    @Override
    public CardNode getOrCreateChild(Card card) {
        TreeNode<?> next = super.getOrCreateChild(card);
        return (CardNode)next;
    }

    @Override
    public boolean isTerminal() {
        return state.noCardsLeft();
    }

    @Override
    public PointsCounter rollOut() {
        return state.simulateGame();
    }

}
