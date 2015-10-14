package com.zuehlke.jasschallenge.client.sb.jasslogic.rules;

import com.zuehlke.jasschallenge.client.sb.model.cards.CardNumber;
import com.zuehlke.jasschallenge.client.sb.model.cards.Card;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.zuehlke.jasschallenge.client.sb.model.cards.Card.*;

public class MyCardsTestDataFactory {

    public Set<Card> createMyCardsOneHeartThreeSpadesOneClubs() {
        Set<Card> cards = new HashSet<>();

        cards.add(SPADE_SIX);
        cards.add(SPADE_JACK);
        cards.add(CLUB_KING);
        cards.add(SPADE_SEVEN);
        cards.add(HEART_TEN);

        return cards;
    }

    public Set<Card> createMyCardsWithJackNineAndThirdCard() {
        Set<Card> cards = new HashSet<>();

        cards.add(HEART_NINE);
        cards.add(HEART_JACK);
        cards.add(HEART_SEVEN);
        cards.add(CLUB_SIX);
        cards.add(CLUB_SEVEN);
        cards.add(CLUB_EIGHT);
        cards.add(DIAMOND_QUEEN);
        cards.add(DIAMOND_KING);
        cards.add(DIAMOND_ACE);

        return cards;
    }

    public Set<Card> createMyCardsOnlySmallTrumpfsPlusOneDiamond() {
        Set<Card> cards = new HashSet<>();

        cards.add(CLUB_SIX);
        cards.add(CLUB_SEVEN);
        cards.add(CLUB_EIGHT);
        cards.add(DIAMOND_QUEEN);

        return cards;
    }

    public Set<Card> createMyCardsOnlyClubsSix() {
        Set<Card> cards = new HashSet<>();

        cards.add(CLUB_SIX);

        return cards;
    }

    public Set<Card> createMyCardsWithOneHeart() {
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

    public Set<Card> createMyCards(Card... cards) {
        Set<Card> myCards = new HashSet<>();
        myCards.addAll(Arrays.asList(cards));
        return myCards;
    }


}
