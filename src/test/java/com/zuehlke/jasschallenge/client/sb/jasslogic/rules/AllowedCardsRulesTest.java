package com.zuehlke.jasschallenge.client.sb.jasslogic.rules;

import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfSuit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfUndeufe;
import org.junit.Assert;
import org.junit.Test;

import static com.zuehlke.jasschallenge.client.sb.model.cards.Card.*;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.*;
import java.util.function.Predicate;

public class AllowedCardsRulesTest {

    private MyCardsTestDataFactory myCardsFactory = new MyCardsTestDataFactory();
    private CardsOnTableTestDataFactory cardsOnTableFactory = new CardsOnTableTestDataFactory();

    @Test
    public void getAllowedCardsToPlay_noCardsOnTable_allCardsAreAllowed() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setTrumpf(new TrumpfSuit(Suit.CLUBS));
        builder.setMyCards(myCardsFactory.createMyCardsWithJackNineAndThirdCard());
        builder.setCardsOnTable(cardsOnTableFactory.createCardsOnTable());

        Set<Card> allowedCardsToPlay = builder.getAllowedCards();

        Assert.assertEquals(builder.getMyCards(), allowedCardsToPlay);
    }

    @Test
    public void getAllowedCardsToPlay_oneCardPlayed_andIHaveThisSuit_onlyCardsOfThisSuitAndTrumpfsAreAllowed() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setTrumpf(new TrumpfSuit(Suit.CLUBS));
        builder.setMyCards(myCardsFactory.createMyCardsWithJackNineAndThirdCard());
        builder.setCardsOnTable(cardsOnTableFactory.createCardsOnTable(HEART_SIX));

        Set<Card> allowedCardsToPlay = builder.getAllowedCards();

        Assert.assertEquals(6, allowedCardsToPlay.size());
        Set<Card> expectedAllowedCardsToPlay = new HashSet<>(Arrays.asList(builder.getMyCards().stream().filter(card -> card.getSuit().equals(Suit.HEARTS) || card.getSuit().equals(builder.getTrumpf().getSuit())).toArray(Card[]::new)));
        Assert.assertEquals(expectedAllowedCardsToPlay, allowedCardsToPlay);
    }

    @Test
    public void getAllowedCardsToPlay_trumpfOnTableButNotFirst_onlySmallerTrumpfsInHandPlusOtherCards_trumpfIsNotPlayed() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setTrumpf(new TrumpfSuit(Suit.CLUBS));
        builder.setMyCards(myCardsFactory.createMyCardsOnlySmallTrumpfsPlusOneDiamond());
        builder.setCardsOnTable(cardsOnTableFactory.createCardOnTableHeartSixAndClubsNine());

        Set<Card> allowedCardsToPlay = builder.getAllowedCards();

        Assert.assertEquals(1, allowedCardsToPlay.size());
        Set<Card> expectedAllowedCardsToPlay = new HashSet<>(Arrays.asList(new Card[]{DIAMOND_QUEEN}));
        Assert.assertEquals(expectedAllowedCardsToPlay, allowedCardsToPlay);
    }

    @Test
    public void getAllowedCardsToPlay_undeufe_heartOnTable_heartInHand_thenHeartIsPlayed() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setTrumpf(new TrumpfUndeufe());
        builder.setMyCards(myCardsFactory.createMyCardsWithOneHeart());
        builder.setCardsOnTable(cardsOnTableFactory.createCardOnTableHeartSix());

        Set<Card> allowedCardsToPlay = builder.getAllowedCards();

        Assert.assertEquals(1, allowedCardsToPlay.size());
        Set<Card> expectedAllowedCardsToPlay = new HashSet<>(Arrays.asList(new Card[]{HEART_JACK}));
        Assert.assertEquals(expectedAllowedCardsToPlay, allowedCardsToPlay);
    }

    @Test
    public void getAllowedCardsToPlay_threeNonTrumpfsPlayed_iHaveATrumpfAndThreeMatchingNonTrumpfs_theyAreAllReturned() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setMyCards(myCardsFactory.createMyCardsOneHeartThreeSpadesOneClubs());
        builder.setCardsOnTable(cardsOnTableFactory.createCardsOnTableThreeSpades());
        builder.setTrumpf(new TrumpfSuit(Suit.HEARTS));

        Set<Card> allowedCardsToPlay = builder.getAllowedCards();

        Assert.assertEquals(4, allowedCardsToPlay.size());
        Assert.assertTrue(allowedCardsToPlay.contains(SPADE_SIX));
        Assert.assertTrue(allowedCardsToPlay.contains(SPADE_JACK));
        Assert.assertTrue(allowedCardsToPlay.contains(SPADE_SEVEN));
        Assert.assertTrue(allowedCardsToPlay.contains(HEART_TEN));
    }

    @Test
    public void getAllowedCardsToPlay_thirdCardOnTableIsTrumpf_iHaveATrumpfAndThreeMatchingNonTrumpfs_myOnlyTrumpfIsLower_onlyNonTrumpfsAreReturned() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setTrumpf(new TrumpfSuit(Suit.DIAMONDS));
        builder.setCardsOnTable(cardsOnTableFactory.createCardsOnTable(HEART_QUEEN, HEART_KING, DIAMOND_JACK));
        builder.setMyCards(myCardsFactory.createMyCards(DIAMOND_QUEEN, HEART_JACK, CLUB_EIGHT));

        Set<Card> allowedCardsToPlay = builder.getAllowedCards();

        Assert.assertEquals(1, allowedCardsToPlay.size());
        Assert.assertEquals(allowedCardsToPlay.toArray()[0], HEART_JACK);
    }

    @Test
    public void getAllowedCardsToPlay_trumpfIsPlayed_andIHaveOnlyAweakerTrumpf_iStillHaveToPlayIt() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setTrumpf(new TrumpfSuit(Suit.SPADES));
        builder.setCardsOnTable(cardsOnTableFactory.createCardsOnTable(SPADE_JACK, CLUB_TEN));
        builder.setMyCards(myCardsFactory.createMyCards(DIAMOND_EIGHT, SPADE_SIX));

        Set<Card> allowedCardsToPlay = builder.getAllowedCards();

        Assert.assertEquals(1, allowedCardsToPlay.size());
        Assert.assertEquals(allowedCardsToPlay.toArray()[0], SPADE_SIX);
    }

    @Test
    public void noCardsOnTable_playAnyCard() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setMyCards(myCardsFactory.createMyCardsWithJackNineAndThirdCard());
        builder.setCardsOnTable(cardsOnTableFactory.createCardsOnTableEmpty());
        builder.setTrumpf(new TrumpfSuit(Suit.CLUBS));

        Set<Card> allowed = builder.getAllowedCards();

        Assert.assertEquals(builder.getMyCards(), allowed);
    }

    @Test
    public void cardsOnTable_andIHaveCardOfSameSuit_playCardOfSameSuitOrTrumpf() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setMyCards(myCardsFactory.createMyCardsWithJackNineAndThirdCard());
        builder.setCardsOnTable(cardsOnTableFactory.createCardsOnTable(CLUB_SIX));
        builder.setCardsOnTable(cardsOnTableFactory.createCardsOnTable(CLUB_NINE));
        builder.setTrumpf(new TrumpfSuit(Suit.HEARTS));

        Set<Card> allowed = builder.getAllowedCards();

        Predicate<Card> isClubsOrHearts = c -> Suit.CLUBS.equals(c.getSuit()) || Suit.HEARTS.equals(c.getSuit());
        Assert.assertTrue(allowed.stream().allMatch(isClubsOrHearts));
    }

    @Test
    public void firstCardNotTrumpf_butTrumpfOnTable_andIHaveOnlyLowerTrumpfs_thisTrumpfIsNotPlayed() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setMyCards(myCardsFactory.createMyCardsOnlySmallTrumpfsPlusOneDiamond());
        builder.setCardsOnTable(cardsOnTableFactory.createCardOnTableHeartSixAndClubsNine());
        builder.setTrumpf(new TrumpfSuit(Suit.CLUBS));

        Set<Card> allowed = builder.getAllowedCards();

        Assert.assertEquals(1, allowed.size());

        Card card = allowed.iterator().next();

        Assert.assertEquals(DIAMOND_QUEEN, card);
    }

    @Test
    public void firstCardNotTrumpf_butTrumpfOnTable_andIHaveOnlyLowerTrumpfs_andMustPlayTrumpf() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setMyCards(Arrays.asList(DIAMOND_SIX, DIAMOND_EIGHT));
        builder.setTrumpf(new TrumpfSuit(Suit.DIAMONDS));
        builder.setCardsOnTable(Arrays.asList(CLUB_NINE, SPADE_EIGHT, DIAMOND_TEN));

        Set<Card> allowed = builder.getAllowedCards();

        Assert.assertEquals(2, allowed.size());

        assertThat(allowed.size(), is(2));
        assertThat(allowed, hasItem(DIAMOND_SIX));
        assertThat(allowed, hasItem(DIAMOND_EIGHT));
    }

    @Test
    public void noMatchingCardAtHand_playAnyCard() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setMyCards(myCardsFactory.createMyCardsOnlyClubsSix());
        builder.setCardsOnTable(cardsOnTableFactory.createCardOnTableHeartSixAndClubsNine());
        builder.setTrumpf(new TrumpfSuit(Suit.SPADES));

        Set<Card> allowed = builder.getAllowedCards();

        Assert.assertEquals(builder.getMyCards(), allowed);
    }

}
