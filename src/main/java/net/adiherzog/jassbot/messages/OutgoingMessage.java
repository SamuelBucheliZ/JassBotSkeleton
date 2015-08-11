package net.adiherzog.jassbot.messages;

import net.adiherzog.jassbot.model.MessageType;

public class OutgoingMessage {

    private MessageType type;
    private Object data;

    public OutgoingMessage(MessageType type) {
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

}
