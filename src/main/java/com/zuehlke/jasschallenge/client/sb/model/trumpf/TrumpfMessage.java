package com.zuehlke.jasschallenge.client.sb.model.trumpf;

import com.google.common.base.Preconditions;
import com.google.gson.annotations.SerializedName;
import com.zuehlke.jasschallenge.client.sb.model.cards.Suit;

public class TrumpfMessage {

    private TrumpfMode mode;

    @SerializedName("trumpfColor")
    private Suit suit;

    /**
     * Must not be used for TrumpfMode.TRUMPF!
     */
    public TrumpfMessage(TrumpfMode mode) {
        Preconditions.checkNotNull(mode);
        Preconditions.checkArgument(!TrumpfMode.TRUMPF.equals(mode));
        this.mode = mode;
    }

    /**
     * Use this for TrumpfMode.TRUMPF only!
     */
    public TrumpfMessage(TrumpfMode mode, Suit suit) {
        Preconditions.checkNotNull(mode);
        Preconditions.checkArgument(TrumpfMode.TRUMPF.equals(mode));
        this.mode = mode;
        this.suit = suit;
    }

    public Trumpf toTrumpf() {
        Trumpf trumpf;
        switch (mode) {
            case TRUMPF:
                trumpf = new TrumpfSuit(suit);
                break;
            case OBEABE:
                trumpf = new TrumpfObeabe();
                break;
            case UNDEUFE:
                trumpf = new TrumpfUndeufe();
                break;
            default:
                throw new IllegalStateException("Cannot create trumpf from trumpf message.");
        }
        return trumpf;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(mode);
        if(TrumpfMode.TRUMPF.equals(mode)) {
            builder.append(" ");
            builder.append(suit);
        }

        return builder.toString();
    }

}
