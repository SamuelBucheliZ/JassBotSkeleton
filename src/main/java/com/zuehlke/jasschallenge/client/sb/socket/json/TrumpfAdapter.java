package com.zuehlke.jasschallenge.client.sb.socket.json;


import com.google.gson.*;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.*;

import java.lang.reflect.Type;

public class TrumpfAdapter implements JsonDeserializer<Trumpf> {

    @Override
    public Trumpf deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        TrumpfMode mode = TrumpfMode.valueOf(jsonObject.get("mode").getAsString());
        Trumpf trumpf;
        switch(mode) {
            case TRUMPF:
                Suit suit = Suit.valueOf(jsonObject.get("trumpfColor").getAsString());
                trumpf = new TrumpfSuit(suit);
                break;
            case OBEABE:
                trumpf = new TrumpfObeabe();
                break;
            case UNDEUFE:
                trumpf = new TrumpfUndeufe();
                break;
            case SCHIEBE:
                trumpf = new TrumpfSchiebe();
                break;
            default:
                throw new JsonParseException("Could not deserialize " + jsonElement);
        }
        return trumpf;
    }

}
