package com.zuehlke.jasschallenge.client.sb.socket;

import com.zuehlke.jasschallenge.client.sb.socket.json.ResponseWriter;
import com.zuehlke.jasschallenge.client.sb.game.Game;
import com.zuehlke.jasschallenge.client.sb.socket.json.MessageReader;
import com.zuehlke.jasschallenge.client.sb.socket.messages.Message;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.websocket.*;
import org.apache.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

/**
 * Handles the WebSocket communication for one Jass bot player.
 */
@ClientEndpoint
public class JassBotClientEndpoint {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final MessageReader messageReader = new MessageReader();

    private final CountDownLatch countDown;

    private Game game;

    public JassBotClientEndpoint(Game game, CountDownLatch countDown) {
        this.game = game;
        this.countDown = countDown;
    }

    @OnOpen
    public void onOpen(Session session) {
        logger.info(game.getPlayerName() + ": Opened connection to server. Session: " + session.getId());
    }

    /**
     * Possible messages see:<br>
     * https://github.com/webplatformz/challenge/wiki/Sample-Output<br>
     * https://github.com/webplatformz/challenge/blob/master/shared/messages/messages.js
     */
    @OnMessage
    public void onMessage(String messageFromServer, Session session) {
        Message message = messageReader.readMessage(messageFromServer);
        Optional<Response> response = message.dispatch(game);
        if (response.isPresent()) {
            ResponseWriter responseWriter = new ResponseWriter(session);
            responseWriter.sendResponse(response.get());
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("%s: Session %s close because of %s", game.getPlayerName(), session.getId(), closeReason));
        countDown.countDown();
    }

    @OnError
    public void onError(Throwable t) {
        ExceptionUtils.printRootCauseStackTrace(t);
        logger.error(game.getPlayerName() + ": Error " + t.getMessage());
    }

}
