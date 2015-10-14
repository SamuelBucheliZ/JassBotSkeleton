package com.zuehlke.jasschallenge.client.sb.model.cards;

import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfObeabe;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfSuit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfUndeufe;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;

import static com.zuehlke.jasschallenge.client.sb.model.cards.Card.*;

public class CardTest {

    @Test
    public void compareTo_sixToSeven_sevenIsBigger_inTrumpfModeObeabe() {
        Card six = CLUB_SIX;
        Card seven = CLUB_SEVEN;
        Trumpf trumpf = new TrumpfObeabe();

        Comparator<Card> comparator = trumpf.getComparator();
        int compareResult = comparator.compare(six, seven);
        Assert.assertEquals(-1, compareResult);
    }

    @Test
    public void equals_forTwoCardsWithSameSuitAndNumber_returnsTrue() {
        Card six = HEART_SIX;
        Card six2 = valueOf(Suit.HEARTS, CardNumber.SIX);

        Assert.assertEquals(six, six2);
    }

    @Test
    public void beatsCard_trumpf_withNonTrumpfCards() {
        Card lowerCard = CLUB_SIX;
        Card higherCard = CLUB_EIGHT;
        Trumpf trumpf = new TrumpfSuit(Suit.HEARTS);

        Assert.assertTrue(higherCard.beatsCard(lowerCard, trumpf));
        Assert.assertFalse(lowerCard.beatsCard(higherCard, trumpf));
    }

    @Test
    public void beatsCard_trumpf_withOneTrumpfCard() {
        Card trumpfCard = CLUB_SIX;
        Card nonTrumpfCard = SPADE_EIGHT;
        Trumpf trumpf = new TrumpfSuit(Suit.CLUBS);

        Assert.assertTrue(trumpfCard.beatsCard(nonTrumpfCard, trumpf));
        Assert.assertFalse(nonTrumpfCard.beatsCard(trumpfCard, trumpf));
    }

    @Test
    public void beatsCard_trumpf_withTwoTrumpfCardOneOfThemJack() {
        Card higherTrumpf = CLUB_JACK;
        Card lowerTrumpf = CLUB_ACE;
        Trumpf trumpf = new TrumpfSuit(Suit.CLUBS);

        Assert.assertTrue(higherTrumpf.beatsCard(lowerTrumpf, trumpf));
        Assert.assertFalse(lowerTrumpf.beatsCard(higherTrumpf, trumpf));
    }

    @Test
    public void beatsCard_trumpf_withTwoTrumpfCardOneOfThemNine() {
        Card higherTrumpf = CLUB_NINE;
        Card lowerTrumpf = CLUB_ACE;
        Trumpf trumpf = new TrumpfSuit(Suit.CLUBS);

        Assert.assertTrue(higherTrumpf.beatsCard(lowerTrumpf, trumpf));
        Assert.assertFalse(lowerTrumpf.beatsCard(higherTrumpf, trumpf));
    }

    @Test
    public void beatsCard_forUndeufe() {
        Card lowerCard = CLUB_SIX;
        Card higherCard = CLUB_EIGHT;
        Trumpf trumpf = new TrumpfUndeufe();

        Assert.assertTrue(lowerCard.beatsCard(higherCard, trumpf));
        Assert.assertFalse(higherCard.beatsCard(lowerCard, trumpf));
    }

    @Test
    public void beatsCard_forObeabe() {
        Card lowerCard = CLUB_SIX;
        Card higherCard = CLUB_EIGHT;
        Trumpf trumpf = new TrumpfObeabe();

        Assert.assertTrue(higherCard.beatsCard(lowerCard, trumpf));
        Assert.assertFalse(lowerCard.beatsCard(higherCard, trumpf));
    }

}
