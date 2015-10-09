package com.zuehlke.jasschallenge.client.sb.jasslogic;

import com.zuehlke.jasschallenge.client.sb.game.GameState;
import com.zuehlke.jasschallenge.client.sb.model.card.Card;
import com.zuehlke.jasschallenge.client.sb.model.card.CardNumber;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.model.card.Suit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfSuit;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CardChooserTest {

    private MyCardsTestDataFactory myCardsFactory = new MyCardsTestDataFactory();
    private CardsOnTableTestDataFactory cardsOnTableFactory = new CardsOnTableTestDataFactory();
    private CardChooser cardChooser = new CardChooser();

    @Test
    public void noCardsOnTable_playFirstCardOfList() {
        GameState gameState = new GameState();
        gameState.setMyCards(createMyCards());
        gameState.setCardsOnTable(createCardsOnTableEmpty());
        gameState.setTrumpf(new TrumpfSuit(Suit.CLUBS));

        Card chosenCard = cardChooser.chooseCard(gameState);

        Assert.assertNotNull(chosenCard);
        Assert.assertEquals(gameState.getMyCards().iterator().next(), chosenCard);
    }

    @Test
    public void cardsOnTable_andIHaveCardOfSameSuit_playCardOfSameSuit() {
        GameState gameState = new GameState();
        gameState.setMyCards(createMyCards());
        gameState.setCardsOnTable(cardsOnTableFactory.create(new Card(Suit.CLUBS, CardNumber.valueOf(9))));
        gameState.setTrumpf(new TrumpfSuit(Suit.HEARTS));

        Card chosenCard = cardChooser.chooseCard(gameState);

        Assert.assertNotNull(chosenCard);
        //Assert.assertEquals(Suit.CLUBS, chosenCard.getSuit());
        Assert.assertTrue(Suit.CLUBS.equals(chosenCard.getSuit()) || Suit.HEARTS.equals(chosenCard.getSuit()));
        Assert.assertTrue(gameState.getMyCards().contains(chosenCard));
    }

    @Test
    public void firstCardNotTrumpf_butTrumpfOnTable_andIHaveOnlyLowerTrumpfs_thisTrumpfIsNotPlayed() {
        GameState gameState = new GameState();
        gameState.setMyCards(createMyCardsOnlySmallTrumpfsPlusOneDiamond());
        gameState.setCardsOnTable(createCardOnTableHeartSixAndClubsNine());
        gameState.setTrumpf(new TrumpfSuit(Suit.CLUBS));

        Card chosenCard = cardChooser.chooseCard(gameState);

        Assert.assertNotNull(chosenCard);
        Assert.assertEquals(new Card(Suit.DIAMONDS, CardNumber.valueOf(12)), chosenCard);
        Assert.assertTrue(gameState.getMyCards().contains(chosenCard));
    }

    @Test
    public void noMatchingCardAtHand_playAnyCard() {
        GameState gameState = new GameState();
        gameState.setMyCards(createMyCardsOnlyClubsSix());
        gameState.setCardsOnTable(createCardOnTableHeartSixAndClubsNine());
        gameState.setTrumpf(createTrumpfSpades());

        Card chosenCard = cardChooser.chooseCard(gameState);

        Assert.assertNotNull(chosenCard);
        Assert.assertEquals(new Card(Suit.CLUBS, CardNumber.valueOf(6)), chosenCard);
    }

    private Trumpf createTrumpfSpades() {
        return new TrumpfSuit(Suit.SPADES);
    }

    private List<Card> createCardsOnTableEmpty() {
        return new LinkedList<>();
    }

    private List<Card> createCardOnTableClubsNine() {
        List<Card> cards = new LinkedList<>();
        cards.add(new Card(Suit.CLUBS, CardNumber.valueOf(9)));
        return cards;
    }

    private List<Card> createCardOnTableHeartSixAndClubsNine() {
        List<Card> cards = new LinkedList<>();
        cards.add(new Card(Suit.HEARTS, CardNumber.valueOf(6)));
        cards.add(new Card(Suit.CLUBS, CardNumber.valueOf(9)));
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


    private Set<Card> createMyCardsOnlyClubsSix() {
        Set<Card> cards = new HashSet<>();

        cards.add(new Card(Suit.CLUBS, CardNumber.valueOf(6)));

        return cards;
    }

}
