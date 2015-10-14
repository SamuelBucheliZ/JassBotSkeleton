package com.zuehlke.jasschallenge.client.sb.socket.responses;

import com.google.common.base.Preconditions;

public class SessionChoiceData {

    private final SessionChoice sessionChoice;
    private final String sessionName;
    private final SessionType sessionType;

    public SessionChoiceData(SessionChoice sessionChoice, String sessionName, SessionType sessionType) {
        Preconditions.checkNotNull(sessionChoice);
        Preconditions.checkNotNull(sessionName);
        this.sessionChoice = sessionChoice;
        this.sessionName = sessionName;
        this.sessionType = sessionType;
    }

    public SessionChoice getSessionChoice() {
        return sessionChoice;
    }

    public String getSessionName() {
        return sessionName;
    }

    public SessionType getSessionType() {
        return sessionType;
    }
}
