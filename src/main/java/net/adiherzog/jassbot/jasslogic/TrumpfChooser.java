package net.adiherzog.jassbot.jasslogic;

import com.google.common.base.Preconditions;
import net.adiherzog.jassbot.model.Card;
import net.adiherzog.jassbot.model.Suit;
import net.adiherzog.jassbot.model.Trumpf;
import net.adiherzog.jassbot.model.TrumpfMode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class TrumpfChooser {

    public Trumpf chooseTrumpf(Set<Card> myCards) {
        Preconditions.checkNotNull(myCards);
        Preconditions.checkArgument(myCards.size() == 9);
        for (Card card : myCards) {
            Preconditions.checkNotNull(card);
            Preconditions.checkNotNull(card.getSuit());
            Preconditions.checkArgument(card.getNumber() >= 6);
            Preconditions.checkArgument(card.getNumber() <= 14);
        }

        return new Trumpf(TrumpfMode.TRUMPF, Suit.HEARTS);
    }

}
