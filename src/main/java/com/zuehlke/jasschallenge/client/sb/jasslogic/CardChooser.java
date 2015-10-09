package com.zuehlke.jasschallenge.client.sb.jasslogic;

import com.google.common.base.Preconditions;
import com.zuehlke.jasschallenge.client.sb.game.GameState;
import com.zuehlke.jasschallenge.client.sb.model.card.Card;

public class CardChooser {

       public Card chooseCard(GameState gameState) {
        Preconditions.checkNotNull(gameState);
        gameState.checkIsValid();

        return gameState.getAllowedCardsToPlay().iterator().next();
    }

}
