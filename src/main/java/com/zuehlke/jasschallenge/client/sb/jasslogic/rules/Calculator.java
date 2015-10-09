package com.zuehlke.jasschallenge.client.sb.jasslogic.rules;

import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;

import java.util.List;

public class Calculator {
    private final Trumpf trumpf;
    private final List<Card> cardsOnTable;

    public Calculator(Trumpf trumpf, List<Card> cardsOnTable) {
        this.trumpf = trumpf;
        this.cardsOnTable = cardsOnTable;
    }


}
