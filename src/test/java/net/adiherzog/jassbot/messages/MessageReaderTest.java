package net.adiherzog.jassbot.messages;

import net.adiherzog.jassbot.model.MessageType;
import org.junit.Assert;
import org.junit.Test;

public class MessageReaderTest {

    private MessageReader messageReader = new MessageReader();

    @Test
    public void requestPlayerNameMessage_thatIsValid_isReadCorrectly() {
        String messageFromServer = "{\"type\":\"REQUEST_PLAYER_NAME\"}";

        IncommingMessage message = messageReader.read(messageFromServer);

        Assert.assertNotNull(message);
        Assert.assertEquals(MessageType.REQUEST_PLAYER_NAME, message.getType());
    }

    @Test
    public void messageWithoutType_throwsException() {
        String messageFromServer = "{\"somethingElse\":\"bla\"}";

        try {
            IncommingMessage message = messageReader.read(messageFromServer);
        } catch(IllegalMessageException e) {
            Assert.assertEquals(messageFromServer, e.getMessageFromServer());
            Assert.assertEquals("Field \"type\" is missing.", e.getMessage());
        }
    }

}
