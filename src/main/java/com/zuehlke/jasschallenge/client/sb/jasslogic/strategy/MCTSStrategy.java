package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy;

import com.zuehlke.jasschallenge.client.sb.game.GameState;
import com.zuehlke.jasschallenge.client.sb.game.SessionInfo;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.mcts.CardNode;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.mcts.TrumpfNode;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

/**
 * Monte Carlo Tree Search Strategy
 * See:
 * Browne, C.B.; Powley, E.; Whitehouse, D.; Lucas, S.M.; Cowling, P.I.; Rohlfshagen, P.; Tavener, S.; Perez, D.; Samothrakis, S.; Colton, S.,
 * "A Survey of Monte Carlo Tree Search Methods," in Computational Intelligence and AI in Games, IEEE Transactions on , vol.4, no.1, pp.1-43, March 2012
 * doi: 10.1109/TCIAIG.2012.2186810
 * URL: http://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=6145622&isnumber=6169178
 * Available at http://pubs.doc.ic.ac.uk/survey-mcts-methods/survey-mcts-methods.pdf
 *
 * Concrete implementations come in two variants, allowing to limit the time spent for search or allowing to limit the number of iterations used for search.
 *
 */
public abstract class MCTSStrategy implements Strategy {
    private static final Logger logger = LogManager.getLogger(MCTSStrategy.class);

    private Optional<CardNode> root = Optional.empty();
    private SessionInfo sessionInfo;

    abstract void searchTrumpf(TrumpfNode node);
    abstract void searchCard(CardNode node);

    @Override
    public void onSessionStarted(SessionInfo sessionInfo) {
        this.sessionInfo = sessionInfo;
    }

    @Override
    public void onGameFinished(GameState state) {
        this.root = Optional.empty();
    }

    @Override
    public void onMoveMade(GameState state) {
        if (root.isPresent()) {
            Card card = state.getLastMove().getCard();
            CardNode newRoot = root.get().getOrCreateChild(card);
            newRoot.makeRoot();
            root = Optional.of(newRoot);
        }
    }

    @Override
    public Trumpf onRequestTrumpf(GameState state, boolean isGeschoben) {

        TrumpfNode trumpfRoot = new TrumpfNode(state, sessionInfo);

        searchTrumpf(trumpfRoot);

        Trumpf trumpf = trumpfRoot.selectBestMove();

        CardNode newRoot = trumpfRoot.getOrCreateChild(trumpf);
        newRoot.makeRoot();

        root = Optional.of(newRoot);

        return trumpf;
    }

    @Override
    public Card onRequestCard(GameState state) {
        if (state.isLastRound()) {
            return state.getMyCards().stream().findAny().get();
        }

        if (!root.isPresent()) {
            root = Optional.of(new CardNode(state, sessionInfo));
        }

        searchCard(root.get());

        return root.get().selectBestMove();
    }
}

