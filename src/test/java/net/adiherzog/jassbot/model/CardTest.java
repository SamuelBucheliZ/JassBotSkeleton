package net.adiherzog.jassbot.model;

import net.adiherzog.jassbot.model.Card;
import net.adiherzog.jassbot.model.Suit;
import org.junit.Assert;
import org.junit.Test;

public class CardTest {

    @Test
    public void compareTo_sixToSeven_sevenIsBigger() {
        Card six = new Card(Suit.CLUBS, 6);
        Card seven = new Card(Suit.CLUBS, 7);

        int compareResult = six.compareTo(seven);
        Assert.assertEquals(-1, compareResult);
    }

    @Test
    public void equals_forTwoCardsWithSameSuitAndNumber_returnsTrue() {
        Card six = new Card(Suit.CLUBS, 6);
        Card six2 = new Card(Suit.CLUBS, 6);

        Assert.assertEquals(six, six2);
    }

    @Test
    public void beatsCard_trumpf_withNonTrumpfCards() {
        Card lowerCard = new Card(Suit.CLUBS, 6);
        Card higherCard = new Card(Suit.CLUBS, 8);
        Trumpf trumpf = new Trumpf(TrumpfMode.TRUMPF, Suit.HEARTS);

        Assert.assertTrue(higherCard.beatsCard(lowerCard, trumpf));
        Assert.assertFalse(lowerCard.beatsCard(higherCard, trumpf));
    }

    @Test
    public void beatsCard_trumpf_withOneTrumpfCard() {
        Card trumpfCard = new Card(Suit.CLUBS, 6);
        Card nonTrumpfCard = new Card(Suit.SPADES, 8);
        Trumpf trumpf = new Trumpf(TrumpfMode.TRUMPF, Suit.CLUBS);

        Assert.assertTrue(trumpfCard.beatsCard(nonTrumpfCard, trumpf));
        Assert.assertFalse(nonTrumpfCard.beatsCard(trumpfCard, trumpf));
    }

    @Test
    public void beatsCard_trumpf_withTwoTrumpfCardOneOfThemJack() {
        Card higherTrumpf = new Card(Suit.CLUBS, 11);
        Card lowerTrumpf = new Card(Suit.CLUBS, 14);
        Trumpf trumpf = new Trumpf(TrumpfMode.TRUMPF, Suit.CLUBS);

        Assert.assertTrue(higherTrumpf.beatsCard(lowerTrumpf, trumpf));
        Assert.assertFalse(lowerTrumpf.beatsCard(higherTrumpf, trumpf));
    }

    @Test
    public void beatsCard_trumpf_withTwoTrumpfCardOneOfThemNine() {
        Card higherTrumpf = new Card(Suit.CLUBS, 9);
        Card lowerTrumpf = new Card(Suit.CLUBS, 14);
        Trumpf trumpf = new Trumpf(TrumpfMode.TRUMPF, Suit.CLUBS);

        Assert.assertTrue(higherTrumpf.beatsCard(lowerTrumpf, trumpf));
        Assert.assertFalse(lowerTrumpf.beatsCard(higherTrumpf, trumpf));
    }

    @Test
    public void beatsCard_forUndeufe() {
        Card lowerCard = new Card(Suit.CLUBS, 6);
        Card higherCard = new Card(Suit.CLUBS, 8);
        Trumpf trumpf = new Trumpf(TrumpfMode.UNDEUFE);

        Assert.assertTrue(lowerCard.beatsCard(higherCard, trumpf));
        Assert.assertFalse(higherCard.beatsCard(lowerCard, trumpf));
    }

    @Test
    public void beatsCard_forObeabe() {
        Card lowerCard = new Card(Suit.CLUBS, 6);
        Card higherCard = new Card(Suit.CLUBS, 8);
        Trumpf trumpf = new Trumpf(TrumpfMode.OBEABE);

        Assert.assertTrue(higherCard.beatsCard(lowerCard, trumpf));
        Assert.assertFalse(lowerCard.beatsCard(higherCard, trumpf));
    }

}
