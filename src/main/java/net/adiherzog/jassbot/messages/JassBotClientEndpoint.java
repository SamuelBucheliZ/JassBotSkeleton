package net.adiherzog.jassbot.messages;

import net.adiherzog.jassbot.bot.Game;
import net.adiherzog.jassbot.model.MessageType;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.websocket.*;
import org.apache.log4j.Logger;

import java.util.concurrent.CountDownLatch;

/**
 * Handles the WebSocket communication for one Jass bot player.
 */
@ClientEndpoint
public class JassBotClientEndpoint {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private MessageReader messageReader = new MessageReader();

    private Session session;
    private Game game;
    private String playerName;
    private String sessionName;
    private CountDownLatch countDown;

    public JassBotClientEndpoint(String playerName, String sessionName, CountDownLatch countDown) {
        this.playerName = playerName;
        this.sessionName = sessionName;
        this.countDown = countDown;
    }

    @OnOpen
    public void open(Session session) {
        logger.info(playerName + ": Opened connection to server. Session: " + session.getId());
        game = new Game(playerName, sessionName, new MessageSender(session));
    }

    /**
     * Possible messages see:<br>
     * https://github.com/webplatformz/challenge/wiki/Sample-Output<br>
     * https://github.com/webplatformz/challenge/blob/master/shared/messages/messages.js
     */
    @OnMessage
    public void message(String messageFromServer, Session session) {
        IncommingMessage message = messageReader.read(messageFromServer);
        if ((message.getType().equals(MessageType.BROADCAST_SESSION_JOINED)
                || message.getType().equals(MessageType.BROADCAST_TEAMS)
                || message.getType().equals(MessageType.BROADCAST_GAME_FINISHED))) {
            logger.info(playerName + ": Message from server: " + messageFromServer);
        }
        game.processMessage(message);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("%s: Session %s close because of %s", playerName, session.getId(), closeReason));
        countDown.countDown();
    }

    @OnError
    public void onError(Throwable t) {
        ExceptionUtils.printRootCauseStackTrace(t);
        logger.error(playerName + ": Error " + t.getMessage());
    }

}
