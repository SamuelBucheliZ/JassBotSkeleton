package com.zuehlke.jasschallenge.client.sb;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.Strategy;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.RandomStrategy;
import com.zuehlke.jasschallenge.client.sb.socket.JassBotClientEndpoint;
import org.apache.log4j.Logger;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

/**
 * One Jass player. This program can simulate multiple players.
 */
public class BotPlayer {
    private static final Config conf = ConfigFactory.load().getConfig("bot-player");
    private static final String SERVER_URI = conf.getString("SERVER_URI");
    private static final String SESSION_NAME = conf.getString("SESSION_NAME");
    private static final String BOT_NAME_PREFIX = conf.getString("BOT_NAME_PREFIX");

    private Logger logger = Logger.getLogger(this.getClass());

    public BotPlayer(CountDownLatch countDown, int playerID) {
        //String playerName = BOT_NAME_PREFIX + playerID + ":" + UUID.randomUUID().hashCode();
        String playerName = BOT_NAME_PREFIX + playerID;
        logger.info("Creating bot player " + playerName + " for session " + SESSION_NAME);

        Strategy strategy = new RandomStrategy();
        Game game = new Game(playerName, SESSION_NAME, strategy);

        ClientManager client = ClientManager.createClient();
        try {
            JassBotClientEndpoint endpoint = new JassBotClientEndpoint(game, countDown);
            client.connectToServer(endpoint, new URI(SERVER_URI));
        } catch (DeploymentException | URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
