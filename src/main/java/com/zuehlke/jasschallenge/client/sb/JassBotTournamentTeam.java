package com.zuehlke.jasschallenge.client.sb;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.MonteCarloStrategy;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.Strategy;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.StrategyRepository;
import com.zuehlke.jasschallenge.client.sb.model.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class JassBotTournamentTeam {
    private static final Logger logger = LogManager.getLogger(JassBotTournamentTeam.class);

    public static void main(String[] args) {
        logger.info("Starting JassBotTournamentTeam");

        Config tournamentConfig = ConfigFactory.load().getConfig("jass-bot-tournament-team");
        boolean START_TOURNAMENT = tournamentConfig.getBoolean("START_TOURNAMENT");
        List<String> teamNames = tournamentConfig.getStringList("bot-teams");
        int countDownLatchSize = teamNames.size() * Team.TEAM_SIZE;
        if (START_TOURNAMENT) {
            countDownLatchSize += 1;
        }
        CountDownLatch countDown = new CountDownLatch(countDownLatchSize);

        try {
            for (String teamName : teamNames) {
                // override default configuration with custom configuration
                Config config = ConfigFactory.defaultOverrides()
                        .withValue("bot-player.BOT_NAME_PREFIX", ConfigValueFactory.fromAnyRef(teamName))
                        .withFallback(ConfigFactory.load().getConfig("jass-bot-tournament-team"))
                        .withFallback(ConfigFactory.load());

                for (int i = 0; i < Team.TEAM_SIZE; i++) {
                    Config teamConfig = tournamentConfig.getConfig(teamName);
                    String strategyName = teamConfig.getString("STRATEGY");
                    Strategy strategy;
                    try {
                        // if the strategy is configurable...
                        strategy = StrategyRepository.getStrategy(strategyName, teamConfig);
                    } catch (IllegalArgumentException e) {
                        // else...
                        strategy = StrategyRepository.getStrategy(strategyName);
                    }
                    logger.info("Creating bot {} for team {} using strategy {}", i, teamName, strategy.getClass().getSimpleName());
                    new JassBotPlayer(config, countDown, i, strategy);
                }
            }

            // TODO: add latch so bots can signal readiness for tournament
            if (START_TOURNAMENT) {
                Config config = ConfigFactory.defaultOverrides()
                        .withFallback(ConfigFactory.load().getConfig("jass-bot-tournament-team"))
                        .withFallback(ConfigFactory.load());

                new TournamentStarter(tournamentConfig.getConfig("bot-player").getString("SERVER_URI"),tournamentConfig.getConfig("bot-player").getString("SESSION_NAME"), countDown);
            }
            countDown.await();
        } catch (InterruptedException e) {
            logger.error("JassBotTournamentTeam was interrupted.", e);
        }
    }

}