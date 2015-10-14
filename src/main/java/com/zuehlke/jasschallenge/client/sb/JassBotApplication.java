package com.zuehlke.jasschallenge.client.sb;

import java.util.concurrent.CountDownLatch;

import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.MonteCarloStrategy;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.RandomStrategy;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.Strategy;
import org.apache.log4j.Logger;

/**
 * This is a Jass (swiss card game) bot implementation that plays the game
 * over an instance of the https://github.com/webplatformz/challenge Jass server.
 *
 * @author Adrian Herzog (https://github.com/adiherzog)
 */
public class JassBotApplication {

    private static Logger logger = Logger.getLogger(JassBotApplication.class.getName());
    private static final int NUMBER_OF_BOT_PLAYERS = 4;

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
            e.printStackTrace();
        }
    }

}