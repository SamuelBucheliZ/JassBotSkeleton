package net.adiherzog.jassbot.messages;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.adiherzog.jassbot.model.Card;
import net.adiherzog.jassbot.model.MessageType;
import net.adiherzog.jassbot.model.Stich;
import net.adiherzog.jassbot.model.Trumpf;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Reads messages that come from the Jass server and converts them into Java Objects.
 */
public class MessageReader {

    private Gson gson = new Gson();
    private JsonParser parser = new JsonParser();

    public IncommingMessage read(String messageFromServer) {
        JsonObject messageAsJsonObject = parser.parse(messageFromServer).getAsJsonObject();
        JsonPrimitive typePrimitive = messageAsJsonObject.getAsJsonPrimitive("type");
        if(typePrimitive == null) {
            throw new IllegalMessageException("Field \"type\" is missing.", messageFromServer);
        }
        String type = typePrimitive.getAsString();
        IncommingMessage message = new IncommingMessage(MessageType.valueOf(type));
        addFurtherMessageFields(message, messageAsJsonObject);
        return message;
    }

    private void addFurtherMessageFields(IncommingMessage message, JsonObject messageAsJsonObject) {
        JsonElement data = messageAsJsonObject.get("data");

        switch(message.getType()) {
            case DEAL_CARDS:
                message.setCards(getCardsFromMessage(data));
                break;
            case REQUEST_TRUMPF:
                message.setSchiebenAllowed(getIsSchiebenAllowed(data));
                break;
            case BROADCAST_TRUMPF:
                message.setTrumpf(getTrumpf(data));
                break;
            case REQUEST_CARD:
                message.setCardsOnTable(getCardsFromMessageAsList(data));
                break;
            case BROADCAST_STICH:
                message.setStich(getStichFromMessage(data));
                break;
        }
    }

    private Stich getStichFromMessage(JsonElement data) {
        return gson.fromJson(data, Stich.class);
    }

    private boolean getIsSchiebenAllowed(JsonElement data) {
        return !data.getAsBoolean();
    }

    private Set<Card> getCardsFromMessage(JsonElement data) {
        Type listOfCardsType = new TypeToken<HashSet<Card>>(){}.getType();
        return gson.fromJson(data, listOfCardsType);
    }

    private List<Card> getCardsFromMessageAsList(JsonElement data) {
        Type listOfCardsType = new TypeToken<LinkedList<Card>>(){}.getType();
        return gson.fromJson(data, listOfCardsType);
    }

    private Trumpf getTrumpf(JsonElement data) {
        return gson.fromJson(data, Trumpf.class);
    }

}
