package net.adiherzog.jassbot.bot;

import net.adiherzog.jassbot.jasslogic.CardChooser;
import net.adiherzog.jassbot.jasslogic.GameState;
import net.adiherzog.jassbot.jasslogic.TrumpfChooser;
import net.adiherzog.jassbot.messages.*;
import net.adiherzog.jassbot.model.*;
import net.adiherzog.jassbot.messages.MessageSender;

import org.apache.log4j.Logger;

public class Game {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String playerName;
    private String sessionName;
    private MessageSender messageSender;
    private CardChooser cardChooser = new CardChooser();

    private GameState gameState = new GameState();

    public Game(String playerName, String sessionName, MessageSender messageSender) {
        logger.info(playerName + ": Created new game for player");
        this.playerName = playerName;
        this.sessionName = sessionName;
        this.messageSender = messageSender;
    }

    public void processMessage(IncommingMessage incommingMessage) {
        switch (incommingMessage.getType()) {
            case REQUEST_PLAYER_NAME:
                messageSender.sendMessage(createPlayerNameMessage());
                break;
            case REQUEST_SESSION_CHOICE:
                messageSender.sendMessage(createSessionChoiceMessage());
                break;
            case DEAL_CARDS:
                gameState.getMyCards().addAll(incommingMessage.getCards());
                break;
            case REQUEST_TRUMPF:
                Trumpf trumpf = new TrumpfChooser().chooseTrumpf(gameState.getMyCards());
                if (!trumpf.getMode().equals(TrumpfMode.SCHIEBE)) {
                    gameState.setIMadeTrumpf(true);
                }
                messageSender.sendMessage(createChooseTrumpfMessage(trumpf));
                break;
            case BROADCAST_TRUMPF:
                gameState.setTrumpf(incommingMessage.getTrumpf());
                break;
            case REQUEST_CARD:
                gameState.setCardsOnTable(incommingMessage.getCardsOnTable());
                Card card = cardChooser.chooseCard(gameState);
                gameState.getMyCards().remove(card);
                messageSender.sendMessage(createChooseCardMessage(card));
                break;
            case BROADCAST_STICH:
                logger.info(playerName + ": " + incommingMessage.getStich());
                gameState.startNextRound();
                break;
            case BROADCAST_GAME_FINISHED:
                gameState.resetAfterGameRound();
                break;
        }
    }

    private OutgoingMessage createPlayerNameMessage() {
        OutgoingMessage outgoingMessage = new OutgoingMessage(MessageType.CHOOSE_PLAYER_NAME);
        outgoingMessage.setData(playerName);
        return outgoingMessage;
    }

    private OutgoingMessage createSessionChoiceMessage() {
        OutgoingMessage outgoingMessage = new OutgoingMessage(MessageType.CHOOSE_SESSION);
        outgoingMessage.setData(createSessionChoiceData());
        return outgoingMessage;
    }

    private SessionChoice createSessionChoiceData() {
        return new SessionChoice(SessionChoiceType.JOIN_EXISTING, sessionName);
    }

    private OutgoingMessage createChooseTrumpfMessage(Trumpf trumpf) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(MessageType.CHOOSE_TRUMPF);
        outgoingMessage.setData(trumpf);
        return outgoingMessage;
    }

    private OutgoingMessage createChooseCardMessage(Card card) {
        OutgoingMessage outgoingMessage = new OutgoingMessage(MessageType.CHOOSE_CARD);
        outgoingMessage.setData(card);
        return outgoingMessage;
    }

}
