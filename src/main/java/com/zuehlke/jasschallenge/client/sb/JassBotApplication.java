package com.zuehlke.jasschallenge.client.sb;

import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.MonteCarloStrategy;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.RandomStrategy;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.Strategy;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.spi.LoggerContext;

/**
 * This is a Jass (swiss card game) bot implementation that plays the game
 * using an instance of the https://github.com/webplatformz/challenge Jass server.
 *
 * @author Adrian Herzog (https://github.com/adiherzog)
 * @author Samuel Bucheli (https://github.com/SamuelBucheliZ)
 *
 * Also using some ideas by Florian Luescher (https://github.com/fluescher/).
 *
 */
public class JassBotApplication {
    private static final int NUMBER_OF_BOT_PLAYERS = 4;

    private static final Logger logger = LogManager.getLogger(JassBotApplication.class);

    public static void main(String[] args) {
        logger.info("Starting JassBotApplication");

        try {
            CountDownLatch countDown = new CountDownLatch(NUMBER_OF_BOT_PLAYERS);

            for(int i = 0; i < NUMBER_OF_BOT_PLAYERS; i++) {
                Strategy strategy;
                if (i % 2 == 0) {
                    strategy = new MonteCarloStrategy();
                } else {
                    strategy = new RandomStrategy();
                }
                new JassBotPlayer(countDown, i, strategy);
            }

            countDown.await();
        } catch (InterruptedException e) {
            logger.error("JassBotApplication was interrupted.", e);
            e.printStackTrace();
        }
    }

}