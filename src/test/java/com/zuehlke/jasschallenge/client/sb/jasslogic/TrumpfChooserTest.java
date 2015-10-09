package com.zuehlke.jasschallenge.client.sb.jasslogic;

import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.model.card.CardNumber;
import com.zuehlke.jasschallenge.client.sb.model.card.Suit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfMode;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class TrumpfChooserTest {

    @Test
    public void alwaysReturnsTrumpfHearts() {
        Set<Card> cards = createCardsWithJackNineAndThirdCard();

        Trumpf trumpf = new TrumpfChooser().chooseTrumpf(cards);

        Assert.assertNotNull(trumpf);
        Assert.assertEquals(TrumpfMode.TRUMPF, trumpf.getMode());
        Assert.assertEquals(Suit.HEARTS, trumpf.getSuit());
    }

    private Set<Card> createCardsWithJackNineAndThirdCard() {
        Set<Card> cards = new HashSet<>();

        cards.add(new Card(Suit.HEARTS, CardNumber.valueOf(9)));
        cards.add(new Card(Suit.HEARTS, CardNumber.valueOf(11)));
        cards.add(new Card(Suit.HEARTS, CardNumber.valueOf(7)));
        cards.add(new Card(Suit.CLUBS, CardNumber.valueOf(6)));
        cards.add(new Card(Suit.CLUBS, CardNumber.valueOf(7)));
        cards.add(new Card(Suit.CLUBS, CardNumber.valueOf(8)));
        cards.add(new Card(Suit.DIAMONDS, CardNumber.valueOf(12)));
        cards.add(new Card(Suit.DIAMONDS, CardNumber.valueOf(13)));
        cards.add(new Card(Suit.DIAMONDS, CardNumber.valueOf(14)));

        return cards;
    }

}
