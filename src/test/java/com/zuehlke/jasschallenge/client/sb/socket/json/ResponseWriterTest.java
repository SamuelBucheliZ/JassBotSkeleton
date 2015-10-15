package com.zuehlke.jasschallenge.client.sb.socket.json;

import com.google.gson.Gson;
import com.zuehlke.jasschallenge.client.sb.socket.responses.ChooseCard;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.socket.json.GsonInitializer;
import com.zuehlke.jasschallenge.client.sb.socket.responses.Response;
import org.junit.Assert;
import org.junit.Test;

public class ResponseWriterTest {
    private Gson gson = GsonInitializer.gson;

    @Test
    public void response_isSerializedCorrectly() {
        Response response = new ChooseCard(Card.DIAMOND_QUEEN);
        String responseAsJson = gson.toJson(response);
        Assert.assertEquals("{\"type\":\"CHOOSE_CARD\",\"data\":{\"color\":\"DIAMONDS\",\"number\":12}}", responseAsJson);
    }

}
