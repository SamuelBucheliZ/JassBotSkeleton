package com.zuehlke.jasschallenge.client.sb.socket.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.zuehlke.jasschallenge.client.sb.model.Stich;
import com.zuehlke.jasschallenge.client.sb.model.Team;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.socket.messages.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.util.*;


public class MessageAdapter implements JsonDeserializer<Message> {
    private static final Logger logger = LogManager.getLogger(MessageAdapter.class);

    private static final Type cardType = new TypeToken<Card>(){}.getType();
    private static final Type listOfCardsType = new TypeToken<LinkedList<Card>>(){}.getType();
    private static final Type listOfPointsInformationType = new TypeToken<LinkedList<PointsInformation>>(){}.getType();
    private static final Type pointsInformationType = new TypeToken<PointsInformation>(){}.getType();
    private static final Type sessionJoinedDataType = new TypeToken<SessionJoinedData>(){}.getType();
    private static final Type setOfCardsType = new TypeToken<HashSet<Card>>(){}.getType();
    private static final Type stichType = new TypeToken<Stich>(){}.getType();
    private static final Type teamListType = new TypeToken<List<Team>>(){}.getType();
    private static final Type trumpfType = new TypeToken<Trumpf>(){}.getType();

    @Override
    public Message deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonPrimitive typePrimitive = jsonObject.getAsJsonPrimitive("type");
        if (Objects.isNull(typePrimitive)) {
            // we expect the field type to be present for messages...
            throw new JsonParseException("Field \"type\" is missing: "  + jsonElement);
        }
        JsonElement data = jsonObject.get("data");
        Message message;
        String messageTypeString = typePrimitive.getAsString();
        // ...but we do not assume we know all possible message types
        if (!EnumSet.allOf(MessageType.class).stream().map(MessageType::toString).anyMatch(s -> s.equals(messageTypeString))) {
            logger.info("Unknown message type {}: {}", messageTypeString, jsonElement);
            return new NullMessage();
        }
        MessageType messageType = MessageType.valueOf(typePrimitive.getAsString());
        switch (messageType) {
            case BAD_MESSAGE:
                message = new BadMessage(jsonElement.toString());
                break;
            case BROADCAST_GAME_FINISHED:
                List<PointsInformation> gameFinishedPointsInformation = jsonDeserializationContext.deserialize(data, listOfPointsInformationType);
                message = new BroadcastGameFinished(gameFinishedPointsInformation);
                break;
            case BROADCAST_SESSION_JOINED:
                SessionJoinedData sessionJoinedData = jsonDeserializationContext.deserialize(data, sessionJoinedDataType);
                message = new BroadcastSessionJoined(sessionJoinedData);
                break;
            case BROADCAST_STICH:
                Stich stich = jsonDeserializationContext.deserialize(data, stichType);
                message = new BroadcastStich(stich);
                break;
            case BROADCAST_TOURNAMENT_STARTED:
                message = new BroadCastTournamentStarted(jsonElement.toString());
                break;
            case BROADCAST_TOURNAMENT_RANKING_TABLE:
                message = new BroadCastTournamentRankingTable(jsonElement.toString());
                break;
            case BROADCAST_TEAMS:
                List<Team> broadCastTeams = jsonDeserializationContext.deserialize(data, teamListType);
                message = new BroadcastTeams(broadCastTeams);
                break;
            case BROADCAST_TRUMPF:
                Trumpf trumpf = jsonDeserializationContext.deserialize(data, trumpfType);
                message = new BroadcastTrumpf(trumpf);
                break;
            case BROADCAST_WINNER_TEAM:
                PointsInformation winningTeamPointsInformation = jsonDeserializationContext.deserialize(data, pointsInformationType);
                message = new BroadcastWinnerTeam(winningTeamPointsInformation);
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
                boolean isGeschoben = data.getAsBoolean();
                message = new RequestTrumpf(isGeschoben);
                break;
            default:
                logger.info("Message type {} currently not handled: {}", messageType, jsonElement);
                message = new NullMessage();
        }
        return message;
    }
}
