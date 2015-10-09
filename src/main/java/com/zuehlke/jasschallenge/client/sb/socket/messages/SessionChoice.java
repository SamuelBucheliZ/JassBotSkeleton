package com.zuehlke.jasschallenge.client.sb.socket.messages;

import com.google.common.base.Preconditions;

public class SessionChoice {

    private SessionChoiceType sessionChoice;
    private String sessionName;

    public SessionChoice(SessionChoiceType sessionChoice, String sessionName) {
        Preconditions.checkNotNull(sessionChoice);
        Preconditions.checkNotNull(sessionName);
        this.sessionChoice = sessionChoice;
        this.sessionName = sessionName;
    }

    public SessionChoiceType getSessionChoice() {
        return sessionChoice;
    }

    public String getSessionName() {
        return sessionName;
    }

}
