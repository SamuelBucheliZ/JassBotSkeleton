package net.adiherzog.jassbot.bot;

import net.adiherzog.jassbot.messages.JassBotClientEndpoint;
import org.apache.log4j.Logger;
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
public class BotPlayer {

    private Logger logger = Logger.getLogger(this.getClass());
    private static final String SERVER_URI = "wss://jasschallenge.herokuapp.com";
    private static final String SESSION_NAME = "showdown";
    private static final String BOT_NAME = "ah";

    public BotPlayer(CountDownLatch countDown) {
        String playerName = BOT_NAME + ":" + UUID.randomUUID().hashCode();
        logger.info("Creating bot player " + playerName + " for session " + SESSION_NAME);

        ClientManager client = ClientManager.createClient();
        try {
            JassBotClientEndpoint endpoint = new JassBotClientEndpoint(playerName, SESSION_NAME, countDown);
            client.connectToServer(endpoint, new URI(SERVER_URI));
        } catch (DeploymentException | URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
