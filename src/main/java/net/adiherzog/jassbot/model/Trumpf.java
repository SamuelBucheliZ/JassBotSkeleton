package net.adiherzog.jassbot.model;

import com.google.common.base.Preconditions;
import com.google.gson.annotations.SerializedName;

public class Trumpf {

    private TrumpfMode mode;

    @SerializedName("trumpfColor")
    private Suit suit;

    /**
     * Must not be used for TrumpfMode.TRUMPF!
     */
    public Trumpf(TrumpfMode mode) {
        Preconditions.checkNotNull(mode);
        Preconditions.checkArgument(!TrumpfMode.TRUMPF.equals(mode));
        this.mode = mode;
    }

    /**
     * Use this for TrumpfMode.TRUMPF only!
     */
    public Trumpf(TrumpfMode mode, Suit suit) {
        Preconditions.checkNotNull(mode);
        Preconditions.checkArgument(TrumpfMode.TRUMPF.equals(mode));
        this.mode = mode;
        this.suit = suit;
    }

    public Suit getSuit() {
        return suit;
    }

    public TrumpfMode getMode() {
        return mode;
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

    public boolean isObenabeOrUndeufe() {
        return TrumpfMode.OBEABE.equals(mode) || TrumpfMode.UNDEUFE.equals(mode);
    }

}
