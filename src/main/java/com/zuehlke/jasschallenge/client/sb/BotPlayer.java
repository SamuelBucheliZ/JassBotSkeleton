package com.zuehlke.jasschallenge.client.sb;

import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.Strategy;
import com.zuehlke.jasschallenge.client.sb.jasslogic.strategy.TrivialStrategy;
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

    private Logger logger = Logger.getLogger(this.getClass());
    private static final String SERVER_URI = "wss://jasschallenge.herokuapp.com";
    private static final String SESSION_NAME = "showdown";
    private static final String BOT_NAME = "sb";

    public BotPlayer(CountDownLatch countDown, int playerID) {
        //String playerName = BOT_NAME + playerID + ":" + UUID.randomUUID().hashCode();
        String playerName = BOT_NAME + playerID;
        logger.info("Creating bot player " + playerName + " for session " + SESSION_NAME);

        Strategy strategy = new TrivialStrategy();
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
