package de.bethibande.launcher.networking.encryption;

import lombok.Getter;

import java.math.BigInteger;

public class PublicKey {

    @Getter
    private final BigInteger e;
    @Getter
    private final BigInteger N;

    public PublicKey(BigInteger e, BigInteger N) {
        this.e = e;
        this.N = N;
    }

    @Override
    public String toString() {
        return N.toString() + ";" + e.toString();
    }

    public static PublicKey parseString(String s) {
        String[] keys = s.split(";");
        return new PublicKey(new BigInteger(keys[1]), new BigInteger(keys[0]));
    }

}
