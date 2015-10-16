package com.zuehlke.jasschallenge.client.sb;

import com.zuehlke.jasschallenge.client.sb.socket.TournamentStarterEndpoint;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;


public class TournamentStarter {
    public TournamentStarter(String SERVER_URI, String sessionName, CountDownLatch countDown) {
        ClientManager client = ClientManager.createClient();
        try {
            TournamentStarterEndpoint endpoint = new TournamentStarterEndpoint(sessionName, countDown);
            client.connectToServer(endpoint, new URI(SERVER_URI));
        } catch (DeploymentException | URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
