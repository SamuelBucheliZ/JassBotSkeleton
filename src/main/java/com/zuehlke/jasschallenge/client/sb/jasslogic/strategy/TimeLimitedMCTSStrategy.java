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

public class TimeLimitedMCTSStrategy extends MCTSStrategy {
    private static final Logger logger = LogManager.getLogger(TimeLimitedMCTSStrategy.class);

    private final Duration TIME_BUDGET_TRUMPF_REQUEST;
    private final Duration TIME_BUDGET_CARD_REQUEST;

    private Optional<CardNode> root = Optional.empty();

    private SessionInfo sessionInfo;

    public TimeLimitedMCTSStrategy() {
        this(ConfigFactory.load());
    }

    public TimeLimitedMCTSStrategy(Config conf) {
        Config config = ConfigFactory.defaultOverrides()
                .withFallback(conf)
                .withFallback(ConfigFactory.load())
                .getConfig("time-limited-mcts-strategy");

        this.TIME_BUDGET_CARD_REQUEST = config.getDuration("TIME_BUDGET_CARD_REQUEST");
        this.TIME_BUDGET_TRUMPF_REQUEST = config.getDuration("TIME_BUDGET_TRUMPF_REQUEST");
    }

    @Override
    void searchTrumpf(TrumpfNode node) {
        searchFor(TIME_BUDGET_TRUMPF_REQUEST, node);
    }

    @Override
    void searchCard(CardNode node) {
        searchFor(TIME_BUDGET_CARD_REQUEST, node);
    }

    private void searchFor(Duration timeBudget, TreeNode node) {
        long startTime = System.nanoTime();

        // search should be executed at least once, else select is problematic
        do  {
            TreeNode.search(node);
        } while (System.nanoTime() - startTime < timeBudget.getNano());
    }

}

