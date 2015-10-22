package com.zuehlke.jasschallenge.client.sb;

import java.util.concurrent.CountDownLatch;

import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a Jass (swiss card game) bot implementation that plays the game
 * using an instance of the https://github.com/webplatformz/challenge Jass server.
 *
 * @author Adrian Herzog (https://github.com/adiherzog)
 * @author Samuel Bucheli (https://github.com/SamuelBucheliZ)
 *
 * Also using lots of ideas by Florian Luescher (https://github.com/fluescher/).
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
                if (i < 2) {
                    strategy = StrategyRepository.getStrategy("IterationLimitedMCTSStrategy");
                    //strategy = StrategyRepository.getStrategy("MonteCarloStrategy");
                } else {
                    strategy = StrategyRepository.getStrategy("RandomStrategy");
                }
                new JassBotPlayer(countDown, i, strategy);
            }

            countDown.await();
        } catch (InterruptedException e) {
            logger.error("JassBotApplication was interrupted.", e);
        }
    }

}