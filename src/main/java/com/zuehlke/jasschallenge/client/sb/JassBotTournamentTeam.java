package com.zuehlke.jasschallenge.client.sb;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.MonteCarloStrategy;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.Strategy;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.StrategyRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CountDownLatch;

// a team consisting of two bots with the same name for the game night hacked challenge
public class JassBotTournamentTeam {
    private static final Logger logger = LogManager.getLogger(JassBotTournamentTeam.class);

    public static void main(String[] args) {
        logger.info("Starting JassBotTournamentTeam");

        // override default configuration with custom configuration
        Config config = ConfigFactory.defaultOverrides()
                .withFallback(ConfigFactory.load().getConfig("jass-bot-tournament-team"))
                .withFallback(ConfigFactory.load());

        int NUMBER_OF_BOTS_IN_TOURNAMENT = config.getInt("NUMBER_OF_BOTS_IN_TOURNAMENT");
        String STRATEGY = config.getString("STRATEGY");

        try {
            CountDownLatch countDown = new CountDownLatch(NUMBER_OF_BOTS_IN_TOURNAMENT);

            for(int i = 0; i < NUMBER_OF_BOTS_IN_TOURNAMENT; i++) {
                Strategy strategy = StrategyRepository.getStrategy(STRATEGY);
                new JassBotPlayer(config, countDown, i, strategy);
            }

            countDown.await();
        } catch (InterruptedException e) {
            logger.error("JassBotApplication was interrupted.", e);
            e.printStackTrace();
        }
    }

}