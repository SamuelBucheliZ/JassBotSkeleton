package com.zuehlke.jasschallenge.client.sb.jasslogic.rules;

import com.zuehlke.jasschallenge.client.sb.jasslogic.CardsOnTableTestDataFactory;
import com.zuehlke.jasschallenge.client.sb.jasslogic.MyCardsTestDataFactory;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.Trumpf;
import com.zuehlke.jasschallenge.client.sb.model.trumpf.TrumpfSuit;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.Predicate;

import static com.zuehlke.jasschallenge.client.sb.model.cards.Card.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class AllowedCardsTest {

    private MyCardsTestDataFactory myCardsFactory = new MyCardsTestDataFactory();
    private CardsOnTableTestDataFactory cardsOnTableFactory = new CardsOnTableTestDataFactory();

    @Test
    public void noCardsOnTable_playAnyCard() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setMyCards(myCardsFactory.createMyCardsWithJackNineAndThirdCard());
        builder.setCardsOnTable(createCardsOnTableEmpty());
        builder.setTrumpf(new TrumpfSuit(Suit.CLUBS));

        Set<Card> allowed = builder.getAllowedCards();

        Assert.assertEquals(builder.getMyCards(), allowed);
    }

    @Test
    public void cardsOnTable_andIHaveCardOfSameSuit_playCardOfSameSuitOrTrumpf() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setMyCards(myCardsFactory.createMyCardsWithJackNineAndThirdCard());
        builder.setCardsOnTable(cardsOnTableFactory.create(CLUB_SIX));
        builder.setCardsOnTable(cardsOnTableFactory.create(CLUB_NINE));
        builder.setTrumpf(new TrumpfSuit(Suit.HEARTS));

        Set<Card> allowed = builder.getAllowedCards();

        Predicate<Card> isClubsOrHearts = c -> Suit.CLUBS.equals(c.getSuit()) || Suit.HEARTS.equals(c.getSuit());
        Assert.assertTrue(allowed.stream().allMatch(isClubsOrHearts));
    }

    @Test
    public void firstCardNotTrumpf_butTrumpfOnTable_andIHaveOnlyLowerTrumpfs_thisTrumpfIsNotPlayed() {
        AllowedCardsBuilder builder = new AllowedCardsBuilder();
        builder.setMyCards(createMyCardsOnlySmallTrumpfsPlusOneDiamond());
        builder.setCardsOnTable(createCardOnTableHeartSixAndClubsNine());
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
        builder.setMyCards(createMyCardsOnlyClubsSix());
        builder.setCardsOnTable(createCardOnTableHeartSixAndClubsNine());
        builder.setTrumpf(createTrumpfSpades());

        Set<Card> allowed = builder.getAllowedCards();

        Assert.assertEquals(builder.getMyCards(), allowed);
    }


    private Trumpf createTrumpfSpades() {
        return new TrumpfSuit(Suit.SPADES);
    }

    private List<Card> createCardsOnTableEmpty() {
        return new LinkedList<>();
    }


    private List<Card> createCardOnTableHeartSixAndClubsNine() {
        List<Card> cards = new LinkedList<>();
        cards.add(HEART_SIX);
        cards.add(CLUB_NINE);
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


    private Set<Card> createMyCardsOnlyClubsSix() {
        Set<Card> cards = new HashSet<>();

        cards.add(CLUB_SIX);

        return cards;
    }

}
