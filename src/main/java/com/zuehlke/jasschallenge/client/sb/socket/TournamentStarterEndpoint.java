package com.zuehlke.jasschallenge.client.sb.socket;

import com.google.gson.Gson;
import com.zuehlke.jasschallenge.client.sb.socket.json.GsonInitializer;
import com.zuehlke.jasschallenge.client.sb.socket.responses.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.*;
import java.io.IOException;
import java.util.EnumMap;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

// TODO: This is quite an ugly hack, improve!
@ClientEndpoint
public class TournamentStarterEndpoint {
    private static final Gson gson = GsonInitializer.gson;

    private static final Logger logger = LogManager.getLogger(TournamentStarterEndpoint.class);

    private final CountDownLatch countDown;
    private final String sessionName;

    private enum TournamentStarterState {
        AWAITING_NAME_CHOICE,
        AWAITING_SESSION_CHOICE,
        TOURNAMENT_STARTED
    }

    private TournamentStarterState state;

    private EnumMap<TournamentStarterState, Consumer<Session>> messageHandlers;
    //Consumer<Session>[] messageHandlers;

    public TournamentStarterEndpoint(String sessionName, CountDownLatch countDown) {
        this.sessionName = sessionName;
        this.countDown = countDown;
        this.state = TournamentStarterState.AWAITING_NAME_CHOICE;
        this.messageHandlers = new EnumMap<>(TournamentStarterState.class);
        this.messageHandlers.put(TournamentStarterState.AWAITING_NAME_CHOICE, this::handleRequestName);
        this.messageHandlers.put(TournamentStarterState.AWAITING_SESSION_CHOICE, this::handleRequestSessionChoice);
        this.messageHandlers.put(TournamentStarterState.TOURNAMENT_STARTED, this::handleOther);
    }

    @OnOpen
    public void onOpen(Session session) {
    }

    public void handleRequestName(Session session) {
        Response response = new ChoosePlayerName("TournamentStarter" + UUID.randomUUID().hashCode());
        sendResponse(response, session);
        this.state = TournamentStarterState.AWAITING_SESSION_CHOICE;
    }

    public void handleRequestSessionChoice(Session session) {
        SessionChoiceData sessionChoiceData = new SessionChoiceData(SessionChoice.SPECTATOR, "showdown", SessionType.TOURNAMENT);
        Response response = new ChooseSession(sessionChoiceData);
        sendResponse(response, session);
        sendResponse(new StartTournament(), session);
        this.state = TournamentStarterState.TOURNAMENT_STARTED;
    }

    public void handleOther(Session session) {
    }

    @OnMessage
    public void onMessage(String messageFromServer, Session session) {
        logger.info("TournamentStarterEndpoint: Received message {}.", messageFromServer);
        messageHandlers.get(state).accept(session);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info("TournamentStarterEndpoint: Session {} closed because of {}.", session, closeReason);
        countDown.countDown();
    }

    @OnError
    public void onError(Throwable t) {
        logger.error("TournamentStarterEndpoint: Error.", t);
    }

    private void sendResponse(Response response, Session session) {
        String responseAsJson = gson.toJson(response);
        try {
            session.getBasicRemote().sendText(responseAsJson);
        } catch (IOException e) {
            logger.error("TournamentStarterEndpoint: Could not send message.", e);
        }
    }

}
