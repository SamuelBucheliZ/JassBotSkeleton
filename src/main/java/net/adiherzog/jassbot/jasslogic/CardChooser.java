package net.adiherzog.jassbot.jasslogic;

import com.google.common.base.Preconditions;
import net.adiherzog.jassbot.model.Card;

public class CardChooser {

       public Card chooseCard(GameState gameState) {
        Preconditions.checkNotNull(gameState);
        gameState.checkIsValid();

        return gameState.getAllowedCardsToPlay().iterator().next();
    }

}
