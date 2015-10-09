package com.zuehlke.jasschallenge.client.sb.socket.json;

import com.google.gson.*;
import com.zuehlke.jasschallenge.client.sb.socket.messages.Message;

/**
 * Reads messages that come from the Jass server and converts them into Java Objects.
 */
public class MessageReader {

    private static final Gson gson = GsonInitializer.gson;

    public Message readMessage(String messageFromServer) {
        return gson.fromJson(messageFromServer, Message.class);
    }

}
