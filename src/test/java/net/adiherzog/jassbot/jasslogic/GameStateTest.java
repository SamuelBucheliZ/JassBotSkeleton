package net.adiherzog.jassbot.jasslogic;

import net.adiherzog.jassbot.model.Card;
import net.adiherzog.jassbot.model.Suit;
import net.adiherzog.jassbot.model.Trumpf;
import net.adiherzog.jassbot.model.TrumpfMode;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class GameStateTest {

    private MyCardsTestDataFactory myCardsFactory = new MyCardsTestDataFactory();
    private CardsOnTableTestDataFactory cardsOnTableFactory = new CardsOnTableTestDataFactory();

    @Test
    public void getAllowedCardsToPlay_noCardsOnTable_allCardsAreAllowed() {
        GameState gameState = new GameState();
        gameState.setTrumpf(new Trumpf(TrumpfMode.TRUMPF, Suit.CLUBS));
        gameState.setMyCards(createMyCards());
        gameState.setCardsOnTable(createCardsOnTable());

        Set<Card> allowedCardsToPlay = gameState.getAllowedCardsToPlay();

        Assert.assertEquals(gameState.getMyCards(), allowedCardsToPlay);
    }

    @Test
    public void getAllowedCardsToPlay_oneCardPlayed_andIHaveThisSuit_onlyCardsOfThisSuitAndTrumpfsAreAllowed() {
        GameState gameState = new GameState();
        gameState.setTrumpf(new Trumpf(TrumpfMode.TRUMPF, Suit.CLUBS));
        gameState.setMyCards(createMyCards());
        gameState.setCardsOnTable(createCardsOnTable(new Card(Suit.HEARTS, 6)));

        Set<Card> allowedCardsToPlay = gameState.getAllowedCardsToPlay();

        Assert.assertEquals(6, allowedCardsToPlay.size());
        Set<Card> expectedAllowedCardsToPlay = new HashSet<Card>(Arrays.asList(gameState.getMyCards().stream().filter(card -> card.getSuit().equals(Suit.HEARTS) || card.getSuit().equals(gameState.getTrumpf().getSuit())).toArray(Card[]::new)));
        Assert.assertEquals(expectedAllowedCardsToPlay, allowedCardsToPlay);
    }

    @Test
    public void getAllowedCardsToPlay_trumpfOnTableButNotFirst_onlySmallerTrumpfsInHandPlusOtherCards_trumpfIsNotPlayed() {
        GameState gameState = new GameState();
        gameState.setTrumpf(new Trumpf(TrumpfMode.TRUMPF, Suit.CLUBS));
        gameState.setMyCards(createMyCardsOnlySmallTrumpfsPlusOneDiamond());
        gameState.setCardsOnTable(createCardOnTableHeartSixAndClubsNine());

        Set<Card> allowedCardsToPlay = gameState.getAllowedCardsToPlay();

        Assert.assertEquals(1, allowedCardsToPlay.size());
        Set<Card> expectedAllowedCardsToPlay = new HashSet<Card>(Arrays.asList(new Card[]{new Card(Suit.DIAMONDS, 12)}));
        Assert.assertEquals(expectedAllowedCardsToPlay, allowedCardsToPlay);
    }

    @Test
    public void getAllowedCardsToPlay_undeufe_heartOnTable_heartInHand_thenHeartIsPlayed() {
        GameState gameState = new GameState();
        gameState.setTrumpf(new Trumpf(TrumpfMode.UNDEUFE));
        gameState.setMyCards(createMyCardsWithOneHeart());
        gameState.setCardsOnTable(createCardOnTableHeartSix());

        Set<Card> allowedCardsToPlay = gameState.getAllowedCardsToPlay();

        Assert.assertEquals(1, allowedCardsToPlay.size());
        Set<Card> expectedAllowedCardsToPlay = new HashSet<Card>(Arrays.asList(new Card[]{new Card(Suit.HEARTS, 11)}));
        Assert.assertEquals(expectedAllowedCardsToPlay, allowedCardsToPlay);
    }

    @Test
    public void getSafeTricks_threeNonTrumpfsOnTable_iHaveATrumpf_thisTrumpfIsReturned() {
        GameState gameState = new GameState();
        gameState.setMyCards(myCardsFactory.createMyCardsOneHeartThreeSpadesOneClubs());
        gameState.setCardsOnTable(cardsOnTableFactory.createCardsOnTableThreeSpades());
        gameState.setTrumpf(new Trumpf(TrumpfMode.TRUMPF, Suit.HEARTS));
        gameState.setIMadeTrumpf(false);
        gameState.setRound(4);

        Card[] safeTricks = gameState.getSafeTricks().toArray(Card[]::new);

        Assert.assertEquals(1, safeTricks.length);
        Assert.assertEquals(new Card(Suit.HEARTS, 10), safeTricks[0]);
    }

    @Test
    public void getAllowedCardsToPlay_threeNonTrumpfsPlayed_iHaveATrumpfAndThreeMatchingNonTrumpfs_theyAreAllReturned() {
        GameState gameState = new GameState();
        gameState.setMyCards(myCardsFactory.createMyCardsOneHeartThreeSpadesOneClubs());
        gameState.setCardsOnTable(cardsOnTableFactory.createCardsOnTableThreeSpades());
        gameState.setTrumpf(new Trumpf(TrumpfMode.TRUMPF, Suit.HEARTS));
        gameState.setIMadeTrumpf(false);
        gameState.setRound(4);

        Set<Card> allowedCardsToPlay = gameState.getAllowedCardsToPlay();

        Assert.assertEquals(4, allowedCardsToPlay.size());
        Assert.assertTrue(allowedCardsToPlay.contains(new Card(Suit.SPADES, 6)));
        Assert.assertTrue(allowedCardsToPlay.contains(new Card(Suit.SPADES, 11)));
        Assert.assertTrue(allowedCardsToPlay.contains(new Card(Suit.SPADES, 7)));
        Assert.assertTrue(allowedCardsToPlay.contains(new Card(Suit.HEARTS, 10)));
    }

    @Test
    public void getAllowedCardsToPlay_thirdCardOnTableIsTrumpf_iHaveATrumpfAndThreeMatchingNonTrumpfs_myOnlyTrumpfIsLower_onlyNonTrumpfsAreReturned() {
        GameState gameState = new GameState();
        gameState.setTrumpf(new Trumpf(TrumpfMode.TRUMPF, Suit.DIAMONDS));
        gameState.setCardsOnTable(cardsOnTableFactory.create(new Card(Suit.HEARTS, 12), new Card(Suit.HEARTS, 13), new Card(Suit.DIAMONDS, 11)));
        gameState.setMyCards(myCardsFactory.create(new Card(Suit.DIAMONDS, 12), new Card(Suit.HEARTS, 11), new Card(Suit.CLUBS, 8)));
        gameState.setIMadeTrumpf(false);

        Set<Card> allowedCardsToPlay = gameState.getAllowedCardsToPlay();

        Assert.assertEquals(1, allowedCardsToPlay.size());
        Assert.assertEquals(allowedCardsToPlay.toArray()[0], new Card(Suit.HEARTS, 11));
    }

    @Test
    public void getAllowedCardsToPlay_trumpfIsPlayed_andIHaveOnlyAweakerTrumpf_iStillHaveToPlayIt() {
        GameState gameState = new GameState();
        gameState.setTrumpf(new Trumpf(TrumpfMode.TRUMPF, Suit.SPADES));
        gameState.setCardsOnTable(cardsOnTableFactory.create(new Card(Suit.SPADES, 11), new Card(Suit.CLUBS, 10)));
        gameState.setMyCards(myCardsFactory.create(new Card(Suit.DIAMONDS, 8), new Card(Suit.SPADES, 6)));
        gameState.setIMadeTrumpf(false);

        Set<Card> allowedCardsToPlay = gameState.getAllowedCardsToPlay();

        Assert.assertEquals(1, allowedCardsToPlay.size());
        Assert.assertEquals(allowedCardsToPlay.toArray()[0], new Card(Suit.SPADES, 6));
    }

    private Set<Card> createMyCardsWithOneHeart() {
        Set<Card> cards = new HashSet<>();

        cards.add(new Card(Suit.SPADES, 9));
        cards.add(new Card(Suit.HEARTS, 11));
        cards.add(new Card(Suit.CLUBS, 7));
        cards.add(new Card(Suit.CLUBS, 13));
        cards.add(new Card(Suit.DIAMONDS, 8));
        cards.add(new Card(Suit.DIAMONDS, 14));
        cards.add(new Card(Suit.CLUBS, 9));
        cards.add(new Card(Suit.SPADES, 7));
        cards.add(new Card(Suit.CLUBS, 12));

        return cards;
    }

    private Set<Card> createMyCards() {
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

    private Set<Card> createMyCardsOnlySmallTrumpfsPlusOneDiamond() {
        Set<Card> cards = new HashSet<>();

        cards.add(new Card(Suit.CLUBS, 6));
        cards.add(new Card(Suit.CLUBS, 7));
        cards.add(new Card(Suit.CLUBS, 8));
        cards.add(new Card(Suit.DIAMONDS, 12));

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
        cards.add(new Card(Suit.HEARTS, 7));
        return cards;
    }

    private List<Card> createCardOnTableHeartSixAndClubsNine() {
        List<Card> cards = new LinkedList<>();
        cards.add(new Card(Suit.HEARTS, 6));
        cards.add(new Card(Suit.CLUBS, 9));
        return cards;
    }

}
