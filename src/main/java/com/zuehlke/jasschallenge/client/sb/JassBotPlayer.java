package com.zuehlke.jasschallenge.client.sb;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.MonteCarloStrategy;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.Strategy;
import com.zuehlke.jasschallenge.client.sb.socket.JassBotClientEndpoint;
import com.zuehlke.jasschallenge.client.sb.socket.responses.SessionChoice;
import com.zuehlke.jasschallenge.client.sb.socket.responses.SessionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * One Jass player. This program can simulate multiple players.
 */
public class JassBotPlayer {

    private static final Logger logger = LogManager.getLogger(JassBotPlayer.class);

    public JassBotPlayer(CountDownLatch countDown, int playerID, Strategy strategy) {
        this(ConfigFactory.load(), countDown, playerID, strategy);
    }

    public JassBotPlayer(Config conf, CountDownLatch countDown, int playerID, Strategy strategy) {
        Config config = conf.getConfig("bot-player");
        String SERVER_URI = config.getString("SERVER_URI");
        String SESSION_NAME = config.getString("SESSION_NAME");
        String BOT_NAME_PREFIX = config.getString("BOT_NAME_PREFIX");
        boolean APPEND_UUID_TO_BOT_NAME = config.getBoolean("APPEND_UUID_TO_BOT_NAME");
        SessionChoice SESSION_CHOICE = SessionChoice.valueOf(config.getString("SESSION_CHOICE"));
        SessionType SESSION_TYPE = SessionType.valueOf(config.getString("SESSION_TYPE"));

        String playerName = BOT_NAME_PREFIX;
        if (APPEND_UUID_TO_BOT_NAME) {
            playerName += playerID + ":" + UUID.randomUUID().hashCode();
        }
        logger.info("Creating bot player {} for session  {}.", playerName, SESSION_NAME);

        Game game = new Game(playerName, SESSION_NAME, SESSION_CHOICE, SESSION_TYPE, strategy);

        ClientManager client = ClientManager.createClient();
        try {
            JassBotClientEndpoint endpoint = new JassBotClientEndpoint(game, countDown);
            client.connectToServer(endpoint, new URI(SERVER_URI));
        } catch (DeploymentException | URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
