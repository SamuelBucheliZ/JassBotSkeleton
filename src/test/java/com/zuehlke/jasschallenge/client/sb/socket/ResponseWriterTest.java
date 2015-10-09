package com.zuehlke.jasschallenge.client.sb.socket;

import com.google.gson.Gson;
import com.zuehlke.jasschallenge.client.sb.socket.responses.ChooseCard;
import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.model.card.CardNumber;
import com.zuehlke.jasschallenge.client.sb.model.card.Suit;
import com.zuehlke.jasschallenge.client.sb.socket.json.GsonInitializer;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;
import org.junit.Assert;
import org.junit.Test;

public class ResponseWriterTest {
    private Gson gson = GsonInitializer.gson;

    @Test
    public void response_isSerializedCorrectly() {
        Response response = new ChooseCard(new Card(Suit.DIAMONDS, CardNumber.QUEEN));
        String responseAsJson = gson.toJson(response);
        Assert.assertEquals("{\"type\":\"CHOOSE_CARD\",\"data\":{\"color\":\"DIAMONDS\",\"number\":12}}", responseAsJson);
    }

}
