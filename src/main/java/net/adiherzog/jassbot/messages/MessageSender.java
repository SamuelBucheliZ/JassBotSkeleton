package net.adiherzog.jassbot.messages;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import javax.websocket.Session;
import java.io.IOException;
import org.apache.log4j.Logger;

public class MessageSender {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private Session session;

    public MessageSender(Session session)  {
        Preconditions.checkNotNull(session);
        this.session = session;
    }

    public void sendMessage(OutgoingMessage outgoingMessage) {
        String messageAsJson = new Gson().toJson(outgoingMessage);
        try {
            this.session.getBasicRemote().sendText(messageAsJson);
        } catch (IOException e) {
            logger.error("Could not send message", e);
        }
    }

}
