package com.zuehlke.jasschallenge.client.sb.game;

import com.zuehlke.jasschallenge.client.sb.jasslogic.CardsOnTableTestDataFactory;
import com.zuehlke.jasschallenge.client.sb.jasslogic.MyCardsTestDataFactory;
import com.zuehlke.jasschallenge.client.sb.model.cards.CardNumber;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfSuit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfUndeufe;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class GameStateTest {

    private MyCardsTestDataFactory myCardsFactory = new MyCardsTestDataFactory();
    private CardsOnTableTestDataFactory cardsOnTableFactory = new CardsOnTableTestDataFactory();

    @Test
    public void getAllowedCardsToPlay_noCardsOnTable_allCardsAreAllowed() {
        GameState gameState = new GameState();
        gameState.setTrumpf(new TrumpfSuit(Suit.CLUBS));
        gameState.setMyCards(createMyCards());
        gameState.setCardsOnTable(createCardsOnTable());

        Set<Card> allowedCardsToPlay = gameState.getAllowedCardsToPlay();

        Assert.assertEquals(gameState.getMyCards(), allowedCardsToPlay);
    }

    @Test
    public void getAllowedCardsToPlay_oneCardPlayed_andIHaveThisSuit_onlyCardsOfThisSuitAndTrumpfsAreAllowed() {
        GameState gameState = new GameState();
        gameState.setTrumpf(new TrumpfSuit(Suit.CLUBS));
        gameState.setMyCards(createMyCards());
        gameState.setCardsOnTable(createCardsOnTable(new Card(Suit.HEARTS, CardNumber.valueOf(6))));

        Set<Card> allowedCardsToPlay = gameState.getAllowedCardsToPlay();

        Assert.assertEquals(6, allowedCardsToPlay.size());
        Set<Card> expectedAllowedCardsToPlay = new HashSet<>(Arrays.asList(gameState.getMyCards().stream().filter(card -> card.getSuit().equals(Suit.HEARTS) || card.getSuit().equals(gameState.getTrumpf().getSuit())).toArray(Card[]::new)));
        Assert.assertEquals(expectedAllowedCardsToPlay, allowedCardsToPlay);
    }

    @Test
    public void getAllowedCardsToPlay_trumpfOnTableButNotFirst_onlySmallerTrumpfsInHandPlusOtherCards_trumpfIsNotPlayed() {
        GameState gameState = new GameState();
        gameState.setTrumpf(new TrumpfSuit(Suit.CLUBS));
        gameState.setMyCards(createMyCardsOnlySmallTrumpfsPlusOneDiamond());
        gameState.setCardsOnTable(createCardOnTableHeartSixAndClubsNine());

        Set<Card> allowedCardsToPlay = gameState.getAllowedCardsToPlay();

        Assert.assertEquals(1, allowedCardsToPlay.size());
        Set<Card> expectedAllowedCardsToPlay = new HashSet<>(Arrays.asList(new Card[]{new Card(Suit.DIAMONDS, CardNumber.valueOf(12))}));
        Assert.assertEquals(expectedAllowedCardsToPlay, allowedCardsToPlay);
    }

    @Test
    public void getAllowedCardsToPlay_undeufe_heartOnTable_heartInHand_thenHeartIsPlayed() {
        GameState gameState = new GameState();
        gameState.setTrumpf(new TrumpfUndeufe());
        gameState.setMyCards(createMyCardsWithOneHeart());
        gameState.setCardsOnTable(createCardOnTableHeartSix());

        Set<Card> allowedCardsToPlay = gameState.getAllowedCardsToPlay();

        Assert.assertEquals(1, allowedCardsToPlay.size());
        Set<Card> expectedAllowedCardsToPlay = new HashSet<>(Arrays.asList(new Card[]{new Card(Suit.HEARTS, CardNumber.valueOf(11))}));
        Assert.assertEquals(expectedAllowedCardsToPlay, allowedCardsToPlay);
    }

    @Test
    public void getAllowedCardsToPlay_threeNonTrumpfsPlayed_iHaveATrumpfAndThreeMatchingNonTrumpfs_theyAreAllReturned() {
        GameState gameState = new GameState();
        gameState.setMyCards(myCardsFactory.createMyCardsOneHeartThreeSpadesOneClubs());
        gameState.setCardsOnTable(cardsOnTableFactory.createCardsOnTableThreeSpades());
        gameState.setTrumpf(new TrumpfSuit(Suit.HEARTS));
        gameState.setIMadeTrumpf(false);
        gameState.setRound(4);

        Set<Card> allowedCardsToPlay = gameState.getAllowedCardsToPlay();

        Assert.assertEquals(4, allowedCardsToPlay.size());
        Assert.assertTrue(allowedCardsToPlay.contains(new Card(Suit.SPADES, CardNumber.valueOf(6))));
        Assert.assertTrue(allowedCardsToPlay.contains(new Card(Suit.SPADES, CardNumber.valueOf(11))));
        Assert.assertTrue(allowedCardsToPlay.contains(new Card(Suit.SPADES, CardNumber.valueOf(7))));
        Assert.assertTrue(allowedCardsToPlay.contains(new Card(Suit.HEARTS, CardNumber.valueOf(10))));
    }

    @Test
    public void getAllowedCardsToPlay_thirdCardOnTableIsTrumpf_iHaveATrumpfAndThreeMatchingNonTrumpfs_myOnlyTrumpfIsLower_onlyNonTrumpfsAreReturned() {
        GameState gameState = new GameState();
        gameState.setTrumpf(new TrumpfSuit(Suit.DIAMONDS));
        gameState.setCardsOnTable(cardsOnTableFactory.create(new Card(Suit.HEARTS, CardNumber.valueOf(12)), new Card(Suit.HEARTS, CardNumber.valueOf(13)), new Card(Suit.DIAMONDS, CardNumber.valueOf(11))));
        gameState.setMyCards(myCardsFactory.create(new Card(Suit.DIAMONDS, CardNumber.valueOf(12)), new Card(Suit.HEARTS, CardNumber.valueOf(11)), new Card(Suit.CLUBS, CardNumber.valueOf(8))));
        gameState.setIMadeTrumpf(false);

        Set<Card> allowedCardsToPlay = gameState.getAllowedCardsToPlay();

        Assert.assertEquals(1, allowedCardsToPlay.size());
        Assert.assertEquals(allowedCardsToPlay.toArray()[0], new Card(Suit.HEARTS, CardNumber.valueOf(11)));
    }

    @Test
    public void getAllowedCardsToPlay_trumpfIsPlayed_andIHaveOnlyAweakerTrumpf_iStillHaveToPlayIt() {
        GameState gameState = new GameState();
        gameState.setTrumpf(new TrumpfSuit(Suit.SPADES));
        gameState.setCardsOnTable(cardsOnTableFactory.create(new Card(Suit.SPADES, CardNumber.valueOf(11)), new Card(Suit.CLUBS, CardNumber.valueOf(10))));
        gameState.setMyCards(myCardsFactory.create(new Card(Suit.DIAMONDS, CardNumber.valueOf(8)), new Card(Suit.SPADES, CardNumber.valueOf(6))));
        gameState.setIMadeTrumpf(false);

        Set<Card> allowedCardsToPlay = gameState.getAllowedCardsToPlay();

        Assert.assertEquals(1, allowedCardsToPlay.size());
        Assert.assertEquals(allowedCardsToPlay.toArray()[0], new Card(Suit.SPADES, CardNumber.valueOf(6)));
    }

    private Set<Card> createMyCardsWithOneHeart() {
        Set<Card> cards = new HashSet<>();

        cards.add(new Card(Suit.SPADES, CardNumber.valueOf(9)));
        cards.add(new Card(Suit.HEARTS, CardNumber.valueOf(11)));
        cards.add(new Card(Suit.CLUBS, CardNumber.valueOf(7)));
        cards.add(new Card(Suit.CLUBS, CardNumber.valueOf(13)));
        cards.add(new Card(Suit.DIAMONDS, CardNumber.valueOf(8)));
        cards.add(new Card(Suit.DIAMONDS, CardNumber.valueOf(14)));
        cards.add(new Card(Suit.CLUBS, CardNumber.valueOf(9)));
        cards.add(new Card(Suit.SPADES, CardNumber.valueOf(7)));
        cards.add(new Card(Suit.CLUBS, CardNumber.valueOf(12)));

        return cards;
    }

    private Set<Card> createMyCards() {
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

    private Set<Card> createMyCardsOnlySmallTrumpfsPlusOneDiamond() {
        Set<Card> cards = new HashSet<>();

        cards.add(new Card(Suit.CLUBS, CardNumber.valueOf(6)));
        cards.add(new Card(Suit.CLUBS, CardNumber.valueOf(7)));
        cards.add(new Card(Suit.CLUBS, CardNumber.valueOf(8)));
        cards.add(new Card(Suit.DIAMONDS, CardNumber.valueOf(12)));

        return cards;
    }

    private List<Card> createCardsOnTable(Card... cards) {
        return Arrays.asList(cards);
    }

    private List<Card> createCardsOnTableEmpty() {
        return new LinkedList<>();
    }


    private List<Card> createCardOnTableHeartSix() {
        List<Card> cards = new LinkedList<>();
        cards.add(new Card(Suit.HEARTS, CardNumber.valueOf(7)));
        return cards;
    }

    private List<Card> createCardOnTableHeartSixAndClubsNine() {
        List<Card> cards = new LinkedList<>();
        cards.add(new Card(Suit.HEARTS, CardNumber.valueOf(6)));
        cards.add(new Card(Suit.CLUBS, CardNumber.valueOf(9)));
        return cards;
    }

}