package com.zuehlke.jasschallenge.client.sb.socket.json;


import com.google.gson.*;
import com.zuehlke.jasschallenge.client.sb.model.cards.CardNumber;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;

import java.lang.reflect.Type;

public class CardAdapter implements JsonSerializer<Card>, JsonDeserializer<Card> {

    @Override
    public Card deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Suit suit = Suit.valueOf(jsonObject.get("color").getAsString());
        CardNumber cardNumber = CardNumber.valueOf(jsonObject.get("number").getAsInt());
        return new Card(suit, cardNumber);
    }

    @Override
    public JsonElement serialize(Card card, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("color", card.getSuit().name());
        jsonObject.addProperty("number", card.getNumber());
        return jsonObject;
    }
}
