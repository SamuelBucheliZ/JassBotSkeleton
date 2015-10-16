package com.zuehlke.jasschallenge.client.sb.socket;

import com.google.gson.Gson;
import com.zuehlke.jasschallenge.client.sb.socket.json.GsonInitializer;
import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.socket.messages.Message;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.*;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

/**
 * Handles the WebSocket communication for one Jass bot player.
 */
@ClientEndpoint
public class JassBotClientEndpoint {
    long time = 0;
    long requests = 0;

    private static final Gson gson = GsonInitializer.gson;

    private static final Logger logger = LogManager.getLogger(JassBotClientEndpoint.class);

    private final CountDownLatch countDown;

    private Game game;

    public JassBotClientEndpoint(Game game, CountDownLatch countDown) {
        this.game = game;
        this.countDown = countDown;
    }

    @OnOpen
    public void onOpen(Session session) {
        logger.info("{}: Opened connection to server. Session: {}.", game.getLocalPlayerName(), session.getId());
    }

    /**
     * Possible messages see:<br>
     * https://github.com/webplatformz/challenge/wiki/Sample-Output<br>
     * https://github.com/webplatformz/challenge/blob/master/shared/messages/messages.js
     */
    @OnMessage
    public void onMessage(String messageFromServer, Session session) {
        long startTime = System.nanoTime();
        Message message = gson.fromJson(messageFromServer, Message.class);
        Optional<Response> response = message.dispatch(game);
        if (response.isPresent()) {
            sendResponse(response.get(), session);
        }
        long endTime = System.nanoTime();
        time += (endTime-startTime)/100000;
        requests += 1;
        logger.trace("{}: Handling message {} took {} milliseconds.", game.getLocalPlayerName(), message.getClass().getSimpleName(), (endTime - startTime) / 100000);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info("{}: Session {} closed because of {}.", game.getLocalPlayerName(), session.getId(), closeReason);
        logger.info("{}: Served {} requests in {} milliseconds, average {} milliseconds per request.", game.getLocalPlayerName(), requests, time, time/requests);
        countDown.countDown();
    }

    @OnError
    public void onError(Throwable t) {
        logger.error(String.format("%s: Error.", game.getLocalPlayerName()), t);
    }

    private void sendResponse(Response response, Session session) {
        String responseAsJson = gson.toJson(response);
        try {
            session.getBasicRemote().sendText(responseAsJson);
        } catch (IOException e) {
            logger.error(String.format("%s: Could not send response %s to message.", game.getLocalPlayerName(), responseAsJson), e);
        }
    }
}
