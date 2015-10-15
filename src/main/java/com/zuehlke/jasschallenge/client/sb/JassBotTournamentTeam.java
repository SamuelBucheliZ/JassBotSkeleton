package com.zuehlke.jasschallenge.client.sb;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.MonteCarloStrategy;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.Strategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CountDownLatch;

public class JassBotTournamentTeam {
    private static final Logger logger = LogManager.getLogger(JassBotTournamentTeam.class);

    public static void main(String[] args) {
        logger.info("Starting JassBotTournamentTeam");

        Config config = ConfigFactory.defaultOverrides()
                .withFallback(ConfigFactory.load().getConfig("jass-bot-tournament-team"))
                .withFallback(ConfigFactory.load());

        int NUMBER_OF_BOTS_IN_TOURNAMENT = config.getInt("NUMBER_OF_BOTS_IN_TOURNAMENT");

        try {
            CountDownLatch countDown = new CountDownLatch(NUMBER_OF_BOTS_IN_TOURNAMENT);

            for(int i = 0; i < NUMBER_OF_BOTS_IN_TOURNAMENT; i++) {
                Strategy strategy = new MonteCarloStrategy();
                new JassBotPlayer(config, countDown, i, strategy);
            }

            countDown.await();
        } catch (InterruptedException e) {
            logger.error("JassBotApplication was interrupted.", e);
            e.printStackTrace();
        }
    }

}