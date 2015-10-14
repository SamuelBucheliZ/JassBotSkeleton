package com.zuehlke.jasschallenge.client.sb.socket.json;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.zuehlke.jasschallenge.client.sb.model.Player;
import com.zuehlke.jasschallenge.client.sb.model.Stich;
import com.zuehlke.jasschallenge.client.sb.model.Team;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfMode;
import com.zuehlke.jasschallenge.client.sb.socket.messages.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static com.zuehlke.jasschallenge.client.sb.model.cards.Card.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class MessageReaderTest {

    private static final Gson gson = GsonInitializer.gson;

    @Test
    public void requestPlayerNameMessage_thatIsValid_isReadCorrectly() {
        String messageFromServer = "{\"type\":\"REQUEST_PLAYER_NAME\"}";

        Message message = gson.fromJson(messageFromServer, Message.class);

        Assert.assertNotNull(message);
        Assert.assertTrue(message instanceof RequestPlayerName);
    }

    @Test
    public void messageWithoutType_throwsException() {
        String messageFromServer = "{\"somethingElse\":\"bla\"}";

        try {
            Message message = gson.fromJson(messageFromServer, Message.class);
        } catch(JsonParseException e) {
            Assert.assertEquals("Field \"type\" is missing: {\"somethingElse\":\"bla\"}", e.getMessage());
        }
    }

    @Test
    public void trumpfMessage_thatIsValid_isReadCorrectly() {
        String messageFromServer = "{\"type\" : \"BROADCAST_TRUMPF\", \"data\" : { \"mode\" : \"TRUMPF\", \"trumpfColor\" : \"SPADES\"} }";

        Message message = gson.fromJson(messageFromServer, Message.class);

        Assert.assertTrue(message instanceof BroadcastTrumpf);

        Trumpf trumpf = ((BroadcastTrumpf)message).getTrumpf();

        Assert.assertEquals(TrumpfMode.TRUMPF, trumpf.getMode());
        Assert.assertEquals(Suit.SPADES, trumpf.getSuit());
    }

    @Test
    public void broadcastTeamsMessage_thatIsvalid_isReadCorrectly() {
        String messageFromServer = "{\n" +
                "    \"type\" : \"BROADCAST_TEAMS\",\n" +
                "    \"data\" : [\n" +
                "            {\n" +
                "                name: \"Team 1\",\n" +
                "                players: [\n" +
                "                    {\n" +
                "                        name: \"Player 1\",\n" +
                "                        id: 0\n" +
                "                    }, {\n" +
                "                        name: \"Player 3\",\n" +
                "                        id: 2\n" +
                "                    }\n" +
                "                ]\n" +
                "            }, {\n" +
                "                name: \"Team 2\",\n" +
                "                players: [\n" +
                "                    {\n" +
                "                        name: \"Player 2\",\n" +
                "                        id: 1\n" +
                "                    }, {\n" +
                "                        name: \"Player 4\",\n" +
                "                        id: 3\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ]\n" +
                "}";

        Message message = gson.fromJson(messageFromServer, Message.class);

        assertThat(message, instanceOf(BroadcastTeams.class));

        List<Team> teams = ((BroadcastTeams)message).getTeams();

        assertThat(teams, is(notNullValue()));
        assertThat(teams.size(), is(2));

        Team team1 = teams.get(0);
        Team team2 = teams.get(1);

        assertThat(team1.getName(), is("Team 1"));

        List<Player> players1 = team1.getPlayers();

        assertThat(players1, is(notNullValue()));
        assertThat(players1.size(), is(2));

        Player player1 = players1.get(0);
        Player player3 = players1.get(1);

        assertThat(player1, is(notNullValue()));
        assertThat(player1.getId(), is(0));
        assertThat(player1.getName(), is("Player 1"));

        assertThat(player3, is(notNullValue()));
        assertThat(player3.getId(), is(2));
        assertThat(player3.getName(), is("Player 3"));

        assertThat(team2.getName(), is("Team 2"));

        List<Player> players2 = team2.getPlayers();

        assertThat(players2, is(notNullValue()));
        assertThat(players2.size(), is(2));

        Player player2 = players2.get(0);
        Player player4 = players2.get(1);

        assertThat(player2, is(notNullValue()));
        assertThat(player2.getId(), is(1));
        assertThat(player2.getName(), is("Player 2"));

        assertThat(player4, is(notNullValue()));
        assertThat(player4.getId(), is(3));
        assertThat(player4.getName(), is("Player 4"));
    }

    @Test
    public void dealCardsMessage_thatIsValid_isReadCorrectly() {
        String messageFromServer = "{\n" +
                "    \"type\" : \"DEAL_CARDS\",\n" +
                "    \"data\" : [{\n" +
                "            \"number\" : 13,\n" +
                "            \"color\" : \"SPADES\"\n" +
                "        }, {\n" +
                "            \"number\" : 9,\n" +
                "            \"color\" : \"DIAMONDS\"\n" +
                "        }, {\n" +
                "            \"number\" : 12,\n" +
                "            \"color\" : \"HEARTS\"\n" +
                "        }, {\n" +
                "            \"number\" : 11,\n" +
                "            \"color\" : \"HEARTS\"\n" +
                "        }, {\n" +
                "            \"number\" : 10,\n" +
                "            \"color\" : \"HEARTS\"\n" +
                "        }, {\n" +
                "            \"number\" : 8,\n" +
                "            \"color\" : \"CLUBS\"\n" +
                "        }, {\n" +
                "            \"number\" : 11,\n" +
                "            \"color\" : \"SPADES\"\n" +
                "        }, {\n" +
                "            \"number\" : 9,\n" +
                "            \"color\" : \"HEARTS\"\n" +
                "        }, {\n" +
                "            \"number\" : 9,\n" +
                "            \"color\" : \"CLUBS\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        Message message = gson.fromJson(messageFromServer, Message.class);
        Assert.assertTrue(message instanceof DealCards);

        Set<Card> cards = ((DealCards)message).getCards();

        final int DECK_SIZE = 9;
        Assert.assertEquals(DECK_SIZE, cards.size());
        Assert.assertTrue(cards.contains(SPADE_KING));
        Assert.assertTrue(cards.contains(DIAMOND_NINE));
        Assert.assertTrue(cards.contains(HEART_QUEEN));
        Assert.assertTrue(cards.contains(HEART_JACK));
        Assert.assertTrue(cards.contains(HEART_TEN));
        Assert.assertTrue(cards.contains(CLUB_EIGHT));
        Assert.assertTrue(cards.contains(SPADE_JACK));
        Assert.assertTrue(cards.contains(HEART_NINE));
        Assert.assertTrue(cards.contains(CLUB_NINE));
    }

    @Test
    public void broadcastStichMessage_thatIsValid_isReadCorrectly() {
        String messageFromServer = "{\n" +
                "    \"type\" : \"BROADCAST_STICH\",\n" +
                "    \"data\" : {\n" +
                "        \"name\" : \"Client 2\",\n" +
                "        \"id\" : 1,\n" +
                "        \"playedCards\" : [{\n" +
                "                \"number\" : 13,\n" +
                "                \"color\" : \"SPADES\"\n" +
                "            }, {\n" +
                "                \"number\" : 9,\n" +
                "                \"color\" : \"SPADES\"\n" +
                "            }, {\n" +
                "                \"number\" : 12,\n" +
                "                \"color\" : \"SPADES\"\n" +
                "            }, {\n" +
                "                \"number\" : 6,\n" +
                "                \"color\" : \"SPADES\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"teams\" : [{\n" +
                "                \"name\" : \"Team 2\",\n" +
                "                \"points\" : 42,\n" +
                "                \"currentRoundPoints\" : 42\n" +
                "            }, {\n" +
                "                \"name\" : \"Team 1\",\n" +
                "                \"points\" : 0,\n" +
                "                \"currentRoundPoints\" : 0\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";

        Message message = gson.fromJson(messageFromServer, Message.class);

        assertThat(message, instanceOf(BroadcastStich.class));

        Stich stich = ((BroadcastStich)message).getStich();

        assertThat(stich.getPlayerName(), is("Client 2"));
        assertThat(stich.getPlayerId(), is(1));
        assertThat(stich.getPlayedCards().size(), is(4));
        assertThat(stich.getPlayedCards(), hasItem(SPADE_KING));
        assertThat(stich.getPlayedCards(), hasItem(SPADE_NINE));
        assertThat(stich.getPlayedCards(), hasItem(SPADE_QUEEN));
        assertThat(stich.getPlayedCards(), hasItem(SPADE_SIX));
        assertThat(stich.getTeams().size(), is(2));
        assertThat(stich.getTeams().get(0).getName(), is("Team 2"));
        assertThat(stich.getTeams().get(0).getPoints(), is(42));
        assertThat(stich.getTeams().get(0).getCurrentRoundPoints(), is(42));
        assertThat(stich.getTeams().get(1).getName(), is("Team 1"));
        assertThat(stich.getTeams().get(1).getPoints(), is(0));
        assertThat(stich.getTeams().get(1).getCurrentRoundPoints(), is(0));
    }

}
