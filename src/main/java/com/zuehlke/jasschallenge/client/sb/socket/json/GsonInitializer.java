package com.zuehlke.jasschallenge.client.sb.socket.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.socket.messages.Message;

public class GsonInitializer {
    private static final GsonBuilder gsonBuilder = new GsonBuilder();
    static {
        gsonBuilder.registerTypeAdapter(Card.class, new CardAdapter());
        gsonBuilder.registerTypeHierarchyAdapter(Message.class, new MessageAdapter());
    }
    public static final Gson gson = gsonBuilder.create();
}
