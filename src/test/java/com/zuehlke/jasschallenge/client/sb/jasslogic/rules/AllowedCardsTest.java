package com.zuehlke.jasschallenge.client.sb.jasslogic.rules;

import com.zuehlke.jasschallenge.client.sb.game.GameState;
import com.zuehlke.jasschallenge.client.sb.game.GameStateBuilder;
import com.zuehlke.jasschallenge.client.sb.jasslogic.CardsOnTableTestDataFactory;
import com.zuehlke.jasschallenge.client.sb.jasslogic.MyCardsTestDataFactory;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.CardNumber;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfSuit;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class AllowedCardsTest {

    private MyCardsTestDataFactory myCardsFactory = new MyCardsTestDataFactory();
    private CardsOnTableTestDataFactory cardsOnTableFactory = new CardsOnTableTestDataFactory();

    @Test
    public void noCardsOnTable_playAnyCard() {
        GameStateBuilder builder = new GameStateBuilder();
        builder.setMyCards(createMyCards());
        builder.setCardsOnTable(createCardsOnTableEmpty());
        builder.setTrumpf(new TrumpfSuit(Suit.CLUBS));
        GameState gameState = builder.build();

        AllowedCards allowed = AllowedCardsRules.getFor(gameState);

        Assert.assertEquals(gameState.getMyCards(), allowed.get());
    }

    @Test
    public void cardsOnTable_andIHaveCardOfSameSuit_playCardOfSameSuitOrTrumpf() {
        GameStateBuilder builder = new GameStateBuilder();
        builder.setMyCards(createMyCards());
        builder.setCardsOnTable(cardsOnTableFactory.create(new Card(Suit.CLUBS, CardNumber.valueOf(9))));
        builder.setTrumpf(new TrumpfSuit(Suit.HEARTS));
        GameState gameState = builder.build();

        AllowedCards allowed = AllowedCardsRules.getFor(gameState);

        Predicate<Card> isClubsOrHearts = c -> Suit.CLUBS.equals(c.getSuit()) || Suit.HEARTS.equals(c.getSuit());
        Assert.assertTrue(allowed.get().stream().allMatch(isClubsOrHearts));
    }

    @Test
    public void firstCardNotTrumpf_butTrumpfOnTable_andIHaveOnlyLowerTrumpfs_thisTrumpfIsNotPlayed() {
        GameStateBuilder builder = new GameStateBuilder();
        builder.setMyCards(createMyCardsOnlySmallTrumpfsPlusOneDiamond());
        builder.setCardsOnTable(createCardOnTableHeartSixAndClubsNine());
        builder.setTrumpf(new TrumpfSuit(Suit.CLUBS));
        GameState gameState = builder.build();

        AllowedCards allowed = AllowedCardsRules.getFor(gameState);

        Assert.assertEquals(1, allowed.get().size());

        Card card = allowed.get().iterator().next();

        Assert.assertEquals(new Card(Suit.DIAMONDS, CardNumber.QUEEN), card);
    }

    @Test
    public void noMatchingCardAtHand_playAnyCard() {
        GameStateBuilder builder = new GameStateBuilder();
        builder.setMyCards(createMyCardsOnlyClubsSix());
        builder.setCardsOnTable(createCardOnTableHeartSixAndClubsNine());
        builder.setTrumpf(createTrumpfSpades());
        GameState gameState = builder.build();

        AllowedCards allowed = AllowedCardsRules.getFor(gameState);

        Assert.assertEquals(gameState.getMyCards(), allowed.get());
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
