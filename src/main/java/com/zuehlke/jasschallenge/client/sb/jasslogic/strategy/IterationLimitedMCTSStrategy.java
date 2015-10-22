package com.zuehlke.jasschallenge.client.sb.jasslogic.strategy;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zuehlke.jasschallenge.client.sb.game.GameState;
import com.zuehlke.jasschallenge.client.sb.game.SessionInfo;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.mcts.CardNode;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.mcts.TreeNode;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.mcts.TrumpfNode;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.Optional;

public class IterationLimitedMCTSStrategy extends MCTSStrategy {
    private static final Logger logger = LogManager.getLogger(IterationLimitedMCTSStrategy.class);

    private final int ITERATION_BUDGET_TRUMPF_REQUEST;
    private final int ITERATION_BUDGET_CARD_REQUEST;

    private Optional<CardNode> root = Optional.empty();

    private SessionInfo sessionInfo;

    public IterationLimitedMCTSStrategy() {
        this(ConfigFactory.load());
    }

    public IterationLimitedMCTSStrategy(Config conf) {
        Config config = ConfigFactory.defaultOverrides()
                .withFallback(conf)
                .withFallback(ConfigFactory.load())
                .getConfig("iteration-limited-mcts-strategy");

        this.ITERATION_BUDGET_CARD_REQUEST = config.getInt("ITERATION_BUDGET_CARD_REQUEST");
        this.ITERATION_BUDGET_TRUMPF_REQUEST = config.getInt("ITERATION_BUDGET_TRUMPF_REQUEST");
    }

    @Override
    void searchTrumpf(TrumpfNode node) {
        searchFor(ITERATION_BUDGET_TRUMPF_REQUEST, node);
    }

    @Override
    void searchCard(CardNode node) {
        searchFor(ITERATION_BUDGET_CARD_REQUEST, node);
    }

    private void searchFor(int iterationBudget, TreeNode node) {
        int i = 0;

        // search should be executed at least once, else select is problematic
        do  {
            TreeNode.search(node);
            i++;
        } while (i < iterationBudget);
    }

}

