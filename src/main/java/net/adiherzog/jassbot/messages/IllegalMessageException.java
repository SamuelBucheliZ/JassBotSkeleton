package net.adiherzog.jassbot.messages;

public class IllegalMessageException extends RuntimeException {

    private String messageFromServer;

    public IllegalMessageException(String message, String messageFromServer) {
        super(message);
        this.messageFromServer = messageFromServer;
    }

    public String getMessageFromServer() {
        return messageFromServer;
    }

}
