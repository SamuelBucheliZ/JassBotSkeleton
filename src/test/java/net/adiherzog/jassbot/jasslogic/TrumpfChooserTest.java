package net.adiherzog.jassbot.jasslogic;

import net.adiherzog.jassbot.model.Card;
import net.adiherzog.jassbot.model.Suit;
import net.adiherzog.jassbot.model.Trumpf;
import net.adiherzog.jassbot.model.TrumpfMode;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class TrumpfChooserTest {

    @Test
    public void alwaysReturnsTrumfHearts() {
        Set<Card> cards = createCardsWithJackNineAndThirdCard();

        Trumpf trumpf = new TrumpfChooser().chooseTrumpf(cards);

        Assert.assertNotNull(trumpf);
        Assert.assertEquals(TrumpfMode.TRUMPF, trumpf.getMode());
        Assert.assertEquals(Suit.HEARTS, trumpf.getSuit());
    }

    private Set<Card> createCardsWithJackNineAndThirdCard() {
        Set<Card> cards = new HashSet<>();

        cards.add(new Card(Suit.HEARTS, 9));
        cards.add(new Card(Suit.HEARTS, 11));
        cards.add(new Card(Suit.HEARTS, 7));
        cards.add(new Card(Suit.CLUBS, 6));
        cards.add(new Card(Suit.CLUBS, 7));
        cards.add(new Card(Suit.CLUBS, 8));
        cards.add(new Card(Suit.DIAMONDS, 12));
        cards.add(new Card(Suit.DIAMONDS, 13));
        cards.add(new Card(Suit.DIAMONDS, 14));

        return cards;
    }

}
