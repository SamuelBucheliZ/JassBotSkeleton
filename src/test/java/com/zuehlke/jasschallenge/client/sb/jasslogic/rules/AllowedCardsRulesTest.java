package com.zuehlke.jasschallenge.client.sb.jasslogic.rules;

import com.zuehlke.jasschallenge.client.sb.jasslogic.CardsOnTableTestDataFactory;
import com.zuehlke.jasschallenge.client.sb.jasslogic.MyCardsTestDataFactory;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfSuit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfUndeufe;
import org.junit.Assert;
import org.junit.Test;

import static com.zuehlke.jasschallenge.client.sb.model.cards.Card.*;

import java.util.*;

public class AllowedCardsRulesTest {

    private MyCardsTestDataFactory myCardsFactory = new MyCardsTestDataFactory();
    private CardsOnTableTestDataFactory cardsOnTableFactory = new CardsOnTableTestDataFactory();

    @Test
    public void getAllowedCardsToPlay_noCardsOnTable_allCardsAreAllowed() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setTrumpf(new TrumpfSuit(Suit.CLUBS));
        builder.setMyCards(myCardsFactory.createMyCardsWithJackNineAndThirdCard());
        builder.setCardsOnTable(createCardsOnTable());

        Set<Card> allowedCardsToPlay = builder.getAllowedCards();

        Assert.assertEquals(builder.getMyCards(), allowedCardsToPlay);
    }

    @Test
    public void getAllowedCardsToPlay_oneCardPlayed_andIHaveThisSuit_onlyCardsOfThisSuitAndTrumpfsAreAllowed() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setTrumpf(new TrumpfSuit(Suit.CLUBS));
        builder.setMyCards(myCardsFactory.createMyCardsWithJackNineAndThirdCard());
        builder.setCardsOnTable(createCardsOnTable(HEART_SIX));

        Set<Card> allowedCardsToPlay = builder.getAllowedCards();

        Assert.assertEquals(6, allowedCardsToPlay.size());
        Set<Card> expectedAllowedCardsToPlay = new HashSet<>(Arrays.asList(builder.getMyCards().stream().filter(card -> card.getSuit().equals(Suit.HEARTS) || card.getSuit().equals(builder.getTrumpf().getSuit())).toArray(Card[]::new)));
        Assert.assertEquals(expectedAllowedCardsToPlay, allowedCardsToPlay);
    }

    @Test
    public void getAllowedCardsToPlay_trumpfOnTableButNotFirst_onlySmallerTrumpfsInHandPlusOtherCards_trumpfIsNotPlayed() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setTrumpf(new TrumpfSuit(Suit.CLUBS));
        builder.setMyCards(createMyCardsOnlySmallTrumpfsPlusOneDiamond());
        builder.setCardsOnTable(createCardOnTableHeartSixAndClubsNine());

        Set<Card> allowedCardsToPlay = builder.getAllowedCards();

        Assert.assertEquals(1, allowedCardsToPlay.size());
        Set<Card> expectedAllowedCardsToPlay = new HashSet<>(Arrays.asList(new Card[]{DIAMOND_QUEEN}));
        Assert.assertEquals(expectedAllowedCardsToPlay, allowedCardsToPlay);
    }

    @Test
    public void getAllowedCardsToPlay_undeufe_heartOnTable_heartInHand_thenHeartIsPlayed() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setTrumpf(new TrumpfUndeufe());
        builder.setMyCards(createMyCardsWithOneHeart());
        builder.setCardsOnTable(createCardOnTableHeartSix());

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
        builder.setCardsOnTable(cardsOnTableFactory.create(HEART_QUEEN, HEART_KING, DIAMOND_JACK));
        builder.setMyCards(myCardsFactory.create(DIAMOND_QUEEN, HEART_JACK, CLUB_EIGHT));

        Set<Card> allowedCardsToPlay = builder.getAllowedCards();

        Assert.assertEquals(1, allowedCardsToPlay.size());
        Assert.assertEquals(allowedCardsToPlay.toArray()[0], HEART_JACK);
    }

    @Test
    public void getAllowedCardsToPlay_trumpfIsPlayed_andIHaveOnlyAweakerTrumpf_iStillHaveToPlayIt() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setTrumpf(new TrumpfSuit(Suit.SPADES));
        builder.setCardsOnTable(cardsOnTableFactory.create(SPADE_JACK, CLUB_TEN));
        builder.setMyCards(myCardsFactory.create(DIAMOND_EIGHT, SPADE_SIX));

        Set<Card> allowedCardsToPlay = builder.getAllowedCards();

        Assert.assertEquals(1, allowedCardsToPlay.size());
        Assert.assertEquals(allowedCardsToPlay.toArray()[0], SPADE_SIX);
    }

    private Set<Card> createMyCardsWithOneHeart() {
        Set<Card> cards = new HashSet<>();

        cards.add(SPADE_NINE);
        cards.add(HEART_JACK);
        cards.add(CLUB_SEVEN);
        cards.add(CLUB_KING);
        cards.add(DIAMOND_EIGHT);
        cards.add(DIAMOND_KING);
        cards.add(CLUB_NINE);
        cards.add(SPADE_SEVEN);
        cards.add(CLUB_QUEEN);

        return cards;
    }

    private Set<Card> createMyCardsOnlySmallTrumpfsPlusOneDiamond() {
        Set<Card> cards = new HashSet<>();

        cards.add(CLUB_SIX);
        cards.add(CLUB_SEVEN);
        cards.add(CLUB_EIGHT);
        cards.add(DIAMOND_QUEEN);

        return cards;
    }

    private List<Card> createCardsOnTable(Card... cards) {
        return Arrays.asList(cards);
    }

    private List<Card> createCardOnTableHeartSix() {
        List<Card> cards = new LinkedList<>();
        cards.add(HEART_SIX);
        return cards;
    }

    private List<Card> createCardOnTableHeartSixAndClubsNine() {
        List<Card> cards = new LinkedList<>();
        cards.add(HEART_SIX);
        cards.add(CLUB_NINE);
        return cards;
    }

}
