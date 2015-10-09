package com.zuehlke.jasschallenge.client.sb.socket.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.zuehlke.jasschallenge.client.sb.socket.messages.*;
import com.zuehlke.jasschallenge.client.sb.model.Stich;
import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfMessage;

import java.lang.reflect.Type;
import java.util.*;

public class MessageAdapter implements JsonDeserializer<Message> {
    private static final Type cardType = new TypeToken<Card>(){}.getType();
    private static final Type listOfCardsType = new TypeToken<LinkedList<Card>>(){}.getType();
    private static final Type setOfCardsType = new TypeToken<HashSet<Card>>(){}.getType();
    private static final Type stichType = new TypeToken<Stich>(){}.getType();
    private static final Type trumpfMessageType = new TypeToken<TrumpfMessage>(){}.getType();

    @Override
    public Message deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonPrimitive typePrimitive = jsonObject.getAsJsonPrimitive("type");
        if (Objects.isNull(typePrimitive)) {
            throw new JsonParseException("Field \"type\" is missing: "  + jsonElement);
        }
        JsonElement data = jsonObject.get("data");
        Message message;
        MessageType messageType = MessageType.valueOf(typePrimitive.getAsString());
        switch (messageType) {
            case BAD_MESSAGE:
                message = new BadMessage(jsonElement.toString());
                break;
            case BROADCAST_GAME_FINISHED:
                message = new BroadcastGameFinished(jsonElement.toString());
                break;
            case BROADCAST_SESSION_JOINED:
                message = new BroadcastSessionJoined(jsonElement.toString());
                break;
            case BROADCAST_STICH:
                Stich stich = jsonDeserializationContext.deserialize(data, stichType);
                message = new BroadcastStich(stich);
                break;
            case BROADCAST_TEAMS:
                message = new BroadcastTeams(jsonElement.toString());
                break;
            case BROADCAST_TRUMPF:
                TrumpfMessage trumpfMessage = jsonDeserializationContext.deserialize(data, trumpfMessageType);
                message = new BroadcastTrumpf(trumpfMessage.toTrumpf());
                break;
            case BROADCAST_WINNER_TEAM:
                message = new BroadcastWinnerTeam();
                break;
            case DEAL_CARDS:
                Set<Card> cards = jsonDeserializationContext.deserialize(data, setOfCardsType);
                message = new DealCards(cards);
                break;
            case PLAYED_CARDS:
                List<Card> playedCards = jsonDeserializationContext.deserialize(data, listOfCardsType);
                message = new PlayedCards(playedCards);
                break;
            case REJECT_CARD:
                Card rejectedCard = jsonDeserializationContext.deserialize(data, cardType);
                message = new RejectCard(rejectedCard);
                break;
            case REQUEST_CARD:
                List<Card> cardsOnTable = jsonDeserializationContext.deserialize(data, listOfCardsType);
                message = new RequestCard(cardsOnTable);
                break;
            case REQUEST_PLAYER_NAME:
                message = new RequestPlayerName();
                break;
            case REQUEST_SESSION_CHOICE:
                message = new RequestSessionChoice();
                break;
            case REQUEST_TRUMPF:
                boolean isSchiebenAllowed = !data.getAsBoolean();
                message = new RequestTrumpf(isSchiebenAllowed);
                break;
            default:
                throw new JsonParseException("Unknown message type: "  + jsonElement);
        }
        return message;
    }
}
