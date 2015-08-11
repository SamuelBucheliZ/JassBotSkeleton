package net.adiherzog.jassbot.messages;

import net.adiherzog.jassbot.model.Card;
import net.adiherzog.jassbot.model.MessageType;
import net.adiherzog.jassbot.model.Stich;
import net.adiherzog.jassbot.model.Trumpf;

import java.util.List;
import java.util.Set;

/**
 * A message comming from the server.
 */
public class IncommingMessage {

    private MessageType type;
    private Set<Card> cards;
    private boolean isSchiebenAllowed;
    private Trumpf trumpf;
    private List<Card> cardsOnTable;
    private Stich stich;

    public IncommingMessage(MessageType type) {
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public Set<Card> getCards() {
        return cards;
    }

    /**
     * Only relevant for MessageType.REQUEST_TRUMPF
     */
    public void setSchiebenAllowed(boolean isSchiebenAllowed) {
        this.isSchiebenAllowed = isSchiebenAllowed;
    }

    /**
     * Only relevant for MessageType.REQUEST_TRUMPF
     */
    public boolean isSchiebenAllowed() {
        return isSchiebenAllowed;
    }

    public Trumpf getTrumpf() {
        return trumpf;
    }

    public void setTrumpf(Trumpf trumpf) {
        this.trumpf = trumpf;
    }

    public List<Card> getCardsOnTable() {
        return cardsOnTable;
    }

    public void setCardsOnTable(List<Card> cardsOnTable) {
        this.cardsOnTable = cardsOnTable;
    }

    public Stich getStich() {
        return stich;
    }

    public void setStich(Stich stich) {
        this.stich = stich;
    }

}
