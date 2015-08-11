package net.adiherzog.jassbot.jasslogic;

import com.google.common.base.Preconditions;
import net.adiherzog.jassbot.model.Card;
import net.adiherzog.jassbot.model.Suit;
import net.adiherzog.jassbot.model.Trumpf;

import java.util.*;
import org.apache.log4j.Logger;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameState {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private Set<Card> myCards = new HashSet<>();
    private Trumpf trumpf;
    private List<Card> cardsOnTable;
    private boolean iMadeTrumpf = false;
    private int round = 0;

    public Set<Card> getMyCards() {
        return myCards;
    }

    public void setMyCards(Set<Card> myCards) {
        this.myCards = myCards;
    }

    public Trumpf getTrumpf() {
        return trumpf;
    }

    public void setTrumpf(Trumpf trumpf) {
        this.trumpf = trumpf;
    }

    public List<Card> getCardsOnTable() {
        return cardsOnTable;
    }

    public void setCardsOnTable(List<Card> cardsOnTable) {
        this.cardsOnTable = cardsOnTable;
    }

    public boolean isIMadeTrumpf() {
        return iMadeTrumpf;
    }

    public void setIMadeTrumpf(boolean iMadeTrumpf) {
        this.iMadeTrumpf = iMadeTrumpf;
    }

    public void setRound(int round) {
        this.round = round;
    }


    public void startNextRound() {
        round++;
    }

    public void checkIsValid() {
        Preconditions.checkNotNull(cardsOnTable);
        Preconditions.checkNotNull(myCards);
        Preconditions.checkNotNull(trumpf);
        Preconditions.checkArgument(round >= 0 && round <= 8);
    }

    public void resetAfterGameRound() {
        myCards = new HashSet<>();
        trumpf = null;
        cardsOnTable = null;
        iMadeTrumpf = false;
        round = 0;
    }

    public Set<Card> getAllowedCardsToPlay() {
        if (cardsOnTable.size() == 0) {
            return myCards;
        }

        Stream<Card> nonTrumpfsThatMatchFirstCard = getNonTrumpfCardsThatSuitFirstCardOnTable();
        Stream<Card> allowedTrumpfs = getMyTrumpfsThatDoNotUndertrumpf();
        Set<Card> nonTrumpfsThatMatchFirstCardPlusAllowedTrumpfs = toSet(Stream.concat(nonTrumpfsThatMatchFirstCard, allowedTrumpfs));

        Set<Card> myTrumpfs = toSet(getMyTrumpfs());

        Set<Card> nonTrumpfCardsThatDoNotMatchFirstCardOnTable = toSet(getNonTrumpfCardsThatDoNotMatchFirstCardOnTable());

        if(nonTrumpfsThatMatchFirstCardPlusAllowedTrumpfs.size() > 0) {
            return nonTrumpfsThatMatchFirstCardPlusAllowedTrumpfs;
        } else if (isFirstCardTrumpf() && myTrumpfs.size() > 0) {
            return myTrumpfs;
        } else if (nonTrumpfCardsThatDoNotMatchFirstCardOnTable.size() > 0) {
            return nonTrumpfCardsThatDoNotMatchFirstCardOnTable;
        } else {
            return myCards;
        }
    }

    private Stream<Card> getNonTrumpfCardsThatDoNotMatchFirstCardOnTable() {
        if(cardsOnTable.size() == 0) {
            return getMyNonTrumpfs();
        }

        return getMyNonTrumpfs().filter(card -> !card.getSuit().equals(cardsOnTable.get(0).getSuit()));
    }

    private Stream<Card> getNonTrumpfCardsThatSuitFirstCardOnTable() {
        return getCardsThatSuitFirstCardOnTable().filter(card -> !card.isTrumpf(trumpf));
    }

    private Set<Card> toSet(Stream<Card> cardStream) {
        return cardStream.collect(Collectors.toSet());
    }

    public boolean isFirstCardTrumpf() {
        if (cardsOnTable.size() == 0) {
            return false;
        }
        return cardsOnTable.get(0).getSuit().equals(trumpf.getSuit());
    }

    private Stream<Card> getCardsThatSuitFirstCardOnTable() {
        Suit suitOfFirstCard = cardsOnTable.get(0).getSuit();
        return myCards.stream().filter(card -> card.getSuit().equals(suitOfFirstCard));
    }


    private Stream<Card> getMyTrumpfs() {
        return myCards.stream().filter(card -> card.getSuit().equals(trumpf.getSuit()));
    }

    private Stream<Card> getMyNonTrumpfs() {
        return myCards.stream().filter(card -> !card.getSuit().equals(trumpf.getSuit()));
    }

    private Stream<Card> getMyTrumpfsThatDoNotUndertrumpf() {
        return getMyCardsThatCanBeatCurrentCardsOnTable().filter(card -> card.isTrumpf(trumpf));
    }

    public Stream<Card> getSafeTricks() {
        if(getCardsOnTable().size() == 3) {
            return getMyAllowedCardsThatCanBeatCurrentCardsOnTable();
        }

        // TODO implement more logic

        return Stream.empty();
    }

    /**
     * Returns the card on the table that would make the trick if the round would finish now
     */
    private Optional<Card> getStrongestCardOnTable() {
        return getCardsOnTableThatAreAllowedToTrick().max(Card.getComperatorForTrumpf(trumpf));
    }

    /**
     * Only the cards that could actually trick if they were the highest. E.g. let's assume the first card is
     * SPADES and trumpf is HEARTS. Then HEARTS and SPADES cards that follow are allowed to trick, but CLUBS
     * and DIAMONDS are not allowed to trick, no matter how high they are.
     */
    private Stream getCardsOnTableThatAreAllowedToTrick() {
        if(cardsOnTable.size() == 0) {
            return Stream.empty();
        } else if (cardsOnTable.size() == 1) {
            return cardsOnTable.stream();
        } else {
            Stream<Card> cardsOnTableThatMatchSuitOfFirstCard = cardsOnTable.stream().filter(card -> card.getSuit().equals(cardsOnTable.get(0).getSuit()));
            Stream<Card> trumpfsOnTable = cardsOnTable.stream().filter(card -> card.isTrumpf(trumpf));
            return Stream.concat(cardsOnTableThatMatchSuitOfFirstCard, trumpfsOnTable);
        }
    }

    /**
     * Returns all cards of my hand that can beat the cards currently on the table.
     * This does not mean that I make a trick in the end, if some other player can play after me.
     */
    public Stream<Card> getMyAllowedCardsThatCanBeatCurrentCardsOnTable() {
        Optional<Card> strongestCardOnTable = getStrongestCardOnTable();
        if(!strongestCardOnTable.isPresent()) {
            return myCards.stream();
        }
        return getAllowedCardsToPlay().stream().filter(card -> card.beatsCard(strongestCardOnTable.get(), trumpf));
    }

    /**
     * Also returns cards that are not allowed!
     */
    public Stream<Card> getMyCardsThatCanBeatCurrentCardsOnTable() {
        Optional<Card> strongestCardOnTable = getStrongestCardOnTable();
        if(!strongestCardOnTable.isPresent()) {
            return myCards.stream();
        }
        return myCards.stream().filter(card -> card.beatsCard(strongestCardOnTable.get(), trumpf));
    }

    @Override
    public String toString() {
        return "GameState " + System.identityHashCode(this) + " {" +
                "myCards=" + myCards +
                ", trumpf=" + trumpf +
                ", cardsOnTable=" + cardsOnTable +
                ", iMadeTrumpf=" + iMadeTrumpf +
                ", round=" + round +
                '}';
    }

}
