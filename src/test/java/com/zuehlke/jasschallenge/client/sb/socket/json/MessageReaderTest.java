package com.zuehlke.jasschallenge.client.sb.socket.json;

import com.google.gson.JsonParseException;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.CardNumber;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfMode;
import com.zuehlke.jasschallenge.client.sb.socket.json.MessageReader;
import com.zuehlke.jasschallenge.client.sb.socket.messages.BroadcastTrumpf;
import com.zuehlke.jasschallenge.client.sb.socket.messages.DealCards;
import com.zuehlke.jasschallenge.client.sb.socket.messages.Message;
import com.zuehlke.jasschallenge.client.sb.socket.messages.RequestPlayerName;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class MessageReaderTest {

    private MessageReader messageReader = new MessageReader();

    @Test
    public void requestPlayerNameMessage_thatIsValid_isReadCorrectly() {
        String messageFromServer = "{\"type\":\"REQUEST_PLAYER_NAME\"}";

        Message message = messageReader.readMessage(messageFromServer);

        Assert.assertNotNull(message);
        Assert.assertTrue(message instanceof RequestPlayerName);
    }

    @Test
    public void messageWithoutType_throwsException() {
        String messageFromServer = "{\"somethingElse\":\"bla\"}";

        try {
            Message message = messageReader.readMessage(messageFromServer);
        } catch(JsonParseException e) {
            Assert.assertEquals("Field \"type\" is missing: {\"somethingElse\":\"bla\"}", e.getMessage());
        }
    }

    @Test
    public void trumpfMessage_thatIsValid_isReadCorrectly() {
        String messageFromServer = "{\"type\" : \"BROADCAST_TRUMPF\", \"data\" : { \"mode\" : \"TRUMPF\", \"trumpfColor\" : \"SPADES\"} }";

        Message message = messageReader.readMessage(messageFromServer);

        Assert.assertTrue(message instanceof BroadcastTrumpf);

        Trumpf trumpf = ((BroadcastTrumpf)message).getTrumpf();

        Assert.assertEquals(TrumpfMode.TRUMPF, trumpf.getMode());
        Assert.assertEquals(Suit.SPADES, trumpf.getSuit());

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

        Message message = messageReader.readMessage(messageFromServer);
        Assert.assertTrue(message instanceof DealCards);

        Set<Card> cards = ((DealCards)message).getCards();

        final int DECK_SIZE = 9;
        Assert.assertEquals(DECK_SIZE, cards.size());
        Assert.assertTrue(cards.contains(new Card(Suit.SPADES, CardNumber.valueOf(13))));
        Assert.assertTrue(cards.contains(new Card(Suit.SPADES, CardNumber.KING)));
        Assert.assertTrue(cards.contains(new Card(Suit.DIAMONDS, CardNumber.NINE)));
        Assert.assertTrue(cards.contains(new Card(Suit.HEARTS, CardNumber.QUEEN)));
        Assert.assertTrue(cards.contains(new Card(Suit.HEARTS, CardNumber.JACK)));
        Assert.assertTrue(cards.contains(new Card(Suit.HEARTS, CardNumber.TEN)));
        Assert.assertTrue(cards.contains(new Card(Suit.CLUBS, CardNumber.EIGHT)));
        Assert.assertTrue(cards.contains(new Card(Suit.SPADES, CardNumber.JACK)));
        Assert.assertTrue(cards.contains(new Card(Suit.HEARTS, CardNumber.NINE)));
        Assert.assertTrue(cards.contains(new Card(Suit.CLUBS, CardNumber.NINE)));
    }

}
