package de.bethibande.launcher.networking.encryption;

import lombok.Getter;

import java.math.BigInteger;

public class PrivateKey {

    @Getter
    private final BigInteger d;
    @Getter
    private final BigInteger N;

    public PrivateKey(BigInteger d, BigInteger N) {
        this.d = d;
        this.N = N;
    }

    @Override
    public String toString() {
        return N.toString() + ";" + d.toString();
    }

    public static PrivateKey parseString(String s) {
        String[] keys = s.split(";");
        return new PrivateKey(new BigInteger(keys[1]), new BigInteger(keys[0]));
    }

}
