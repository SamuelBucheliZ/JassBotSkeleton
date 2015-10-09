package com.zuehlke.jasschallenge.client.sb.socket.json;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import javax.websocket.Session;
import java.io.IOException;

import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;
import org.apache.log4j.Logger;

public class ResponseWriter {
    private static final Gson gson = GsonInitializer.gson;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final Session session;

    public ResponseWriter(Session session)  {
        Preconditions.checkNotNull(session);
        this.session = session;
    }

    public void sendResponse(Response response) {
        String responseAsJson = gson.toJson(response);
        try {
            this.session.getBasicRemote().sendText(responseAsJson);
        } catch (IOException e) {
            logger.error("Could not send onMessage: " + responseAsJson, e);
        }
    }
}
