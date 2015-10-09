package com.zuehlke.jasschallenge.client.sb.socket;

import com.google.gson.Gson;
import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.model.card.CardNumber;
import com.zuehlke.jasschallenge.client.sb.socket.json.GsonInitializer;
import com.zuehlke.jasschallenge.client.sb.model.card.Suit;
import org.junit.Assert;
import org.junit.Test;

public class CardAdapterTest {

    private Gson gson = GsonInitializer.gson;

    @Test
    public void card_thatIsValid_isSerializedAndDeserializedCorrectly() {
        Card card = new Card(Suit.SPADES, CardNumber.ACE);
        String json = gson.toJson(card);
        Card fromJson = gson.fromJson(json, Card.class);
        Assert.assertEquals(card, fromJson);
    }
}
