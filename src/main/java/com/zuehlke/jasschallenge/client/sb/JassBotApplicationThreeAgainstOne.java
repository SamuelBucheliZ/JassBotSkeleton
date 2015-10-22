package com.zuehlke.jasschallenge.client.sb;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.Strategy;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.StrategyRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CountDownLatch;

/**
 * If you feel like playing against your own strategy...
 */
public class JassBotApplicationThreeAgainstOne {
    private static final int NUMBER_OF_BOT_PLAYERS = 3;

    private static final Logger logger = LogManager.getLogger(JassBotApplicationThreeAgainstOne.class);

    public static void main(String[] args) {
        logger.info("Starting JassBotApplication");

        Config config = ConfigFactory.load().getConfig("jass-bot-application-three-against-one");
        String strategyName = config.getString("STRATEGY");


        try {
            CountDownLatch countDown = new CountDownLatch(NUMBER_OF_BOT_PLAYERS);

            for(int i = 0; i < NUMBER_OF_BOT_PLAYERS; i++) {
                Strategy strategy = StrategyRepository.getStrategy(strategyName, config);
                new JassBotPlayer(countDown, i, strategy);
            }

            countDown.await();
        } catch (InterruptedException e) {
            logger.error("JassBotApplication was interrupted.", e);
        }
    }

}